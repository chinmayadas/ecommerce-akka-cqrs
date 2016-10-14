import sbt._

object Dependencies {
  // Versions
  lazy val akkaVersion = "2.4.10"
  val scalaTestVersion = "3.0.0"
  val log4jVersion = "2.6.2"
  val scaldiV = "0.5.7"

  // Libraries 
  val akkaActor = "com.typesafe.akka" %% "akka-actor" % akkaVersion
  val akkaStream = "com.typesafe.akka" %% "akka-stream" % akkaVersion
  val akkaPersistence = "com.typesafe.akka" %% "akka-persistence" % akkaVersion
  val akkaContrib = "com.typesafe.akka" %% "akka-contrib" % akkaVersion
  val akkaRemote = "com.typesafe.akka" %% "akka-remote" % akkaVersion
  val akkaCluster = "com.typesafe.akka" %% "akka-cluster" % akkaVersion
  val akkaClusterShrading = "com.typesafe.akka" %% "akka-cluster-sharding" % akkaVersion

  val scalDi = "org.scaldi" %% "scaldi-akka" % scaldiV

  val specs2core = "org.specs2" %% "specs2-core" % "3.8.5"

  val akkaHttp = "com.typesafe.akka" %% "akka-http-experimental" % akkaVersion
  val akkaSprayJson = "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaVersion
  val akkaHttpTestkit = "com.typesafe.akka" %% "akka-http-testkit" % akkaVersion

  // Logging dependencies
  val slf4j = "org.slf4j" % "slf4j-api" % "1.7.21"
  val scalaLogging = "com.typesafe.scala-logging" % "scala-logging_2.11" % "3.5.0"
  val akkaSlf4j = "com.typesafe.akka" % "akka-slf4j_2.11" % akkaVersion
  val logBack = "ch.qos.logback" % "logback-classic" % "1.1.7"

  val scalaTest = "org.scalatest" %% "scalatest" % scalaTestVersion % "test"

  // level db for local persistence
  val levelDb = "org.iq80.leveldb" % "leveldb" % "0.9"
  val levelDbJniAll = "org.fusesource.leveldbjni" % "leveldbjni-all" % "1.8"

  // Projects
  val commonsDeps = Seq(akkaActor, akkaStream, akkaPersistence, akkaContrib, akkaRemote, specs2core % Test)

  val backendDeps = Seq(akkaCluster, scalDi, akkaClusterShrading, levelDb, levelDbJniAll, slf4j, scalaLogging, akkaSlf4j, logBack)

  val frontendDeps = Seq(akkaHttp, scalDi, akkaClusterShrading, akkaSprayJson, akkaHttpTestkit, slf4j, scalaLogging, akkaSlf4j, logBack, scalaTest)
}