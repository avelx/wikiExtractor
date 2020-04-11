package com.avel.data

import com.avel.data.spark.BaseSpark
import org.apache.avro.generic.GenericData.StringType

import scala.util.Random

class WikiGroupBy extends BaseSpark {

  import com.avel.data.spark.FunctionExtensions._
  import org.apache.spark.sql.functions._

  def job(): Unit = {
    val criteria = appConfig.getString("groupby.col")
    logger.info(s"Criteria: $criteria")

    val df = spark
      .read
      .parquet(dataStore)


    val filteredDf = df
      .groupBy(criteria)
      .count()
      .sort(criteria)
      .withColumn("count", col("count").cast("string") )
        //.withColumn("count", stringify( col("count") ))

    filteredDf.printSchema()


    logger.info("Persist result")
    val stamp = Random.alphanumeric.take(5).mkString("")

    filteredDf
      .coalesce(1)
      .write
      .option("header", "true")
      .csv(s"$tempResult/data-groupby-$stamp.csv")
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


