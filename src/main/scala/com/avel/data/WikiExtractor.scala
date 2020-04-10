package com.avel.data

import com.databricks.spark.xml._
import com.avel.data.spark.BaseSpark

class WikiExtractor extends BaseSpark {

  def job() : Unit = {
    try {
      logger.info("Read XML")

      val df = spark.read
        .format("xml")
        .option("rowTag", "page")
        .xml(source)

      logger.info("Write to DataStore")

      // re-partition data
      df
        //.repartition( col("revision.timestamp") )
        .repartition(30)
        .write.parquet(dataStore)

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

object WikiExtractor {

  def main(args: Array[String]): Unit = {

    new WikiExtractor(){ self =>
      exec( job )
    }

  }

}
