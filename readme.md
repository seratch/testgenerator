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
        val first: String = ""
        val last: String = ""
        val instance = new Name(first,last)
        instance should not be null
      }
    
    }

### BeanHolder

    package com.example
    import com.exmaple.bean.Bean
    class BeanHolder(bean: Bean)

### BeanHolderSuite

    package example

    import org.scalatest._
    import org.scalatest.matchers._
    import org.junit.runner.RunWith
    import org.scalatest.junit.JUnitRunner
    import com.example.bean.Bean

    @RunWith(classOf[JUnitRunner])
    class BeanHolderSuite extends FunSuite with ShouldMatchers {

      type ? = this.type

      test("available") {
        val bean: Bean = null
        val instance = new BeanHolder(bean)
        instance should not be null
      }

    }

### Util

    package com.example
    object Util

### UtilSuite

    package com.example
    
    import org.scalatest._
    import org.scalatest.matchers._
    import org.junit.runner.RunWith
    import org.scalatest.junit.JUnitRunner

    @RunWith(classOf[JUnitRunner])
    class UtilSuite extends FunSuite with ShouldMatchers {
    
      type ? = this.type
    
      test("available") {
        Util.isInstanceOf[Singleton] should equal(true)
      }
    
    }

### Writable

    package com.example
    trait Writable

### WritableSuite

    package com.example
    
    import org.scalatest._
    import org.scalatest.matchers._
    import org.junit.runner.RunWith
    import org.scalatest.junit.JUnitRunner

    @RunWith(classOf[JUnitRunner])
    class WritableSuite extends FunSuite with ShouldMatchers {
    
      type ? = this.type
    
      test("available") {
        val mixedin = new Object with Writable
        mixedin should not be null
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

* File or directory 

"src/main/scala" is omissible. 

    sbt
    > testgen src/main/scala/com/example/models.scala
    > "com.example.StaffSuite" is already in being.
    > "com.example.CompanySuite" is already in being.
    > "com.example.StockSuite" is created.

* Class name 

When you specify a class name, the name should be the name of the source file. 

For example, "com.example.MyApp" will be translated as "src/main/scala/com/example/MyApp.scala".

    sbt
    > testgen com.example.MyApp
    > "com.example.MyAppSuite" is created.

* Package name 

"testgen" will search targets recursively under the directory.

    sbt
    > testgen com.example
    > "com.example.MyAppSuite" is created.
    > "com.example.util.MyUtilSuite" is created.

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
 
### run "testgen" goal

The usage is same as sbt plugin.

    maven testgen:run -Dtarget=com.exmaple.MyApp

## then Happy testing! :)

Have fun!

