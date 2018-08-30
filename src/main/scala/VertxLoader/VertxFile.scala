package VertxLoader

import io.vertx.core.{AsyncResult, Handler, Vertx}


trait VertxFile {

  def filePath:String
  def openFile():Unit
}

object VertxFile {

  private[this] val noNesting = 0

  import java.io._


  def apply(filePath:String): Option[VertxFile] = this(filePath, noNesting)

  def apply(filePath:String, nestingLevel:Int): Option[VertxFile] = filePath match {
      case VertxFile(file) if file.isDirectory => Some(new VertxFolder(filePath, nestingLevel))
      case VertxFile(file) => Some(new VertxDocument(filePath))
      case _ => None
  }

  def unapply(filePath: String): Option[File] = {val file = new File(filePath); file match {
    case file:File if file.exists() => Some(file)
    case _ => None
  }}





  private class VertxDocument(override val filePath: String) extends VertxFile {
    override def openFile(): Unit = ???
  }

  private class VertxFolder(override val filePath: String, val nestingLevel:Int) extends VertxFile {


    val handler:Handler[AsyncResult[List[String]]] = (result:AsyncResult[List[String]]) => result match {
      case AsyncResult if result.succeeded() => result.result().foreach(path => VertxFile(path, nestingLevel -1).get.openFile)
      case _ => result.cause()
    }

    override def openFile(): Unit = {
      Vertx.vertx.fileSystem.readDir(filePath, handler)

    }

  }

}


