import sbtassembly.MergeStrategy

name := "wikiExtractor"

version := "0.1"

scalaVersion := "2.12.10"

val jarFileName = "wiki-extractor.jar"

libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.4.5"

libraryDependencies += "com.databricks" %% "spark-xml" % "0.9.0"

libraryDependencies += "com.typesafe" % "config" % "1.4.0"

libraryDependencies += "org.apache.logging.log4j" % "log4j-api" % "2.12.1"

libraryDependencies += "org.apache.logging.log4j" % "log4j-core" % "2.12.1"

mainClass in assembly := Some("com.avel.data.WikiExtractor")

assemblyJarName in assembly := jarFileName

assemblyOutputPath in assembly := file(s"build/$jarFileName")

assemblyOption in assembly :=
  (assemblyOption in assembly).value.copy(includeScala = true, includeDependency = true)

test in assembly := {}

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case _ => MergeStrategy.first
}

fork := true