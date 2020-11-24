package cc.sink

import java.sql.{Connection, DriverManager, PreparedStatement}

import org.apache.spark.sql.{ForeachWriter, Row}

class JDBCSink(preparedSql: String, setValue: (PreparedStatement, Row) => Unit)
    extends ForeachWriter[Row] {
  var connection: Connection = _
  var statement: PreparedStatement = _

  override def open(partitionId: Long, epochId: Long): Boolean = {
    import JDBCSink._
    Class.forName(JDBC_DRIVER_CLASS) // Load the driver
    connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD)
    statement = connection.prepareStatement(preparedSql)
    true
  }

  override def process(value: Row): Unit = {
    setValue(statement, value)
    statement.executeUpdate()
  }

  override def close(errorOrNull: Throwable): Unit = {
    connection.close()
  }
}

object JDBCSink {
  private val JDBC_DRIVER_CLASS = "com.mysql.cj.jdbc.Driver"
  private val JDBC_URL = "jdbc:mysql://hadoop01:3306/cc_data"
  private val USER = "hadoop"
  private val PASSWORD = "123456"
}
