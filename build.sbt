import CommonSettings._

lazy val `ecommerce-akka-cqrs-commons` = project.in(file("ecommerce-akka-cqrs-commons"))
lazy val `ecommerce-akka-cqrs-contracts` = project.in(file("ecommerce-akka-cqrs-contracts"))
lazy val `ecommerce-akka-cqrs-backend` = project.in(file("ecommerce-akka-cqrs-backend"))
lazy val `ecommerce-akka-cqrs-rest-frontend` = project.in(file("ecommerce-akka-cqrs-rest-frontend"))

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  aggregate(`ecommerce-akka-cqrs-commons`, `ecommerce-akka-cqrs-contracts`, `ecommerce-akka-cqrs-backend`, `ecommerce-akka-cqrs-rest-frontend`)
