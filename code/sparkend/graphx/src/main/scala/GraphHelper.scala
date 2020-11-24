import org.apache.spark.graphx._
import org.apache.spark.sql._
import org.apache.spark.sql.functions._

object GraphHelper {
  def getVertices(df: DataFrame, colName: String): DataFrame = {
    df.withColumn(colName, explode(col(colName + 's')))
      .select(colName)
      .groupBy(colName)
      .count()
      .withColumn("id", monotonically_increasing_id())
      .select(col("id"), col(colName), col("count").alias("paper_count"))
  }

  def getEdges(df: DataFrame, vertices: DataFrame, colName: String, minCount: Int): DataFrame = {
    getEdges(df, vertices, colName, minCount, "idx_1", "idx_2")
  }

  def getEdges(df: DataFrame, vertices: DataFrame, colName: String, minCount: Int, idColName1: String,
               idColName2: String): DataFrame = {
    val colName1 = colName + '1'
    val colName2 = colName + '2'
    // 统计出指定列间的边的值：出现在同一论文中的次数
    val exploded = df.withColumn(colName1, explode(col(colName + 's')))
      .select(col("title"), col(colName1))
      .cache()
    val tmp1 = vertices.join(exploded, vertices(colName) === exploded(colName1))
      .select(col(colName1), col("title"))
    val tmp2 = vertices.join(exploded, vertices(colName) === exploded(colName1))
      .select(col(colName).as(colName2), col("title"))
    var counts = tmp1.join(tmp2, tmp1("title") === tmp2("title") && tmp1(colName1) =!= tmp2(colName2))
      .groupBy(colName1, colName2)
      .count()
    if (minCount > 0) {
      counts = counts.filter(col("count") > minCount)
    }
    counts.join(vertices, vertices(colName) === counts(colName1))
      .selectExpr(colName1, colName2, "count", "id as " + idColName1)
      .join(vertices, vertices(colName) === counts(colName2))
      .selectExpr(colName1, colName2, "count", idColName1, "id as " + idColName2)
  }

  def loadGraph(vertices: DataFrame, edges: DataFrame): Graph[Long, Long] = {
    val points = vertices.rdd.map(row => (row.getLong(0), row.getLong(2)))
    val edgesRdd = edges
      .rdd
      .map(row => {
        Edge[Long](row.getLong(3), row.getLong(4), row.getLong(2))
      })
    // 建图
    Graph(points, edgesRdd)
  }
}
