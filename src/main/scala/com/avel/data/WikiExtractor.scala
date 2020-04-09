package com.avel.data

import org.apache.spark.SparkConf

object WikiExtractor {

  import com.databricks.spark.xml._
  import com.typesafe.config.ConfigFactory
  import org.apache.spark.sql.SparkSession

  def main(args: Array[String]) : Unit = {

    val appConfig = ConfigFactory.load()
    val xmlFile : String = appConfig.getString("data.file")

    val conf = new SparkConf()
      .setMaster("local[8]")
      .setAppName("WikiPedia")

    val spark = SparkSession
      .builder()
      .appName("Article Extractor")
      .config(conf)
      .getOrCreate()


    //val df = spark.read.text("/Users/pavel/devcore/data/wiki/enwiki-20200401-pages-articles.xml")
    val df = spark.read
      .option("rowTag", "book")
      .xml(xmlFile)

    df.show(10)

    spark.close()
  }
}
