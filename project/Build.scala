import CommonSettings._
import sbt._
import Keys._


object EcommerceAkkaCQRSBuild extends Build {

  lazy val root = Project(id = "ecommerce-akka-cqrs",
    base = file("."))
    .aggregate(`ecommerce-akka-cqrs-commons`, `ecommerce-akka-cqrs-contracts`, `ecommerce-akka-cqrs-backend`, `ecommerce-akka-cqrs-rest-frontend`)

  lazy val `ecommerce-akka-cqrs-commons` = Project(id = "ecommerce-akka-cqrs-commons",
    base = file("ecommerce-akka-cqrs-commons")).
    settings(commonSettings: _*)

  lazy val `ecommerce-akka-cqrs-contracts` = Project(id = "ecommerce-akka-cqrs-contracts",
    base = file("ecommerce-akka-cqrs-contracts")).
    settings(commonSettings: _*).dependsOn(`ecommerce-akka-cqrs-commons`)

  lazy val `ecommerce-akka-cqrs-backend` = Project(id = "ecommerce-akka-cqrs-backend",
    base = file("ecommerce-akka-cqrs-backend")).
    settings(commonSettings: _*).dependsOn(`ecommerce-akka-cqrs-contracts`)

  lazy val `ecommerce-akka-cqrs-rest-frontend` = Project(id = "ecommerce-akka-cqrs-rest-frontend",
    base = file("ecommerce-akka-cqrs-rest-frontend")).
    settings(commonSettings: _*).dependsOn(`ecommerce-akka-cqrs-contracts`)

}