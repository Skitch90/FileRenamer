package it.alesc.filerenamer.filenamepatternmanager.filenames

import java.util.Date

import it.alesc.filerenamer.filenamepatternmanager.FileNamePatternManager
import it.alesc.filerenamer.util.Utils

import scala.util.matching.Regex

object DateFileName {
  val patterns = FileNamePatternManager.datePatterns

  def unapply(fileName: String): Option[(Date, String)] = {
    patterns.find({ case (x, _) => Utils.matches(x, fileName) }) match {
      case None => None
      case Some((regex, formatter)) => regex.findFirstMatchIn(fileName) match {
        case Some(x) => Some((formatter.parse(x.group("date")), x.group("extension")))
      }
    }
  }

  class DateExtGroupsRegex(regexStr: String)
    extends Regex("""^""" + regexStr + Utils.FILE_EXTENSION_REGEX + """$""", "date", "extension")

}