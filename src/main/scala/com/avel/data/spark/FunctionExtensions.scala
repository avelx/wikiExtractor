package com.avel.data.spark

object FunctionExtensions {
  import org.apache.spark.sql.functions._

  val stringify = udf((vs: Seq[Long]) => vs match {

    case null => null

    case _    => s"""[${vs.mkString(",")}]"""

  })
}
