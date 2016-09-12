logLevel := Level.Warn

resolvers ++=
  Seq(
    "Job Server Bintray" at "https://dl.bintray.com/spark-jobserver/maven",
    "Twitter Maven repo" at "http://maven.twttr.com/",
    "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/",
      "Artima Maven Repository" at "http://repo.artima.com/releases")

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.3")

//addSbtPlugin("com.artima.supersafe" % "sbtplugin" % "1.1.0")
