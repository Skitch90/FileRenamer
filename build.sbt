name := "FileRenamer"

version := "1.0"

scalaVersion := "2.12.2"

libraryDependencies += "commons-cli" % "commons-cli" % "1.4"
libraryDependencies += "commons-io" % "commons-io" % "2.5"
libraryDependencies += "org.apache.commons" % "commons-lang3" % "3.5"
libraryDependencies += "org.scala-lang.modules" % "scala-xml_2.12" % "1.0.6"

//sourceManaged in Compile := (scalaSource in Compile).value / "model"