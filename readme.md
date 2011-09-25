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
      "com.github.seratch" %% "testgen-sbt" % "0.1-SNAPSHOT"
    )

### configure testgen

Currently possbile by system properties.

    java -jar sbt-launch.jar \
    -Dtestgen.srcDir=src/main/scala \
    -Dtestgen.srcTestDir=src/test/scala \
    -Dtestgen.encoding=UTF-8

### run sbt and "testgen" command

file path or directory:

    sbt
    > testgen src/main/scala/com/example/MyApp.scala
    > "com.example.MyAppSuite" is created.

class name:

    sbt
    > testgen com.example.MyApp
    > "com.example.MyAppSuite" is created.

package name:

    sbt
    > testgen com.example
    > "com.example.MyAppSuite" is created.
    > "com.example.MyApp2Suite" is created.

### then Happy testing! :)

Have fun!

