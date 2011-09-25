# "testgen": A Scala unit test code generator

"testgen" is a Scala unit test code generator.

You can use "testgen" as a sbt 0.10.x plugin or a maven plugin.

## output example

Currently only support "FunSuite with ShouldMathcers".

### Name

    package example
    case class Name(first: String, last: String)

### NameSuite

    package example
    
    import org.scalatest._
    import org.scalatest.matchers._
    import org.junit.runner.RunWith
    import org.scalatest.junit.JUnitRunner
    
    @RunWith(classOf[JUnitRunner])
    class NameSuite extends FunSuite with ShouldMatchers {
    
      type ? = this.type
    
      test("available") {
        val first: String = null
        val last: String = null
        val instance = new Name(first,last)
        instance should not be null
      }
    
    }

### MyHolder

    package com.example
    import com.exmaple.bean.Bean
    class MyHolder(bean: Bean)

### MyHolderSuite

    package example

    import org.scalatest._
    import org.scalatest.matchers._
    import org.junit.runner.RunWith
    import org.scalatest.junit.JUnitRunner
    import com.example.bean.Bean

    @RunWith(classOf[JUnitRunner])
    class MyHolderSuite extends FunSuite with ShouldMatchers {

      type ? = this.type

      test("available") {
        val bean: Bean = null
        val instance = new MyHolder(bean)
        instance should not be null
      }

    }

### MyObject

    package com.example
    object MyObject

### MyObjectSuite

    package com.example
    
    import org.scalatest._
    import org.scalatest.matchers._
    import org.junit.runner.RunWith
    import org.scalatest.junit.JUnitRunner

    @RunWith(classOf[JUnitRunner])
    class MyObjectSuite extends FunSuite with ShouldMatchers {
    
      type ? = this.type
    
      test("available") {
        MyObject.isInstanceOf[Singleton] should equal(true)
      }
    
    }

### MyTrait

    package com.example
    trait MyTrait

### MyTraitSuite

    package com.example
    
    import org.scalatest._
    import org.scalatest.matchers._
    import org.junit.runner.RunWith
    import org.scalatest.junit.JUnitRunner

    @RunWith(classOf[JUnitRunner])
    class MyTraitSuite extends FunSuite with ShouldMatchers {
    
      type ? = this.type
    
      test("available") {
        val mixined = new Object with MyTrait
        mixined should not be null
      }
    
    }

## sbt 0.10.x plugin

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

## maven plugin

### pom.xml

    <pluginRepositories>
      <pluginRepository>
        <id>seratch.github.com releases</id>
        <name>seratch.github.com releases</name>
        <url>http://seratch.github.com/mvn-repo/releases</url>
      </pluginRepository>
      <pluginRepository>
        <id>seratch.github.com snapshots</id>
        <name>seratch.github.com snapshots</name>
        <url>http://seratch.github.com/mvn-repo/snapshots</url>
      </pluginRepository>
    </pluginRepositories>

    <build>
      <plugins>
        <plugin>
          <groupId>com.github.seratch</groupId>
          <artifactId>maven-testgen-plugin</artifactId>
          <version>0.1-SNAPSHOT</version>
        </plugin>
      </plugins>
    </build>
 
### maven testgen:run -Dtarget=com.exmaple.MyApp

### then Happy testing! :)

Have fun!

