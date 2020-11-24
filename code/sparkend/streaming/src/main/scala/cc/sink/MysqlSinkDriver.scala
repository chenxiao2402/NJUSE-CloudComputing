package cc.sink

import java.sql.PreparedStatement

import org.apache.spark.sql.streaming.StreamingQuery
import org.apache.spark.sql.{DataFrame, Row}

class MysqlSinkDriver(
    dataFrame: DataFrame,
    checkPointDir: String,
    preparedSql: String,
    settingSql: (PreparedStatement, Row) => Unit
) {

  def start(): StreamingQuery = {
    val writer = new JDBCSink(preparedSql, settingSql)
    dataFrame.writeStream
      .foreach(writer)
      .outputMode("update")
      .option("checkpointLocation", checkPointDir)
      .start()
  }
}
