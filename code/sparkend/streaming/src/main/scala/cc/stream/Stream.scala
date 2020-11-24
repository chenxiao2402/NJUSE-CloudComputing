package cc.stream

import cc.sink.MysqlSinkDriver
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.streaming.StreamingQuery

abstract class Stream(
    source: DataFrame,
    checkpointDir: String
) {

  private var streamingQuery: StreamingQuery = _

  def start(): Unit = {
    val transformation = transformSource(source)
    val driver = setSink(transformation)
    streamingQuery = driver.start()
  }

  def stop(): Unit = {
    if (streamingQuery != null)
      streamingQuery.stop()
  }

  protected def transformSource(data: DataFrame): DataFrame

  protected def setSink(
      data: DataFrame,
      checkpointDir: String = checkpointDir
  ): MysqlSinkDriver

}
