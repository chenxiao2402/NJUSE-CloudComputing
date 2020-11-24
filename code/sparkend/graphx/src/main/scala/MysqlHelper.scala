import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

object MysqlHelper {
  def writeToMysql(dataFrame: DataFrame, tableName: String): Unit = {
    dataFrame.write
      .mode(SaveMode.Append)
      .format("jdbc")
      .option("driver", "com.mysql.cj.jdbc.Driver")
      .option("url", "jdbc:mysql://hadoop01:3306/cc_data")
      .option("user", "hadoop")
      .option("password", "123456")
      .option("dbtable", tableName)
      .save()
  }

  def readFromMysql(sparkSession: SparkSession, table: String): DataFrame = {
    val url = "jdbc:mysql://hadoop01:3306/cc_data"
    val prop = new java.util.Properties()
    prop.put("driver", "com.mysql.jdbc.Driver")
    prop.put("user", "hadoop")
    prop.put("password", "123456")
    sparkSession.read.jdbc(url, table, prop)
  }
}
