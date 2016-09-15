import sbt._
import Keys._
import java.io.File
import sbtassembly._
import sbtassembly.AssemblyKeys._

object Build extends Build {
  override def settings = super.settings ++ Seq(
    javacOptions ++= Seq("-source", "1.8", "-target", "1.8"),
    scalaVersion := "2.11.7"
  )

  lazy val core =
    Project("bioinformatics-core", new File("core")).settings(
      libraryDependencies ++= Seq(
        "org.scalactic" %% "scalactic" % "3.0.0",
        "org.scalatest" %% "scalatest" % "3.0.0" % "test"
      )
    )

  lazy val web =
    Project("bioinformatics-web", new File("web"))
      .settings(
        libraryDependencies ++= Seq(
          "com.twitter" %% "finatra-http" % "2.4.0",
          spark("core")
        )
      )
      .dependsOn(core % "test->test;compile->compile")

  lazy val populator =
    Project("bioinformatics-populator", new File("populator"))
      .settings(
        fork := true,
        runMain in Compile <<= Defaults.runMainTask(fullClasspath in Compile, runner in (Compile, run)),
        libraryDependencies ++= Seq(
          spark("core"),
          spark("mllib"),
          "com.databricks" %% "spark-csv" % "1.4.0",
          "com.github.tototoshi" %% "scala-csv" % "1.3.3"
        ),
        test in assembly := {},
        dependencyOverrides ++= Set(
          "com.fasterxml.jackson.core" % "jackson-databind" % "2.4.4"
        ),
        assemblyMergeStrategy in assembly := {
          case PathList("javax", "servlet", xs @ _*) => MergeStrategy.last
          case PathList("javax", "activation", xs @ _*) => MergeStrategy.last
          case PathList("com", "esotericsoftware", xs @ _*) => MergeStrategy.last
          case PathList("org", "apache", "commons", "collections", xs @ _*) => MergeStrategy.last
          case PathList("org", "apache", "commons", "beanutils", xs @ _*) => MergeStrategy.last
          case x =>
            val oldStrategy = (assemblyMergeStrategy in assembly).value
            oldStrategy(x)
        }
      )
      .dependsOn(core % "test->test;compile->compile")

  val sparkVersion = "2.0.0"
  def spark(pkg: String): ModuleID = {
    "org.apache.spark" %% s"spark-$pkg" % sparkVersion % "provided"
  }
}
