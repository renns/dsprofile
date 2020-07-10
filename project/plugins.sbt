addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.1.1")

val scalaJSVersionOpt = Option(System.getProperty("scalaJSVersion"))
addSbtPlugin("org.scala-js" % "sbt-scalajs" % scalaJSVersionOpt.getOrElse("1.1.1"))
