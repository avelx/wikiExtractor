package com.avel.data

import com.avel.data.spark.BaseSpark

import scala.util.Random

class WikiAgg extends BaseSpark {

  def job(): Unit = {
    try {
      logger.info("Read from DataStore")

      import spark.implicits._

      val df = spark
        .read
        .parquet(dataStore)

      val criteria = appConfig.getString("search.query")

      val filteredDf = df.filter( $"revision.text._VALUE".contains(criteria) )

      logger.info("Persist result")
      val stamp = Random.alphanumeric.take(10).mkString("")


      filteredDf
        .coalesce(1)
        .write
          .json(s"$tempResult/data-$stamp.json")

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
