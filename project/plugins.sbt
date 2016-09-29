logLevel := Level.Warn

// sbt-revolver is a plugin for SBT enabling a super-fast development turnaround for your Scala applications.
// It sports the following features:
//   - Starting and stopping your application in the background of your interactive SBT shell (in a forked JVM)
//  - Triggered restart: automatically restart your application as soon as some of its sources have been changed

addSbtPlugin("io.spray" % "sbt-revolver" % "0.8.0")

// sbt-assembly is a sbt plugin was inspired by Maven's assembly plugin. The goal is simple: Create a fat JAR of your project with all of its dependencies.

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.3")

// SBT native packager lets you build application packages in native formats. It offers different archetypes for common configurations, such as simple Java apps or server applications.

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.1.1")

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.6.0")