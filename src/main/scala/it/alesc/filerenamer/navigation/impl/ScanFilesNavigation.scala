package it.alesc.filerenamer.navigation.impl

import java.io.File

import it.alesc.filerenamer.filenamepatternmanager.filenames.{DateCountFileName, DateFileName, NoDateFileName, StructuredFileName}
import it.alesc.filerenamer.navigation.FileNavigation

/**
  * Created by alesc on 23/03/2017.
  */
class ScanFilesNavigation extends FileNavigation {
  override def executeNavigation(file: File, skipDirsWithNoPrefix: Option[Boolean]): Unit = {
    if (file.isDirectory) {
      manageDirectory(file)
    } else {
      manageFile(file)
    }
  }

  override def manageDirectory(dir: File, level: Int, skipDirsWithNoPrefix: Boolean): Unit = {
    val (directories, files) = dir.listFiles() partition (x => x.isDirectory)
    directories foreach (manageDirectory(_, level + 1))
    files foreach(manageFile(_, level + 1))
  }

  override def manageFile(file: File, level: Int): Unit = {
    file.getName match {
      case StructuredFileName(_) =>
      case DateCountFileName(_) => Console.println(file.getAbsolutePath + " - Known pattern: Y")
      case DateFileName(_) => Console.println(file.getAbsolutePath + " - Known pattern: Y")
      case NoDateFileName(_) => Console.println(file.getAbsolutePath + " - Known pattern: Y")
      case _ => Console.println(file.getAbsolutePath + " - Known pattern: N")
    }
  }
}
