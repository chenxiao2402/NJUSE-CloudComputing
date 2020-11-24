import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions.col

object Utils {
  def loadData(sparkSession: SparkSession, startYear: Long): DataFrame = {
    sparkSession.read.json("/data/paper/arxiv_final.json")
      .filter(col("year") >= startYear)
  }

  def loadAllSubjects(): List[String] = {
    List("Numerical Analysis",
      "Computers and Society",
      "Networking and Internet Architecture",
      "Computational Engineering, Finance, and Science",
      "Information Theory",
      "Neural and Evolutionary Computation",
      "Operating Systems",
      "Machine Learning",
      "Computational Complexity",
      "Sound",
      "Databases",
      "Distributed, Parallel, and Cluster Computing",
      "Discrete Mathematics",
      "Logic in Computer Science",
      "Formal Languages and Automata Theory",
      "Symbolic Computation",
      "Other",
      "Multimedia",
      "Programming Languages",
      "Computer Vision and Pattern Recognition",
      "Graphics",
      "Mathematical Software",
      "Social and Information Networks",
      "Software Engineering",
      "Information Retrieval",
      "Emerging Technologies",
      "Digital Libraries",
      "Cryptography and Security",
      "Artificial Intelligence",
      "Robotics",
      "Hardware Architecture",
      "Computer Science and Game Theory",
      "Computation and Language (Computational Linguistics and Natural Language and Speech Processing)",
      "Data Structures and Algorithms",
      "General Literature",
      "Performance",
      "Computational Geometry",
      "Systems and Control",
      "Multiagent Systems",
      "Human-Computer Interaction",
    )
  }
}
