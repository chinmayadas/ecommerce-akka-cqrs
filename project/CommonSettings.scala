import sbt.Keys._
import sbt.dsl._

object CommonSettings {

  lazy val commonSettings = Seq(
    organization := "com.nikhu.ecommerce",
    version := "0.1.0",
    scalaVersion := "2.11.8"
    // test in assembly := {}
  )

}