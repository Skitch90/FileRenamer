package filenamepatternmanager.filenames

import java.util.Date

import filenamepatternmanager.FileNamePatternManager
import util.Utils

import scala.util.matching.Regex

object DateCountFileName {
  val patterns = FileNamePatternManager.dateCountPatterns

  def unapply(fileName: String): Option[(Date, Int, String)] = {
    patterns.find({ case (x, _) => Utils.matches(x, fileName) }) match {
        case None => None
        case Some((regex, formatter)) => regex.findFirstMatchIn(fileName) match {
          case Some(x) => Some((formatter.parse(x.group("date")),
            x.group("counter").toInt, x.group("extension")))
        }
      }
  }

  class DateCountExtGroupsRegex(regexStr: String)
    extends Regex("""^""" + regexStr + """(\..+)$""", "date", "counter", "extension")
}