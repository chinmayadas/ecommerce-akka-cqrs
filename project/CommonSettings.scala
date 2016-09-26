import sbt.Keys._

object CommonSettings {

  lazy val commonSettings = Seq(
    name := "ecommerce-akka-cqrs",
    organization := "com.nikhu.ecommerce",
    version := "0.1.0",
    scalaVersion := "2.11.8"
  )

}