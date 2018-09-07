package utilities

import VertxLoader.VertxFile.VertxDocument
import VertxLoader._
import utilities.StatisticsStorage._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.matching.Regex
import scala.util.{Failure, Success}

object RegularExpressionMatcher {

  def matchRegularExpression(fileList: List[VertxFile], regularExpression: String): Unit =
    fileList.filter(_.isInstanceOf[VertxDocument]).map(_.asInstanceOf[VertxDocument]) foreach (d => readVertxFile(d)
      .map(_.words)
      .map(_.filter(checkRegExp(_, regularExpression.r)))
      .map(_.size)
      .onComplete({
        case Success(occurences) => addFile(d.filePath, occurences)
        case Failure(exception) => throw new IllegalStateException(exception)
      }))

  implicit class TextString(text: String) {
    def words(): Iterable[String] = text.split("\\W+").toIterable
  }

  implicit def checkRegExp(word: String, regExp: Regex): Boolean = word match {
    case regExp(_) => true
    case _ => false
  }

}
