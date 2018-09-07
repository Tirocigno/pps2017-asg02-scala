import java.util.function.Predicate

import VertxLoader.VertxFile.VertxDocument
import io.vertx.core.Handler

package object VertxLoader {

  import io.vertx.scala.core._

  import scala.concurrent.ExecutionContext.Implicits.global
  import scala.language.implicitConversions

  /**
    * Convert a scala method to a java Handler.
    *
    * @param f the function to use as handler.
    * @tparam A the input data type of the handler.
    * @return an Handler object.
    */
  implicit def functionToHandler[A](f: A => Unit): Handler[A] = (event: A) => f(event)

  /**
    * Convert a scala method to a java Predicate.
    *
    * @param predicate the function to use as predicate.
    * @tparam A the input data type of the predicate.
    * @return a Predicate object.
    */
  implicit def functionToPredicate[A](predicate: A => Boolean): Predicate[A] = (condition: A) => predicate(condition)

  /**
    * Asynchronously open a document and convert its content to a string.
    *
    * @param document the VertxDocument to open.
    * @return a future containing the content of the document.
    */
  def loadVertxFile(document: VertxDocument) = {
    Vertx.vertx().fileSystem().readFileFuture(document.filePath) map (_.toString)
  }

}
