name := "CCGraphx"

version := "0.1"

scalaVersion := "2.12.12"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "3.0.1" % "provided",
  "org.apache.spark" %% "spark-sql" % "3.0.1" % "provided",
  "org.apache.spark" %% "spark-graphx" % "3.0.1" % "provided",
  "mysql" % "mysql-connector-java" % "8.0.22",
)

assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)