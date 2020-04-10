package com.avel.data.spark

import java.util.Calendar
import com.typesafe.config.ConfigFactory
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

trait BaseSpark {

  // Disable logging
  import org.apache.log4j.Logger
  import org.apache.log4j.Level

  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)

  val logger = Logger.getLogger(this.getClass.getName)

  val appConfig = ConfigFactory.load()
  val source: String = appConfig.getString("dataSource.file")
  val dataStore = appConfig.getString("dataStore.file")
  val temp = appConfig.getString("spark.tempStore")

  val tempResult = appConfig.getString("dataStore.temp")

  val conf = new SparkConf()
    .setMaster("local[*]")
    .set("spark.local.dir", temp)
    .setAppName("WikiPedia")


  val spark = SparkSession
    .builder()
    .appName("ArticleExtractor")
    .config(conf)
    .getOrCreate()

  spark.sparkContext
    .hadoopConfiguration.set("fs.file.impl", "org.apache.hadoop.fs.LocalFileSystem")

  spark.sparkContext
    .hadoopConfiguration.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem")

  def exec(block : () => Unit ): Unit = {
    try {
      val start = Calendar.getInstance().getTime().getTime
      logger.info(s"Start time: $start")
      block()
      val end = Calendar.getInstance().getTime().getTime
      logger.info(s"End time: $end")
      logger.info(s"Execution time: ${(end - start)/1000}")
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
