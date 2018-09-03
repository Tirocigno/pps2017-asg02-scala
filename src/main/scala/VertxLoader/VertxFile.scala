package VertxLoader

import io.vertx.core.{AsyncResult, Vertx}

/**
  * Trait to describe a generic VertxFile.
  */
trait VertxFile {
  def filePath: String

  /**
    * Open the file at a specified path and process it.
    */
  def computeFile(): Unit
}

/**
  * Companion Object for the trait VertxFile.
  */
object VertxFile {

  private[this] val noNesting = 0

  val loader:Vertx = Vertx.vertx()

  import java.io._


  /**
    * Apply method for build a VertxFile object without a nesting level specified.
    *
    * @param filePath the path of the file to open
    * @return a Optional containing a object a VertxFile if the file exists, None otherwise.
    */
  def apply(filePath: String): Option[VertxFile] = this (filePath, noNesting)

  /**
    * Apply method for building a vertxfile with a specified nestingLevel
    *
    * @param filePath     the path of the file to open
    * @param nestingLevel nesting level to reach in subfolders(used if the file is a folder)
    * @return a Optional containing a object a VertxFile if the file exists, None otherwise.
    */
  def apply(filePath: String, nestingLevel: Int): Option[VertxFile] = filePath match {
    case VertxFile(file) if file.isDirectory => Some(new VertxFolder(filePath, nestingLevel))
    case VertxFile(file) => Some(new VertxDocument(filePath))
    case _ => None
  }

  /**
    * Unapply method to extract a file from a path.
    *
    * @param filePath the path of the file to open
    * @return A file if the file exists, None otherwise
    */
  def unapply(filePath: String): Option[File] = {
    val file = new File(filePath)
    file match {
      case file: File if file.exists() => Some(file)
      case _ => None
    }
  }

  /**
    * This class represent a document to compute.
    * Package private for test purpose.
    *
    * @param filePath the path of the file to open.
    */
  private[VertxLoader] class VertxDocument(override val filePath: String) extends VertxFile {
    override def computeFile(): Unit = ???
  }

  /**
    * This class represent a folder to compute.
    * Package private for test purpose.
    *
    * @param filePath the path of the file to open.
    */
  private[VertxLoader] class VertxFolder(override val filePath: String, val nestingLevel: Int) extends VertxFile {

    private[this] val filterDocument: VertxFile => Boolean = (file:VertxFile) => file.isInstanceOf[VertxDocument]

    private[this]val handler = (result: AsyncResult[java.util.List[String]]) => result match {

      case result: AsyncResult[java.util.List[String]] if result.succeeded() && nestingLevel > 0 =>
        result.result().forEach(path => VertxFile(path, nestingLevel - 1).get.computeFile())

      case result: AsyncResult[java.util.List[String]] if result.succeeded() =>
        result.result().stream().map(filePath => VertxFile(filePath).get).filter(filterDocument).forEach(document => document.computeFile())
      case _ => println(result.cause())
    }

    override def computeFile(): Unit = {
      loader.fileSystem.readDir(filePath, handler)
    }
  }

}


