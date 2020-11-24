package cc.stream

import org.apache.spark.sql.DataFrame

object StreamFactory {

  def createPaperCountStream(
      source: DataFrame,
      checkpointDir: String
  ): Stream = {
    new PaperCountStream(source, checkpointDir)
  }

  def createAuthorCitationStream(
      source: DataFrame,
      checkpointDir: String
  ): Stream = {
    new AuthorCitationStream(source, checkpointDir)
  }

  def createPaperCitationStream(
      source: DataFrame,
      checkpointDir: String
  ): Stream = {
    new PaperCitationStream(source, checkpointDir)
  }
}
