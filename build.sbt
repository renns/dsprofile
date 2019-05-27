
name := "dsprofile"

// Main settings

version := "0.4.0"

organization := "org.bitbucket.inkytonik.dsprofile"

// Scala compiler settings

scalaVersion := "2.12.8"

scalacOptions := Seq ("-deprecation", "-unchecked")

scalacOptions in ThisBuild in Compile ++= {
    val versionOptions =
        if (scalaVersion.value.startsWith ("2.9"))
            Seq ()
        else
            Seq ("-feature")
    versionOptions
}

scalacOptions in Test := (scalacOptions in Compile).value

// Migration manager (mima)

// mimaDefaultSettings

// previousArtifact <<= (name, organization) { (n, o) =>
//     Some (o % (n + "_2.9.2") % "0.1.0")
// }

// Interactive settings

logLevel := Level.Info

shellPrompt := {
    state =>
        Project.extract(state).currentRef.project + " " + version.value +
            " " + scalaVersion.value + "> "
}

// Dependencies

libraryDependencies ++= Seq (
    "org.scalatest" %% "scalatest" % "3.0.8-RC4" % "test"
)

// No main class since dsprofile is a library

mainClass := None

scalaSource in Compile := baseDirectory.value / "src"

scalaSource in Test := (scalaSource in Compile).value

unmanagedSources in Test :=
    (((scalaSource in Test).value) ** "*Tests.scala").get

unmanagedSources in Compile :=
    (((scalaSource in Compile).value ** "*.scala") +++ ((scalaSource in Compile).value ** "*.java") --- (unmanagedSources in Test).value).get

parallelExecution in Test := false

// Documentation

// Link the documentation to the source in the main repository

scalacOptions in (Compile, doc) ++=
    Seq (
        "-sourcepath",
            baseDirectory.value.getAbsolutePath,
        "-doc-source-url",
            "https://bitbucket.org/inkytonik/dsprofile/src/default€{FILE_PATH}.scala"
    )

// Publishing

publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (version.value.trim.endsWith ("SNAPSHOT"))
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
