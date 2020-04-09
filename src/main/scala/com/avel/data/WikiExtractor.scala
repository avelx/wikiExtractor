package com.avel.data

import org.apache.log4j.Logger

object WikiExtractor {

  import org.apache.spark.SparkConf
  import com.typesafe.config.ConfigFactory
  import org.apache.spark.sql.SparkSession
  import com.databricks.spark.xml._

  def main(args: Array[String]): Unit = {

    val logger = Logger.getLogger(this.getClass.getName)

    val appConfig = ConfigFactory.load()
    val xmlFile: String = "/mnt/diska/enwiki-20200401-pages-articles.xml"
    logger.info(s"FILE NAME: $xmlFile")

    val conf = new SparkConf()
      .setMaster("local[8]")
      .setAppName("WikiPedia")

    val spark = SparkSession
      .builder()
      .appName("Article Extractor")
      .config(conf)
      .getOrCreate()

    try {
      spark.sparkContext.hadoopConfiguration.set("fs.file.impl", "org.apache.hadoop.fs.LocalFileSystem")

      val df = spark.read
        .format("xml")
        .option("rowTag", "page")
        .xml(xmlFile)

      df.show(10)

      df.printSchema()

    } catch {
      case ex: Throwable => {
        logger.error(ex.getMessage)
        logger.error(ex.getStackTrace.mkString(" "))
        spark.close()
      }
    }

    spark.close()
  }
}
