package org.polystat.py2eo.transpiler

import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import org.polystat.py2eo.parser.Statement
import org.polystat.py2eo.transpiler.Common.dfsFiles

import java.io.File
import java.nio.file.{Files, StandardCopyOption}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.reflect.io.Directory
import scala.sys.process.Process

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class DjangoTest extends Commons {

  @Test def aGenUnsupportedDjango() : Unit = {
    val root = new File(testsPrefix)
    val django = new File(testsPrefix + "/django")
    if (!django.exists()) {
      assert(0 == Process("git clone -b 4.0 https://github.com/django/django", root).!)
    }
    val test = dfsFiles(django).filter(f => f.getName.endsWith(".py"))
    val futures = test.map(test =>
      {
        def db(s : Statement.T, str : String) = () // debugPrinter(test)(_, _)
        val name = test.getName
        println(s"parsing $name")
        val eoText = try {
          Transpile.transpile(db)(
            chopExtension(name),
            Transpile.Parameters(wrapInAFunction = false, isModule = false),
            readFile(test)
          )
        } catch {
          case e : Throwable =>
            println(s"failed to transpile $name: ${e.toString}")
            throw e
        }
        writeFile(test, "genUnsupportedEO", ".eo", eoText)
      }
    )
  }

  @Test def bCheckSyntaxForDjango() : Unit = {
    checkEOSyntaxInDirectory(testsPrefix + "/django")
  }

}

