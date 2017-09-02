package filenamepatternmanager

import java.io.File
import java.text.SimpleDateFormat

import filenamepatternmanager.filenames.DateCountFileName.DateCountExtGroupsRegex
import filenamepatternmanager.filenames.NoDateFileName.ExtGroupRegex

import scala.xml.XML

object FileNamePatternManager {
  val configFileName = """filename_patterns.xml"""
  var dateCountPatterns: Map[DateCountExtGroupsRegex, SimpleDateFormat] = Map[DateCountExtGroupsRegex, SimpleDateFormat]()
  var noDatePatterns: List[ExtGroupRegex] = List[ExtGroupRegex]()

  def loadPatterns(): Unit = {
    val xmlFile = new File(configFileName)
    if (xmlFile.exists()) {
      val xml = XML.loadFile(xmlFile)

      (xml \ "dateCountPattern") foreach( (node) =>
          dateCountPatterns += ( new DateCountExtGroupsRegex((node \ "pattern").text) -> new SimpleDateFormat((node \ "dateFormat").text))
        )

      noDatePatterns = (xml \ "noDatePattern" \ "pattern").toList map ((n) => new ExtGroupRegex(n.text))
    } else {
      println("File Not Found: " + configFileName)
    }
  }
}
