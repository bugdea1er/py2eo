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
class DjangoIT extends Commons {

  private val djangoLink = "https://github.com/django/django"
  private val directory = Directory.makeTemp(prefix = "org.polystat.py2eo.")

  @Test
  @Order(1)
  def genUnsupportedDjango() : Unit = {
    val django = Directory(directory + "/django")

    Process(s"git clone -b 4.0 $djangoLink ${django.name}", directory.jfile).!!

    val tests = django.deepFiles.filter(_.extension == "py")
    for {test <- tests} yield {
        def db(s : Statement.T, str : String) = () // debugPrinter(test)(_, _)
        val name = test.name
        val eoText =
          Transpile.transpile(db)(
            chopExtension(name),
            Transpile.Parameters(wrapInAFunction = false, isModule = false),
            readFile(test.jfile)
          )
        writeFile(test.jfile, "genUnsupportedEO", ".eo", eoText)
      }
  }

  @Test
  @Order(2)
  def checkSyntaxForDjango() : Unit = {
    checkEOSyntaxInDirectory(Directory(directory + "/django").toString)
  }

}

