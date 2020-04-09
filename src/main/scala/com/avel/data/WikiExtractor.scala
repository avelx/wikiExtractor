package com.avel.data

object WikiExtractor {

  import org.apache.log4j.Logger
  import org.apache.spark.SparkConf
  import com.typesafe.config.ConfigFactory
  import org.apache.spark.sql.SparkSession
  import com.databricks.spark.xml._
  import org.apache.spark.sql.functions.{col, udf}

  def main(args: Array[String]): Unit = {

    val logger = Logger.getLogger(this.getClass.getName)

    val appConfig = ConfigFactory.load()

    val source: String = appConfig.getString("dataSource.file")
    val dataStore = appConfig.getString("dataStore.file")

    logger.info(s"FILE NAME: $source")

    val conf = new SparkConf()
      .setMaster("local[*]")
      .setAppName("WikiPedia")

    val spark = SparkSession
      .builder()
      .appName("ArticleExtractor")
      .config(conf)
      .getOrCreate()

    try {
      spark.sparkContext
        .hadoopConfiguration.set("fs.file.impl", "org.apache.hadoop.fs.LocalFileSystem")

      spark.sparkContext
        .hadoopConfiguration.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem")

      //conf.set("fs.hdfs.impl", classOf[org.apache.hadoop.hdfs.DistributedFileSystem].getName)

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
        logger.error(ex.getStackTrace.mkString(" "))
        spark.close()
      }
    }

    spark.close()
  }
}
