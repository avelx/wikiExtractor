package com.avel.data

import com.avel.data.spark.BaseSpark

class WikiAgg extends BaseSpark {

  def job(): Unit = {
    try {
      logger.info("Read from DataStore")

      import spark.implicits._

      val df = spark
        .read
        .parquet(dataStore)

      val filteredDf = df.filter( $"revision.text._VALUE".contains("SomeCriteria") )

      logger.info("Persist result")
      filteredDf
        .coalesce(1)
        .write
          .json(s"$tempResult/data.json")

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

object WikiAgg {

  def main(args: Array[String]): Unit = {
    new WikiAgg() {
      self =>
      exec(job)
    }
  }
}
