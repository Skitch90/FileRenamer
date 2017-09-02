package navigation.impl

import java.io.File

import navigation.FileNavigation
import util.filenames.StructuredFileName

/**
  * Created by alesc on 23/03/2017.
  */
class ScanFilesNavigation extends FileNavigation {
  override def executeNavigation(file: File, skipDirsWithNoPrefix: Option[Boolean]): Unit = {
    if (file.isDirectory) {
      manageDirectory(file, 0)
    } else {
      manageFile(file, 0)
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
      case _ => Console.println(file.getAbsolutePath)
    }
  }
}
