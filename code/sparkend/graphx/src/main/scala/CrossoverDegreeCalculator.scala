import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object CrossoverDegreeCalculator {
  def calculate(sparkSession: SparkSession, startYear: Long): Unit = {
    // 读文件
    val df = Utils.loadData(sparkSession, startYear).cache()
    // 获取点集，统计出所有领域作为点并编号
    val vertices = GraphHelper.getVertices(df, "subject").cache()
    // 获取边集，注意这里是有向边的集合，原本一条无向边在这由两条有向边代替
    val edges = GraphHelper.getEdges(df, vertices, "subject", 0).cache()
    // 生成图
    val graph = GraphHelper.loadGraph(vertices, edges)
    // PageRank
    val ranks = graph.pageRank(0.01).vertices // 官方样例为0.0001
    // Get relative_subjects
    val relativeSubjects = edges.select(
      lit(startYear).alias("start_year"),
      col("subject1"),
      col("subject2"),
      col("count").alias("paper_count"))
    // Save the result
    val resultRdd = vertices.rdd.map(row => (row.getLong(0), row.getString(1)))
      .join(ranks)
      .map {
        case (_, (name, result)) => (name, result)
      }
    val subjectCrossoverRank = sparkSession.createDataFrame(resultRdd)
      .withColumn("start_year", lit(startYear))
      .select(
        col("start_year"),
        col("_1").alias("subject"),
        col("_2").alias("crossover_rank"))
    MysqlHelper.writeToMysql(subjectCrossoverRank, "subject_crossover_rank")
    MysqlHelper.writeToMysql(relativeSubjects, "relative_subjects")
  }
}
