package util

import org.apache.commons.cli.{Option, OptionGroup, Options}

object CLIUtils {
  def buildCommandLineOptions(): Options = {
    val cmdOpts = new Options()

    //Input
    cmdOpts.addOption(Option.builder("p").hasArg().required()
      .argName("input-path").desc("input file/directory path").build())

    //Operations
    val operations = new OptionGroup()
    operations.setRequired(true)
    operations.addOption(Option.builder("r").longOpt("rename")
      .desc("perform renaming on file or files in the directory").build())
    operations.addOption(Option.builder("sf").longOpt("scan-file")
      .desc("scan the file or the directory for files with names that not follow a default pattern")
      .build())
    operations.addOption(Option.builder("sd").longOpt("scan-dirs")
      .desc("scan the directory for directories not associated with prefixes").build())
    cmdOpts.addOptionGroup(operations)
    cmdOpts.addOption("sk", "skip-dirs-no_prefix", false,
      "skip the renaming for files contained in a directory with no associated "
        + "prefix. This option can only be used with -r or --rename")

    //  Output
    cmdOpts.addOption(Option.builder("o").hasArg().argName("output-path")
      .desc("path of a file to save the computation result").build())

    cmdOpts
  }
}