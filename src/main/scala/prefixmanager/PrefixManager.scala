package prefixmanager

import java.io.File
import java.util.Scanner

import org.apache.commons.lang3.StringEscapeUtils
import util.Utils
import util.filenames.StructuredFileName

import scala.collection.immutable
import scala.xml.{Elem, PrettyPrinter, XML}

object PrefixManager {
  private var prefixMap = Map[String, String]()
  private var modifiedMap = false
  val prefixesFile = new File("""prefixes""")
  val prefixesXmlFileName = "prefixes.xml";

  def mapContains(key: String): Boolean = prefixMap.contains(key)

  def computePrefix(dir: File): String = {
    val dirName = dir.getName()
    prefixMap.contains(dirName) match {
      case true => prefixMap(dirName)
      case false => searchPrefixFromFiles(dir) match {
        case Some(p) => p
        case None    => prefixFromInput(dirName)
      }
    }
  }

  def searchPrefixFromFiles(dir: File): Option[String] = {
    val prefixSet = dir.listFiles() filter
      (x => Utils.matches(StructuredFileName.regex, x.getName())) map
      (x => StructuredFileName.unapply(x.getName()) match {
        case Some(x) => x._1
      }) toSet

    prefixSet.size match {
      case 1 => {
        val p = prefixSet.iterator.next()
        prefixMap += (dir.getName() -> p)
        modifiedMap = true
        Some(p)
      }
      case _ => None
    }
  }

  def prefixFromInput(dirName: String): String = {
    println(" Insert the prefix for directory " + dirName + ":")
    val input = new Scanner(System.in).nextLine()
    val filteredMap = prefixMap filterKeys (prefixMap(_).equals(input))
    if (filteredMap.isEmpty) {
      prefixMap += (dirName -> input)
      modifiedMap = true
      input
    } else {
      println("Value " + input + " in use for directory/ies " +
        filteredMap.keys.mkString(","))
      prefixFromInput(dirName)
    }
  }

  def loadPrefixes() = {
    val inputFile = new File(prefixesXmlFileName)
    if (inputFile.exists()) {
      val xml = XML.loadFile(prefixesXmlFileName)
      val prefixList = (xml \\ "prefix") foreach (
        prefix => prefixMap += (
          StringEscapeUtils.unescapeHtml4((prefix \ "@dir").toString()) -> (prefix \ "@prf").toString())
        )

      println("Loaded map " + prefixMap.toString())
    }
  }

  def storePrefixes() = {
    if (modifiedMap) {
      val prefixElemsList: immutable.Iterable[Elem] = prefixMap map(x => <prefix prf={x._2} dir={x._1} />)
      val prefixes = <prefixes>{prefixElemsList}</prefixes>
      val prettyPrinter = new PrettyPrinter(80, 2)
      val outXml = XML.loadString(prettyPrinter.format(prefixes))
      XML.save(prefixesXmlFileName, outXml, "UTF-8", true, null)

      println("Storing map " + prefixMap.toString())
    }
  }

  def parsePrefixMapping(str: String): Option[(String, String)] = {
    if (str.length() < 3 || (str count (_.equals('='))) != 1) {
      None
    } else {
      val parts = str.split("=")
      Some(parts(0), parts(1))
    }
  }

}