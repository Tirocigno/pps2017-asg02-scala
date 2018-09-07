package utilities

import org.scalatest.FunSuite
import utilities.RegularExpressionMatcher._

class RegularExpressionMatcherTest extends FunSuite {

  test("Split test on a string") {
    val text = "Sul tagliere l'aglio taglia, non tagliare la tovaglia!"
    assert(text.words().size == 9)
  }

  test("Split test on multiple lines string") {
    val text = "Nel cammin di nostra vita \n , mi ritrovai per una selva oscura\n"
    assert(text.words().size == 11)
  }

}
