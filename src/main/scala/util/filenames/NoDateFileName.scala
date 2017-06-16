package util.filenames

import util.Utils

import scala.util.matching.Regex

object NoDateFileName {
  val patterns = List(
    new ExtGroupRegex("""(?:FB_IMG_|facebook_)"""),
    new ExtGroupRegex("""[0-9a-f]{8}-(?:[0-9a-f]{4}-){3}[0-9a-f]{12}"""),
    new ExtGroupRegex("""\d{2,4} - \d(?: \(\d+\))?"""),
    new ExtGroupRegex("""[0-9a-f]{32}"""),
    new ExtGroupRegex("""\d{8}_\d{16}_\d{19}_n"""),
    new ExtGroupRegex("""- - -.*"""),
    new ExtGroupRegex("""image(?: \(\d\))?"""))

  def unapply(str: String): Option[String] = {
    patterns find (Utils.matches(_, str)) match {
      case None => None
      case Some(pattern) => pattern.findFirstMatchIn(str) match {
        case Some(theMatch) => Some(theMatch.group("extension"))
      }
    }
  }

  class ExtGroupRegex(regexStr: String)
    extends Regex("""^""" + regexStr + """(\..+)$""", "extension")
}