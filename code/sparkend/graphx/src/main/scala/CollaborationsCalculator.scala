import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.{col, desc}
import org.graphframes._

import scala.collection.mutable

object CollaborationsCalculator {
  def calculate(sparkSession: SparkSession, startYear: Int, subject: String, author1: String, author2: String): Unit = {
    // 读文件
    val df = Utils.loadData(sparkSession, startYear)
    // 获取点集，统计出所有热门作者作为点并编号
    val subDf = df.filter(row => row.getAs[Seq[String]]("subjects").contains(subject)).cache()
    val vertices = GraphHelper.getVertices(subDf, "author")
      .orderBy(desc("paper_count"))
      .limit(100)
      .cache()
    // 获取边集，注意这里是有向边的集合，原本一条无向边在这由两条有向边代替
    var edges = GraphHelper.getEdges(subDf, vertices, "author", 2, "src", "dst")
      .cache()
    val result = mutable.ListBuffer[(Int, String, String, String, String)]()
    // 处理直连边
    val straightEdge = edges.filter(col("author1") === author1 && col("author2") === author2)
    if (!straightEdge.isEmpty) {
      result.append((startYear, subject, author1, author2, author1 + "," + author2))
      edges = edges.filter(col("author1") =!= author1 || col("author2") =!= author2)
        .filter(col("author1") =!= author2 || col("author2") =!= author1)
        .cache()
    }
    var n = 0
    // 生成图
    var graphFrame = GraphFrame(vertices, edges).cache()
    do {
      val paths = graphFrame.bfs
        .fromExpr(col("author") === author1)
        .toExpr(col("author") === author2)
        .maxPathLength(5)
        .run()
        .cache()
      if (paths.isEmpty) {
        n = 3
      } else {
        val head = paths.columns
        if (head.contains("v3")) {
          paths.foreach(row => {
            val path = List(author1,
              row.getStruct(2).getAs[String]("author"),
              row.getStruct(4).getAs[String]("author"),
              row.getStruct(6).getAs[String]("author"),
              author2
            )
            result.append((startYear, subject, author1, author2, path.mkString(",")))
          })
          val idList = paths.select("v1.id", "v2.id", "v3.id").collect()
            .flatMap(row => List(row.getAs[Long](0), row.getAs[Long](1), row.getAs[Long](2)))
          graphFrame = graphFrame.filterVertices(!col("id").isin(idList:_*)).cache()
          n = 3
        } else if (head.contains("v2")) {
          paths.foreach(row => {
            val path = List(author1,
              row.getStruct(2).getAs[String]("author"),
              row.getStruct(4).getAs[String]("author"),
              author2
            )
            result.append((startYear, subject, author1, author2, path.mkString(",")))
          })
          val idList = paths.select("v1.id", "v2.id").collect()
            .flatMap(row => List(row.getAs[Long](0), row.getAs[Long](1)))
          graphFrame = graphFrame.filterVertices(!col("id").isin(idList:_*)).cache()
          n = 2
        } else if (head.contains("v1")) {
          paths.foreach(row => {
            val path = List(author1,
              row.getStruct(2).getAs[String]("author"),
              author2
            )
            result.append((startYear, subject, author1, author2, path.mkString(",")))
          })
          val idList = paths.select("v1.id").collect().map(row => row.getAs[Long](0))
          graphFrame = graphFrame.filterVertices(!col("id").isin(idList:_*)).cache()
          n = 1
        } else {
          paths.printSchema()
          paths.show()
          n = 3
        }
      }
    } while (n < 3)
    if (result.isEmpty) {
      result.append((startYear, subject, author1, author2, ""))
    }
    // Save the result
    val resultRdd = sparkSession.sparkContext.parallelize(result)
    val collaborations = sparkSession.createDataFrame(resultRdd)
      .select(
        col("_1").alias("start_year"),
        col("_2").alias("subject"),
        col("_3").alias("source_author"),
        col("_4").alias("target_author"),
        col("_5").alias("path"))
    MysqlHelper.writeToMysql(collaborations, "collaboration_path")
  }
}
