package cc.stream

import java.sql.PreparedStatement

import cc.sink.MysqlSinkDriver
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, Row}

class PaperCountStream(source: DataFrame, checkpointDir: String)
    extends Stream(
      source,
      checkpointDir
    ) {

  override protected def transformSource(data: DataFrame): DataFrame = {
    data
      .withColumn("subject", explode(col("subjects")))
      .select(
        col("year"),
        col("month"),
        col("subject"),
        size(col("authors")).alias("author_count")
      )
      .groupBy("year", "month", "subject")
      .agg(count(lit(1)).alias("paper_count"), sum("author_count").alias("author_count"))
  }

  override protected def setSink(
      data: DataFrame,
      checkpointDir: String
  ): MysqlSinkDriver = {
    import PaperCountStream._
    new MysqlSinkDriver(
      data,
      checkpointDir,
      preparedSql,
      settingSql
    )
  }
}

object PaperCountStream {
  private val preparedSql = "insert into subject_paper_count " +
    "(year, month, subject, paper_count, author_count) " +
    "values (?, ?, ?, ?, ?) " +
    "ON DUPLICATE KEY UPDATE paper_count = ?, author_count = ?;"

  //noinspection DuplicatedCode
  private def settingSql(statement: PreparedStatement, row: Row): Unit = {
    statement.setInt(1, row.getAs[Int]("year"))
    statement.setInt(2, row.getAs[Int]("month"))
    statement.setString(3, row.getAs[String]("subject"))
    statement.setInt(4, row.getAs[Long]("paper_count").toInt)
    statement.setInt(5, row.getAs[Long]("author_count").toInt)
    statement.setInt(6, row.getAs[Long]("paper_count").toInt)
    statement.setInt(7, row.getAs[Long]("author_count").toInt)
  }
}
