import sbt._

object Dependencies {
  // Versions
  lazy val akkaVersion = "2.4.10"

  // Libraries 
  val akkaActor = "com.typesafe.akka" %% "akka-actor" % akkaVersion
  val akkaStream = "com.typesafe.akka" %% "akka-stream" % akkaVersion
  val akkaPersistence = "com.typesafe.akka" %% "akka-persistence" % akkaVersion
  val akkaContrib = "com.typesafe.akka" %% "akka-contrib" % akkaVersion
  val akkaRemote = "com.typesafe.akka" %% "akka-remote" % akkaVersion
  val akkaCluster = "com.typesafe.akka" %% "akka-cluster" % akkaVersion

  val specs2core = "org.specs2" %% "specs2-core" % "3.8.5"

  // Projects
  val commonsDeps = Seq(akkaActor, akkaStream, akkaPersistence, akkaContrib, akkaRemote, specs2core % Test)

  val backendDeps = Seq(akkaCluster)
}