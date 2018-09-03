package VertxLoader

import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class VertxFileTest extends FunSuite {

  def defaultResourcesPath = "src/test/resources"

  def defaultNestingLevel = 0

  def complexNestingLevel = 2

  def defaultElse:()=>VertxFile = () => {fail(); VertxFile(defaultResourcesPath).get}


  test("Non existing folder scan result in None") {
    val filePath = defaultResourcesPath + "/canides"
    assert(VertxFile(filePath) isEmpty)
  }

  test("Existing folder scan result in Some") {
    val filePath = defaultResourcesPath
    assert(VertxFile(filePath) isDefined)
  }

  test("File instantiation") {
    val filePath = defaultResourcesPath +  "/cat.txt"
    assert(VertxFile(filePath).getOrElse(defaultElse).isInstanceOf[VertxFile.VertxDocument])
  }

  test("Folder instantiaton") {
    val filePath = defaultResourcesPath +  "/felides"
    assert(VertxFile(filePath).getOrElse(defaultElse).isInstanceOf[VertxFile.VertxFolder])
  }

}
