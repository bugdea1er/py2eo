package org.polystat.py2eo.transpiler

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.junit.jupiter.api.{Order, Test, TestMethodOrder}
import org.polystat.py2eo.parser.Statement
import org.polystat.py2eo.transpiler.Common.dfsFiles

import java.io.File
import java.nio.file.{Files, StandardCopyOption}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.reflect.io.Directory
import scala.sys.process.Process

@TestMethodOrder(classOf[OrderAnnotation])
class DjangoTest extends Commons {

  @Test
  @Order(1)
  def genUnsupportedDjango() : Unit = {
    val root = new File(testsPrefix)
    val django = new File(testsPrefix + "/django")
    if (!django.exists()) {
      //      assert(0 == Process("git clone file:///home/bogus/pythonProjects/django", root).!)
      assert(0 == Process("git clone -b 4.0 https://github.com/django/django", root).!)
    }
    val test = dfsFiles(django).filter(f => f.getName.endsWith(".py"))
    val futures = test.map(test =>
//      Future
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
//    for (f <- futures) Await.result(f, Duration.Inf)
  }

  @Test
  @Order(2)
  def checkSyntaxForDjango() : Unit = {
    checkEOSyntaxInDirectory(testsPrefix + "/django")
  }

}

