package cc.source

import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame, SparkSession}

object JsonSource {
  val schema: StructType = StructType(
    Array(
      StructField(
        "authors",
        ArrayType(StringType)
      ),
      StructField("citation", IntegerType),
      StructField("month", IntegerType),
      StructField("subjects", ArrayType(StringType)),
      StructField("title", StringType),
      StructField("year", IntegerType)
    )
  )

  def createData(spark: SparkSession, dir: String): DataFrame = {
    spark.readStream
      .schema(schema)
      .json(dir)
  }
}
