package it.alesc.filerenamer.util

import java.io.File
import java.util.{Calendar, Date}

import it.alesc.filerenamer.filenamepatternmanager.filenames.StructuredFileName
import it.alesc.filerenamer.navigation.FileNavigation
import it.alesc.filerenamer.navigation.impl.{RenameNavigation, ScanDirsNavigation, ScanFilesNavigation}
import it.alesc.filerenamer.prefixmanager.PrefixManager
import org.apache.commons.cli.CommandLine
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.time.DateUtils

import scala.util.matching.Regex

object Utils {
  val PATH_SEPARATOR: String = File.separator
  val FILE_EXTENSION_REGEX = """(\.[^.]+)"""

  def computeIndent(level: Int): String = StringUtils.repeat(" ", level * 4)

  def matches(pattern: Regex, string: String): Boolean = {
    pattern.unapplySeq(string) match {
      case Some(_) => true
      case None => false
    }
  }

  def computeNewFileName(parent: File, date: Date, counter: Int, ext: String): String = {

    val prefix = PrefixManager.computePrefix(parent)
    var newName = StructuredFileName(prefix, date, counter, ext)
    print(" New Name: " + newName)
    if (new File(parent.getAbsolutePath + PATH_SEPARATOR + newName).exists()) {
      print(""" File già esiste """)
      val modArray = parent.listFiles() filter
        (x => matches(StructuredFileName.regex, x.getName)) map
        (x => StructuredFileName.unapply(x.getName) match {
          case Some(tuple) => tuple
        })

      val maxCounter = modArray filter
        (equalsIgnoreCounter(_, (prefix, date, counter, ext))) map
        (_._3) max

      newName = StructuredFileName(prefix, date, maxCounter + 1, ext)
      print(" Updated New Name: " + newName)
    }
    newName
  }

  def equalsIgnoreCounter(sub: (String, Date, Int, String),
                          ref: (String, Date, Int, String)): Boolean =
    sub._1.equals(ref._1) &&
      DateUtils.truncatedEquals(sub._2, ref._2, Calendar.DAY_OF_MONTH) &&
      sub._4.equals(ref._4)

  def getNavigator(cl: CommandLine): (FileNavigation, Option[Boolean]) = {
    if (cl.hasOption("r")) {
      (new RenameNavigation(), Some(cl.hasOption("sk")))
    } else if (cl.hasOption("sd")) {
      (new ScanDirsNavigation(), None)
    } else if (cl.hasOption("sf")) {
      (new ScanFilesNavigation(), None)
    } else {
      throw new IllegalArgumentException()
    }
  }
}