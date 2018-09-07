package utilities

import VertxLoader.VertxFile

object RegularExpressionMatcher {

  def matchRegularExpression(fileList: List[VertxFile], regularExpression: String): Unit = ???

  implicit class TextString(text: String) {
    def words(): Iterable[String] = text.split("\\W+").toIterable
  }

}
