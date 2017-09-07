package it.alesc.filerenamer.filenamepatternmanager.filenames

import it.alesc.filerenamer.filenamepatternmanager.FileNamePatternManager
import it.alesc.filerenamer.util.Utils

import scala.util.matching.Regex

object NoDateFileName {
  val patterns = FileNamePatternManager.noDatePatterns

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