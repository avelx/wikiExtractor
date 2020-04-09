import sbtassembly.MergeStrategy

name := "wikiExtractor"

version := "0.1"

scalaVersion := "2.12.10"

val jarFileName = "wiki-extractor.jar"

libraryDependencies += "org.apache.spark" %% "spark-core" % "2.4.5"

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

//assemblyMergeStrategy in assembly := {
//  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
//  case _ => MergeStrategy.first
//}
val meta = """META.INF(.)*""".r

assemblyMergeStrategy in assembly := {
  case PathList("javax", "servlet", xs @ _*) => MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith ".html" => MergeStrategy.first
  case n if n.contains("services") => MergeStrategy.concat
  case n if n.startsWith("reference.conf") => MergeStrategy.concat
  case n if n.endsWith(".conf") => MergeStrategy.concat
  case meta(_) => MergeStrategy.discard
  case x => MergeStrategy.first
}

fork := true