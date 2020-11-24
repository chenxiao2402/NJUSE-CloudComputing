import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object AuthorConnectionsCalculator {
  def calculate(sparkSession: SparkSession, startYear: Long): Unit = {
    // 读文件
    val df = Utils.loadData(sparkSession, startYear)
    // 获取所有领域
    val subjects = Utils.loadAllSubjects()
    // 每个Subject计算一次
    for (subjectName <- subjects) {
      // 获取点集，统计出所有热门作者作为点并编号
      val subDf = df.filter(row => row.getAs[Seq[String]]("subjects").contains(subjectName))
      val vertices = GraphHelper.getVertices(subDf, "author")
        .orderBy(desc("paper_count"))
        .limit(100)
        .cache()
      // 获取边集，注意这里是有向边的集合，原本一条无向边在这由两条有向边代替
      val edges = GraphHelper.getEdges(subDf, vertices, "author", 0, "source_author", "target_author")
        .cache()
      val smallEdges = edges.filter(col("count") > 2)
      // 生成图
      val graph = GraphHelper.loadGraph(vertices, smallEdges)
      // Find the connected components
      val cc = graph.connectedComponents().vertices
      // Join the connected components with the names
      val resultRdd = vertices.rdd
        .map(row => {
          if (row == null || row.isNullAt(0) || row.isNullAt(1)) {
            (-1L, "null")
          } else {
            (row.getAs[Long]("id"), row.getAs[String]("author"))
          }
        })
        .join(cc)
        .map {
          case (id, (name, result)) => (id, name, result)
        }
      val authorConnectionsAuthor = sparkSession.createDataFrame(resultRdd)
        .join(vertices, col("_1") === col("id"))
        .withColumn("start_year", lit(startYear))
        .withColumn("subject", lit(subjectName))
        .select(
          col("start_year"),
          col("subject"),
          col("_1").alias("author_id"),
          col("_2").alias("author_name"),
          col("paper_count"),
          col("_3").alias("author_category"))
      MysqlHelper.writeToMysql(authorConnectionsAuthor, "author_connections_author")
      val collaborations = edges
        .filter(col("source_author") < col("target_author"))
        .withColumn("start_year", lit(startYear))
        .withColumn("subject", lit(subjectName))
        .select(
          col("start_year"),
          col("subject"),
          col("source_author"),
          col("target_author"))
        .withColumn("length", lit(1))
      MysqlHelper.writeToMysql(collaborations, "collaborations")
    }
  }
}
