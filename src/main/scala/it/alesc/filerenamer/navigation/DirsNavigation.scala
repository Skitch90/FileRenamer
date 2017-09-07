package it.alesc.filerenamer.navigation

import java.io.File

abstract class DirsNavigation extends FileNavigation {
  def manageFile(file: File, level: Int): Unit = {}
}