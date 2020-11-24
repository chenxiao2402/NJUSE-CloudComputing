package cc.graph

import org.apache.spark.sql.{DataFrame, SaveMode}

class MysqlWriter {

  def writeToMysql(dataFrame: DataFrame, tableName: String): Unit = {
    dataFrame.write
      .format("jdbc")
      .mode(SaveMode.Overwrite)
      .option("driver", "com.mysql.cj.jdbc.Driver")
      .option("url", "jdbc:mysql://hadoop01:3306/cc_data")
      .option("user", "hadoop")
      .option("password", "123456")
      .option("dbtable", tableName)
      .save()
  }
}
