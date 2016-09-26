import CommonSettings._
import Dependencies._

lazy val `ecommerce-akka-cqrs-backend` = project.in(file(".")).
  settings(commonSettings: _*).
  settings(
    libraryDependencies ++= backendDeps
  )
  .dependsOn("ecommerce-akka-cqrs-contracts", "ecommerce-akka-cqrs-commons")