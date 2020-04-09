package com.avel.data.spark

import com.typesafe.config.ConfigFactory
import org.apache.log4j.Logger
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

trait BaseSpark {

  val logger = Logger.getLogger(this.getClass.getName)

  val appConfig = ConfigFactory.load()
  val source: String = appConfig.getString("dataSource.file")
  val dataStore = appConfig.getString("dataStore.file")
  val tempStore = appConfig.getString("spark.tempStore")


  val conf = new SparkConf()
    .setMaster("local[*]")
    .set("spark.local.dir", tempStore)
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


}
