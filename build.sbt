name := "insidershomework"

version := "1.0"

lazy val `insidershomework` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.2"

libraryDependencies ++= Seq(
  jdbc,
  ehcache,
  ws,
  specs2 % Test,
  guice,
  "org.reactivemongo" %% "play2-reactivemongo" % "0.15.0-play26")

unmanagedResourceDirectories in Test <+= baseDirectory(_ / "target/web/public/test")

      