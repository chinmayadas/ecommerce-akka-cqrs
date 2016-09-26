import CommonSettings._

lazy val `ecommerce-akka-cqrs-rest-frontend` = project.in(file(".")).
  settings(commonSettings: _*).
  settings(
    // other settings
  )
  .dependsOn("ecommerce-akka-cqrs-contracts")