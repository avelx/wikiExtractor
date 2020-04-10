package com.avel.data

import com.avel.data.spark.BaseSpark

import scala.util.Random

class WikiGroupBy extends BaseSpark {

  def job() : Unit = {
    val criteria = appConfig.getString("groupby.col")
    val df = spark
      .read
      .parquet(dataStore)

    val filteredDf = df
      .groupBy(criteria)
      .count()

    logger.info("Persist result")
    val stamp = Random.alphanumeric.take(5).mkString("")

    filteredDf
      .coalesce(1)
      .write
      .json(s"$tempResult/data-groupby-$stamp.json")
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


