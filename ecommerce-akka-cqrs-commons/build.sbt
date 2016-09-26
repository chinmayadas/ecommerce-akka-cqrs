import CommonSettings._
import Dependencies._

lazy val `ecommerce-akka-cqrs-commons` = project.in(file(".")).
  settings(commonSettings: _*).
  settings(
    libraryDependencies ++= commonsDeps
  )