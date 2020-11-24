package cc.stream

import java.sql.PreparedStatement

import cc.sink.MysqlSinkDriver
import org.apache.spark.sql.functions.{col, explode, sum}
import org.apache.spark.sql.{DataFrame, Row}

class AuthorCitationStream(source: DataFrame, checkpointDir: String)
    extends Stream(
      source,
      checkpointDir
    ) {

  override protected def transformSource(data: DataFrame): DataFrame = {
    data
      .filter(col("citation") > 0)
      .withColumn("author", explode(col("authors")))
      .withColumn("subject", explode(col("subjects")))
      .select(
        col("year"),
        col("author"),
        col("subject"),
        col("citation")
      )
      .groupBy("year", "author", "subject")
      .agg(sum("citation").alias("totalCitations"))
      .filter(col("totalCitations") > 10)
  }

  override protected def setSink(
      data: DataFrame,
      checkpointDir: String
  ): MysqlSinkDriver = {
    import AuthorCitationStream._
    new MysqlSinkDriver(
      data,
      checkpointDir,
      preparedSql,
      settingSql
    )
  }
}

object AuthorCitationStream {
  private val preparedSql = "insert into author_citations " +
    "(year, author, subject, citations) " +
    "values (?, ?, ?, ?) " +
    "ON DUPLICATE KEY UPDATE citations = ?;"

  //noinspection DuplicatedCode
  private def settingSql(statement: PreparedStatement, row: Row): Unit = {
    statement.setInt(1, row.getAs[Int]("year"))
    statement.setString(2, row.getAs[String]("author"))
    statement.setString(3, row.getAs[String]("subject"))
    statement.setInt(4, row.getAs[Long]("totalCitations").toInt)
    statement.setInt(5, row.getAs[Long]("totalCitations").toInt)
  }
}
