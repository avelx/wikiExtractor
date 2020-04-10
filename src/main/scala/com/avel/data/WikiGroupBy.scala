package com.avel.data

import com.avel.data.spark.BaseSpark

import scala.util.Random

class WikiGroupBy extends BaseSpark {

  def job(): Unit = {
    try {
      logger.info("Read from DataStore")

      import spark.implicits._

      val df = spark
        .read
        .parquet(dataStore)

      val filteredDf = df
          .groupBy("revision.format")
          .count()

      logger.info("Persist result")
      val stamp = Random.alphanumeric.take(5).mkString("")

      filteredDf
        .coalesce(1)
        .write
          .json(s"$tempResult/data-groupby-$stamp.json")

    } catch {
      case ex: Throwable => {
        logger.error(ex.getMessage)
        logger.error(ex.getCause.getMessage)
        logger.error(ex.fillInStackTrace())
        spark.close()
      }
    }
    spark.close()
  }
}

object WikiGroupBy {

  def main(args: Array[String]): Unit = {
    new WikiGroupBy() {
      self =>
      exec(job)
    }
  }
}


