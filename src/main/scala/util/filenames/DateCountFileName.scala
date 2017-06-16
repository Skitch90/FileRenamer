package util.filenames

import java.text.SimpleDateFormat
import java.util.Date

import util.Utils

import scala.util.matching.Regex

object DateCountFileName {
  val patterns = Map(
    new DateCountExtGroupsRegex("""(\d{2}\.\d{2}\.\d{2})[ ]?-[ ]?(\d+).*""")
      -> new SimpleDateFormat("dd.MM.yy"),
    new DateCountExtGroupsRegex("""(\d{2}\.\d{2}\.\d{4})[ ]?-[ ]?(\d+).*""")
      -> new SimpleDateFormat("dd.MM.yyyy"),
    new DateCountExtGroupsRegex("""(\d{4}-\d{2}-\d{2})_(\d+).*""")
      -> new SimpleDateFormat("yyyy-MM-dd"),
    new DateCountExtGroupsRegex("""(\d{4}-\d{2}-\d{2}) - (\d+).*""")
      -> new SimpleDateFormat("yyyy-MM-dd"),
    new DateCountExtGroupsRegex("""(\d{2}-\d{2}-\d{2})[ ]?-[ ]?(\d+).*""")
      -> new SimpleDateFormat("dd-MM-yy"))

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