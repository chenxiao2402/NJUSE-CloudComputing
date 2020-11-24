package cc.stream

import java.sql.PreparedStatement

import cc.sink.MysqlSinkDriver
import org.apache.spark.sql.functions.{col, explode, max}
import org.apache.spark.sql.{DataFrame, Row}

class PaperCitationStream(source: DataFrame, checkpointDir: String)
    extends Stream(
      source,
      checkpointDir
    ) {

  override protected def transformSource(data: DataFrame): DataFrame = {
    data
      .filter(col("citation") > 10)
      .withColumn("subject", explode(col("subjects")))
      .select(
        col("year"),
        col("month"),
        col("subject"),
        col("title"),
        col("citation")
      )
  }

  override protected def setSink(
      data: DataFrame,
      checkpointDir: String
  ): MysqlSinkDriver = {
    import PaperCitationStream._
    new MysqlSinkDriver(
      data,
      checkpointDir,
      preparedSql,
      settingSql
    )
  }
}

object PaperCitationStream {
  private val preparedSql = "insert into paper_citations " +
    "(year, month, subject, title, citations) " +
    "values (?, ?, ?, ?, ?) " +
    "ON DUPLICATE KEY UPDATE citations = ?;"

  //noinspection DuplicatedCode
  private def settingSql(statement: PreparedStatement, row: Row): Unit = {
    statement.setInt(1, row.getAs[Int]("year"))
    statement.setInt(2, row.getAs[Int]("month"))
    statement.setString(3, row.getAs[String]("subject"))
    statement.setString(4, row.getAs[String]("title"))
    statement.setInt(5, row.getAs[Int]("citation"))
    statement.setInt(6, row.getAs[Int]("citation"))
  }
}
