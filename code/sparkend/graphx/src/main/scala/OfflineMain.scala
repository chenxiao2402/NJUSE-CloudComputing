import org.apache.spark.sql.SparkSession

object OfflineMain {
  /**
   * 用于计算离线数据
   */
  def main(args: Array[String]) {
    val sparkSession = SparkSession.builder.appName("CCGraphX-Offline")
      .config("spark.cores.max", 4)
      .getOrCreate()
    sparkSession.sparkContext.setLogLevel("WARN") // 屏蔽INFO级日志
    for (year <- Range.apply(2016, 2010, -1)) {
      CrossoverDegreeCalculator.calculate(sparkSession, year)
      AuthorConnectionsCalculator.calculate(sparkSession, year)
    }
    sparkSession.stop()
  }
}
