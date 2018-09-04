package VertxLoader

import io.vertx.core.{AsyncResult, Vertx}

import scala.concurrent.Future
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global


/**
  * Trait to describe a generic VertxFile.
  */
sealed trait VertxFile {
  def filePath: String
}

/**
  * Companion Object for the trait VertxFile.
  */
object VertxFile {

  private[this] val loader: Vertx = Vertx.vertx()
  import java.io._


  /**
    * Apply method for building a vertxfile with a specified nestingLevel
    *
    * @param filePath     the path of the file to open
    * @return a Optional containing a object a VertxFile if the file exists, None otherwise.
    */
  def apply(filePath: String): Option[VertxFile] = filePath match {
    case VertxFile(file) if file.isDirectory => Some(VertxFolder(filePath))
    case VertxFile(file) => Some(VertxDocument(filePath))
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

  private[this] def buildFileList(filePath:String, nestingLevel:Int):Future[List[VertxFile]] = ???

  def scanAndApply[A](filePath:String, nestingLevel:Int, strategy: List[VertxFile] => A):Future[A] =
    buildFileList(filePath, nestingLevel).map(strategy)

  /**
    * This class represent a document to compute.
    * Package private for test purpose.
    *
    * @param filePath the path of the file to open.
    */
  private[VertxLoader] case class VertxDocument(override val filePath: String) extends VertxFile

  /**
    * This class represent a folder to compute.
    * Package private for test purpose.
    *
    * @param filePath the path of the file to open.
    */
  private[VertxLoader] case class VertxFolder(override val filePath: String) extends VertxFile/* {

    /**
      * Import used to convert Java collection into scala ones.
      */

    import scala.collection.JavaConverters._


    private[this] val filterDocument: VertxFile => Boolean = (file: VertxFile) => file.isInstanceOf[VertxDocument]

    private[this] val mapToVertxFile: String => VertxFile = (filePath: String) => VertxFile(filePath).get

    private[this] val handler = (result: AsyncResult[java.util.List[String]]) => result match {

      case result: AsyncResult[java.util.List[String]] if result.succeeded() && nestingLevel > 0 =>
        result.result().forEach(path => VertxFile(path, nestingLevel - 1).get.computeFile())

      case result: AsyncResult[java.util.List[String]] if result.succeeded() =>
        result.result().asScala.toStream.map(mapToVertxFile).filter(filterDocument).foreach(f => f.computeFile())
      case _ => println(result.cause())
    }

    override def computeFile(): Unit = {
      loader.fileSystem.readDir(filePath, handler)
    }
  }*/

}


