package navigation.impl

import java.io.File
import java.util.Date

import navigation.FileNavigation
import prefixmanager.PrefixManager
import util.Utils
import util.filenames.{DateCountFileName, NoDateFileName, StructuredFileName}

class RenameNavigation extends FileNavigation {
  def executeNavigation(file: File, skipDirsWithNoPrefix: Option[Boolean]): Unit = {
    PrefixManager.loadPrefixes()

    if (file.isDirectory()) {
      skipDirsWithNoPrefix match {
        case Some(b) => manageDirectory(file, 0, b)
        case None    => manageDirectory(file, 0)
      }
    } else {
      manageFile(file, 0)
    }

    PrefixManager.storePrefixes()
  }

  def manageFile(file: File, level: Int): Unit = {
    val parent = file.getParentFile()

    val newName = file.getName() match {
      case StructuredFileName(_) => return
      case DateCountFileName(date, counter, ext) =>
        print(Utils.computeIndent(level) + "File: " + file.getName)
        Utils.computeNewFileName(parent, date, counter, ext)
      case NoDateFileName(ext) =>
        print(Utils.computeIndent(level) + "File: " + file.getName)
        Utils.computeNewFileName(parent, new Date(file.lastModified()), 1, ext)
      case _ => return
    }

    val newFile = new File((parent.getAbsolutePath) + Utils.PATH_SEPARATOR + newName)
    if (file renameTo (newFile)) {
      println(""" Renamed""")
    } else {
      println()
    }
  }
}