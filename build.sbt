
name := "dsprofile"

// Main settings

version := "0.4.0-SNAPSHOT"

organization := "org.bitbucket.inkytonik.dsprofile"

// Scala compiler settings

scalaVersion := "2.11.0-RC3"

scalaBinaryVersion := "2.11.0-RC3"

scalacOptions := Seq ("-deprecation", "-unchecked")

scalacOptions in Compile <<= (scalaVersion, scalacOptions) map {
    (version, options) =>
        val versionOptions =
            if (version.startsWith ("2.9"))
                Seq ()
            else
                Seq ("-feature")
        options ++ versionOptions
}

scalacOptions in Test <<= scalacOptions in Compile

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
            "org.scalatest" %% "scalatest" % "2.1.2" % "test"
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

scalacOptions in (Compile, doc) <++= baseDirectory map {
    bd => Seq (
        "-sourcepath",
            bd.getAbsolutePath,
        "-doc-source-url",
            "https://bitbucket.org/inkytonik/dsprofile/src/default€{FILE_PATH}.scala"
    )
}

// Publishing

publishTo <<= version { v =>
    val nexus = "https://oss.sonatype.org/"
    if (v.trim.endsWith ("SNAPSHOT"))
        Some ("snapshots" at nexus + "content/repositories/snapshots")
    else
        Some ("releases" at nexus + "service/local/staging/deploy/maven2")
}

publishMavenStyle := true

publishArtifact in Test := true

pomIncludeRepository := { x => false }

pomExtra := (
    <url>https://bitbucket.org/inkytonik/dsprofile</url>
    <licenses>
        <license>
            <name>LGPL 3.0 license</name>
            <url>http://www.opensource.org/licenses/lgpl-3.0.html</url>
            <distribution>repo</distribution>
        </license>
    </licenses>
    <scm>
        <url>https://bitbucket.org/inkytonik/dsprofile</url>
        <connection>scm:hg:https://bitbucket.org/inkytonik/dsprofile</connection>
    </scm>
    <developers>
        <developer>
           <id>inkytonik</id>
           <name>Tony Sloane</name>
           <url>https://bitbucket.org/inkytonik</url>
        </developer>
    </developers>
)
