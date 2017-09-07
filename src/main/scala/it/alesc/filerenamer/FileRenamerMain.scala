package it.alesc.filerenamer

import java.io.{File, FileOutputStream}

import it.alesc.filerenamer.filenamepatternmanager.FileNamePatternManager
import it.alesc.filerenamer.util.{CLIUtils, Utils}
import org.apache.commons.cli.DefaultParser
import org.apache.commons.io.output.TeeOutputStream

object FileRenamerMain {
  def main(args: Array[String]): Unit = {
    val parser = new DefaultParser()
    val cmd = parser.parse(CLIUtils.buildCommandLineOptions(), args)

    val inputFile = new File(cmd.getOptionValue("p"))
    if (!inputFile.exists()) {
      throw new IllegalArgumentException(
        """Il path "%s" non è valido""".format(inputFile.getAbsolutePath))
    }

    FileNamePatternManager.loadPatterns()

    val executeNav = () => {
      val (fNav, sk) = Utils.getNavigator(cmd)
      fNav.executeNavigation(inputFile, sk)
    }

    if (cmd.hasOption("o")) {
      val outputFile = new File(cmd.getOptionValue("o"))

      Console.withOut(new TeeOutputStream(System.out,
        new FileOutputStream(outputFile)))(executeNav())
    } else {
      executeNav()
    }

  }

}