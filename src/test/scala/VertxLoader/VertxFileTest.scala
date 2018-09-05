package VertxLoader

import VertxLoader.VertxFile.VertxDocument
import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner

import scala.concurrent.Await
import scala.concurrent.duration.Duration


@RunWith(classOf[JUnitRunner])
class VertxFileTest extends FunSuite {

  def defaultNestingLevel = 0

  def complexNestingLevel = 2

  def defaultElse: () => VertxFile = () => {
    fail(); VertxFile(defaultResourcesPath).get
  }

  def defaultResourcesPath = "src/test/resources"


  test("Non existing folder scan result in None") {
    val filePath = defaultResourcesPath + "/canides"
    assert(VertxFile(filePath) isEmpty)
  }

  test("Existing folder scan result in Some") {
    val filePath = defaultResourcesPath
    assert(VertxFile(filePath) isDefined)
  }

  test("File instantiation") {
    val filePath = defaultResourcesPath + "/cat.txt"
    assert(VertxFile(filePath).getOrElse(defaultElse).isInstanceOf[VertxFile.VertxDocument])
  }

  test("Folder instantiaton") {
    val filePath = defaultResourcesPath + "/felides"
    assert(VertxFile(filePath).getOrElse(defaultElse).isInstanceOf[VertxFile.VertxFolder])
  }

  test("Folder scanning with nesting level equals to zero") {
    val strategy: List[VertxFile] => Int = list => list count(file => file.isInstanceOf[VertxDocument])
    val future = VertxFile.scanAndApply(defaultResourcesPath, complexNestingLevel, strategy)
    assert(Await.ready(future, Duration.Inf).value.get.get == 1)
  }

}
