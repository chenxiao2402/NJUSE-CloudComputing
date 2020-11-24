package cc

import cc.stream.StreamFactory
import org.apache.spark.sql.SparkSession

//noinspection DuplicatedCode
object Main {
  val SPARK_MASTER_ADDRESS = "spark://172.19.241.172:7077"
  val PAPER_COUNT_DATA_DIR = "/data/paperCount"
  val OTHER_DATA_DIR = "/data/others"
  val CHECK_POINT_DIR = "/data/checkpoint"

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .appName("CCStreaming")
      .config("spark.cores.max", 4)
      .master(SPARK_MASTER_ADDRESS)
      .getOrCreate()

    if (args.length < 1) {
      print("Please define which stream to start!")
      sys.exit(1)
    }

    val targetStream = args(0)
    if (targetStream == "otherTwo") {
      val paperData = source.JsonSource.createData(spark, OTHER_DATA_DIR)

      val authorCitationStream =
        StreamFactory.createAuthorCitationStream(
          paperData,
          s"$CHECK_POINT_DIR/authorCitations"
        )

      val paperCitationStream = StreamFactory.createPaperCitationStream(
        paperData,
        s"$CHECK_POINT_DIR/subjectPaperCitations"
      )

      authorCitationStream.start()
      paperCitationStream.start()
    } else if (targetStream == "paperCount") {
      val paperData = source.JsonSource.createData(spark, PAPER_COUNT_DATA_DIR)

      val paperCountStream = StreamFactory.createPaperCountStream(
        paperData,
        s"$CHECK_POINT_DIR/paperCountBySubject"
      )

      paperCountStream.start()
    } else {
      print("Invalid stream!")
      sys.exit(1)
    }

    spark.streams.awaitAnyTermination()
  }
}
