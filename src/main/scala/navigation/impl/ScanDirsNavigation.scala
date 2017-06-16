package navigation.impl

import java.io.File

import navigation.DirsNavigation
import prefixmanager.PrefixManager

/**
  * Created by alesc on 23/03/2017.
  */
class ScanDirsNavigation extends DirsNavigation {
  override def executeNavigation(file: File, skipDirsWithNoPrefix: Option[Boolean]): Unit = {
    PrefixManager.loadPrefixes()

    if (file.isDirectory) {
      manageDirectory(file, 0)
    }
  }

  override def manageDirectory(dir: File, level: Int): Unit = {
    if (!PrefixManager.mapContains(dir.getName)) Console.println(dir.getAbsolutePath)

    dir.listFiles() filter(_.isDirectory) foreach(manageDirectory(_, level + 1))
  }
}
