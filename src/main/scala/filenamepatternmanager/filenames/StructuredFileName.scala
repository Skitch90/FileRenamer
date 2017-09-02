package filenamepatternmanager.filenames

import java.text.SimpleDateFormat
import java.util.Date

import org.apache.commons.lang3.StringUtils

import scala.util.matching.Regex

object StructuredFileName {

  val targetDateFormatter = new SimpleDateFormat("yyyy-MM-dd")
  val regex = new Regex("""^([a-z]{3})_(\d{4}-\d{2}-\d{2})_(\d{1,2})(\..+)$""",
    "prefix", "date", "counter", "extension")

  def unapply(str: String): Option[(String, Date, Int, String)] = {
    regex.findFirstMatchIn(str) match {
      case Some(matchObj) => Some((matchObj.group("prefix"),
        targetDateFormatter.parse(matchObj.group("date")),
        matchObj.group("counter").toInt,
        matchObj.group("extension")))
      case None => None
    }
  }

  def apply(prefix: String, date: Date, counter: Int, extension: String): String = {
    Array(
      prefix,
      targetDateFormatter.format(date),
      StringUtils.leftPad(counter toString, 2, "0")).mkString("", "_", extension)
  }

}