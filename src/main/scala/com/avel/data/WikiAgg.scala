package com.avel.data

import com.avel.data.spark.BaseSpark
import scala.util.Random

class WikiAgg extends BaseSpark {

  import spark.implicits._

  def job(): Unit = {
    val criteria = appConfig.getString("search.query")

    val df = spark
      .read
      .parquet(dataStore)

    val filteredDf = df.filter($"revision.text._VALUE".contains(criteria))

    logger.info("Persist result")
    val stamp = Random.alphanumeric.take(10).mkString("")

    filteredDf
      .coalesce(1)
      .write
      .json(s"$tempResult/data-$stamp.json")
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
