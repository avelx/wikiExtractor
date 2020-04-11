package com.avel.data

import com.databricks.spark.xml._
import com.avel.data.spark.BaseSpark

class WikiExtractor extends BaseSpark {

  def job(): Unit = {
    logger.info("Read XML")

    val df = spark.read
      .format("xml")
      .option("rowTag", "page")
      .xml(source)

    df.createOrReplaceTempView("wiki")

    val sqlResult = spark.sql(
      "SELECT id, revision.timestamp as timestamp, " +
        " revision.text._VALUE as text FROM wiki")

    logger.info("Write to DataStore")

    // re-partition data
    sqlResult.toDF()
      .repartition(30)
      .write
      .csv(s"$dataStore.json")
  }

}

object WikiExtractor {

  def main(args: Array[String]): Unit = {

    new WikiExtractor() {
      self =>
      exec(job)
    }

  }

}
