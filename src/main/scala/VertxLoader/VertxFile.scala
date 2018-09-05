package VertxLoader

import io.vertx.scala.core.Vertx
import io.vertx.scala.core.file.FileSystem

import scala.concurrent.{Future, Promise}
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


  private def getSubFolderDocuments(filePath:String, nestingLevel:Int):Future[List[VertxFile]] =
    loader.fileSystem()
      .readDirFuture(filePath)
      .map(buffer => Future.sequence(buffer.toStream.map(path => buildFileList(path, nestingLevel-1))))
      .flatten
      .map(s => s.flatten.toList)






  private def getFolderDocuments(folderPath:VertxFolder):Future[List[VertxFile]] = loader.fileSystem()
    .readDirFuture(folderPath.filePath).map(buffer => buffer.toStream.map(s => VertxFile(s).get).toList)

  private[this] def buildFileList(root:String, nestingLevel:Int):Future[List[VertxFile]] = VertxFile(root).get match {
      case document: VertxDocument =>  Future{List(document)}
      case folder:VertxFolder if nestingLevel == 0 => getFolderDocuments(folder)
      case folder: VertxFolder if nestingLevel > 0 => getSubFolderDocuments(root, nestingLevel)
      case _ => Promise.failed(new IllegalStateException("Error in opening "+root)).future
  }
  def scanAndApply[A](filePath:String, nestingLevel:Int, strategy: List[VertxFile] => A):Future[A] =
    buildFileList(filePath, nestingLevel).map(strategy)

  /**
    * This class represent a document to compute.
    * Package private for test purpose.
    *
    * @param filePath the path of the file to open.
    */
  case class VertxDocument(override val filePath: String) extends VertxFile

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


