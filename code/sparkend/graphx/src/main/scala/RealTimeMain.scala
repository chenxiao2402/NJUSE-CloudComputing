import org.apache.spark.sql.SparkSession

object RealTimeMain {
  /**
   * 用于计算实时数据
   *
   * @param args [
   *             startYear: Int,
   *             subject: String,
   *             author1: String,
   *             author2: String
   *             ]
   */
  def main(args: Array[String]) {
    val sparkSession = SparkSession.builder.appName("CCGraphX-RealTime")
      .config("spark.cores.max", 4)
      .getOrCreate()
    sparkSession.sparkContext.setLogLevel("WARN") // 屏蔽INFO级日志
    CollaborationsCalculator.calculate(sparkSession, args(0).toInt, args(1), args(2), args(3))
    sparkSession.stop()
  }
}