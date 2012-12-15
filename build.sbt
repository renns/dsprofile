// Main settings

name := "dsprofile"

version := "0.1.0"

organization := "org.bitbucket.inkytonik.dsprofile"

// Scala compiler settings

scalaVersion := "2.10.0-RC5"

scalaBinaryVersion <<= scalaVersion

crossScalaVersions := Seq ("2.9.2", "2.10.0-RC5")

scalacOptions := Seq ("-deprecation", "-unchecked")

scalacOptions in Compile <<= (scalaVersion, scalacOptions) map {
    (version, options) =>
        val versionOptions =
            if (version.startsWith ("2.10"))
                Seq ("-feature")
            else
                Seq ()
        options ++ versionOptions
}

scalacOptions in Test <<= (scalaVersion, scalacOptions) map {
    (version, options) =>
        val versionOptions =
            if (version.startsWith ("2.10"))
                Seq ("-feature")
            else
                Seq ()
        options ++ versionOptions
}

// Migration manager (mima)

// mimaDefaultSettings

// previousArtifact <<= (name, organization) { (n, o) =>
//     Some (o % (n + "_2.9.2") % "0.1.0")
// }

// Interactive settings

logLevel := Level.Info

shellPrompt <<= (name, version) { (n, v) =>
     _ => n + " " + v + "> "
}

// Dependencies

libraryDependencies <++= scalaVersion {
    version =>
        Seq (
            if (version.startsWith ("2.10"))
                "org.scalatest" %% "scalatest" % "2.0.M5-B1" % "test"
            else
                "org.scalatest" %% "scalatest" % "2.0.M6-SNAP1" % "test"
        )
}

// No main class since dsprofile is a library

mainClass := None

scalaSource in Compile <<= baseDirectory { _ / "src" }

scalaSource in Test <<= scalaSource in Compile

unmanagedSources in Test <<= (scalaSource in Test) map { s => {
    (s ** "*Tests.scala").get
}}

unmanagedSources in Compile <<=
    (scalaSource in Compile, unmanagedSources in Test) map { (s, tests) =>
        ((s ** "*.scala") +++ (s ** "*.java") --- tests).get
    }

parallelExecution in Test := false

// Documentation

// Link the documentation to the source in the main repository

// FIXME
// scalacOptions in (Compile, doc) <++= baseDirectory map {
//     bd => Seq (
//         "-sourcepath",
//             bd.getAbsolutePath,
//         "-doc-source-url",
//             "https://code.google.com/p/kiama/source/browse€{FILE_PATH}.scala"
//     )
// }

// Publishing

// FIXME
// publishTo <<= version { v =>
//     val nexus = "https://oss.sonatype.org/"
//     if (v.trim.endsWith ("SNAPSHOT"))
//         Some ("snapshots" at nexus + "content/repositories/snapshots")
//     else
//         Some ("releases" at nexus + "service/local/staging/deploy/maven2")
// }
//
// publishMavenStyle := true
//
// publishArtifact in Test := true
//
// pomIncludeRepository := { x => false }
//
// pomExtra := (
//     <url>http://kiama.googlecode.com</url>
//     <licenses>
//         <license>
//             <name>LGPL 3.0 license</name>
//             <url>http://www.opensource.org/licenses/lgpl-3.0.html</url>
//             <distribution>repo</distribution>
//         </license>
//     </licenses>
//     <scm>
//         <url>https://kiama.googlecode.com/hg</url>
//         <connection>scm:hg:https://kiama.googlecode.com/hg</connection>
//     </scm>
//     <developers>
//         <developer>
//            <id>inkytonik</id>
//            <name>Tony Sloane</name>
//            <url>https://code.google.com/u/inkytonik</url>
//         </developer>
//     </developers>
// )
