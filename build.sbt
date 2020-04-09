import sbtassembly.MergeStrategy

name := "wikiExtractor"

version := "0.1"

scalaVersion := "2.12.10"

val jarFileName = "wiki-extractor.jar"

libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.4.5"

libraryDependencies += "com.databricks" %% "spark-xml" % "0.9.0"

libraryDependencies += "com.typesafe" % "config" % "1.4.0"

mainClass in assembly := Some("com.avel.data.WikiExtractor")

assemblyJarName in assembly := jarFileName

assemblyOutputPath in assembly := file(jarFileName)

assemblyOption in assembly :=
  (assemblyOption in assembly).value.copy(includeScala = true)

test in assembly := {}

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}

fork := true