# testgen sbt-plugin

"testgen" is a Scala unit test code generator.

## What's this?

This is the sbt plugin for "testgen".

## How to use?

### prepare xsbt 0.10.1

See also: [https://github.com/harrah/xsbt/wiki/Setup](https://github.com/harrah/xsbt/wiki/Setup)

### mkdir -p {root}/project/plugins

Create the directory if it doesn't exist yet.

### vim {root}/project/plugins/testgen.sbt

    // *** Put this under project/plugins/ ***

    scalaVersion := "2.8.1"

    resolvers ++= Seq(
      "seratch.github.com releases"  at "http://seratch.github.com/mvn-repo/releases",
      "seratch.github.com snapshots" at "http://seratch.github.com/mvn-repo/snapshots"
    )

    libraryDependencies ++= Seq(
      "com.github.seratch" %% "testgen-core" % "0.1-SNAPSHOT",
      "com.github.seratch" %% "testgen-sbt" % "0.1-SNAPSHOT"
    )

### run sbt and "testgen" command

File path or directory:

    sbt
    > testgen src/main/scala/com/example/MyApp.scala
    > "com.example.MyAppSuite" is created.

Class:

    sbt
    > testgen com.example.MyApp
    > "com.example.MyAppSuite" is created.

Package:

    sbt
    > testgen com.example
    > "com.example.MyAppSuite" is created.
    > "com.example.MyApp2Suite" is created.

### then Happy testing! :)

Have fun!

