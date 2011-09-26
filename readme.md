# "testgen" : Scala unit test template generator

You can use "testgen" as a sbt 0.10.x plugin or a maven plugin.

## Overview

The default template is "FunSuite with ShouldMathcers", but it's also possible to specify other templates of ScalaTest or specs/specs2.

### Class

    package com.example
    case class Name(first: String, last: String)

When you run "testgen" via sbt:

    $ sbt
    > testgen com.example.Name
    "com.example.NameSuite" is created.

Following will be generated:

    package com.example
    
    import org.scalatest._
    import org.scalatest.matchers._
    import org.junit.runner.RunWith
    import org.scalatest.junit.JUnitRunner
    
    @RunWith(classOf[JUnitRunner])
    class NameSuite extends FunSuite with ShouldMatchers {
    
      type ? = this.type // for IntelliJ IDEA
    
      test("available") {
        val first: String = ""
        val last: String = ""
        val instance = new Name(first,last)
        instance should not be null
      }
    
    }

Also for classes with type-import:

    package com.example
    import entity.Bean
    class BeanHolder(val bean: Bean) extends AbstractHolder

Run "testgen":

    $ sbt
    > testgen com.example.BeanHolder
    "com.example.BeanHolderSuite" is created.

"entity.Bean" will be imported in the generated test:

    package com.example

    import org.scalatest._
    import org.scalatest.matchers._
    import org.junit.runner.RunWith
    import org.scalatest.junit.JUnitRunner
    import entity.Bean

    @RunWith(classOf[JUnitRunner])
    class BeanHolderSpec extends FlatSpec with MustMatchers {

      type ? = this.type // for IntelliJ IDEA

      "BeanHolder" should "be available" in {
        val bean: Bean = null
        val instance = new BeanHolder(bean)
        instance must not be null
      }

    }

Following is an example with specs/specs2:

    @RunWith(classOf[JUnitRunner])
    class BeanHolderSpec extends Specification {
    
      "BeanHolder" should {
        "be available" in {
          val instance = new Sample()
          instance must notBeNull
          // (specs2) instance must not beNull 
        }
      }
     
    }

### Object

    package com.example
    object Util

Run "testgen" via sbt:

    $ sbt
    > testgen com.example.Util
    "com.example.UtilSuite" is created.

Following will be generated:

    package com.example
    
    import org.scalatest._
    import org.scalatest.matchers._
    import org.junit.runner.RunWith
    import org.scalatest.junit.JUnitRunner

    @RunWith(classOf[JUnitRunner])
    class UtilSuite extends FunSuite with ShouldMatchers {
    
      type ? = this.type // for IntelliJ IDEA
    
      test("available") {
        Util.isInstanceOf[Singleton] should equal(true)
      }
    
    }

### Trait

    package com.example
    trait Writable

Run "testgen" via sbt:

    $ sbt
    > testgen com.example.Writable
    "com.example.WritableSuite" is created.

Following will be generated:

    package com.example
    
    import org.scalatest._
    import org.scalatest.matchers._
    import org.junit.runner.RunWith
    import org.scalatest.junit.JUnitRunner

    @RunWith(classOf[JUnitRunner])
    class WritableSuite extends FunSuite with ShouldMatchers {
    
      type ? = this.type // for IntelliJ IDEA
    
      test("available") {
        val mixedin = new Object with Writable
        mixedin should not be null
      }
    
    }

## Usage with sbt

### mkdir -p project/plugins

Create the directory if it doesn't exist yet.

### put project/plugins/plugins.sbt

Edit project/plugins/plugins.sbt as follows:

    scalaVersion := "2.8.1"

    resolvers ++= Seq(
      "seratch.github.com releases"  at "http://seratch.github.com/mvn-repo/releases",
      "seratch.github.com snapshots" at "http://seratch.github.com/mvn-repo/snapshots"
    )

    libraryDependencies ++= Seq(
      "com.github.seratch" %% "testgen-sbt" % "0.1-SNAPSHOT"
    )

### Run sbt

#### xsbt 0.10.x

"testgen" requires xsbt 0.10.x (sorry, sbt 0.7.x is not supported). 

See also: [https://github.com/harrah/xsbt/wiki/Setup](https://github.com/harrah/xsbt/wiki/Setup)

#### Specify a filename

src/main/scala/com/example/models.scala:

    package com.example
    case class Staff(id: Long, name: String, ...)
    case class Company(id: Long, name: String, ...)
    case class Stock(id: Long, itemId: Long, ...)

And specify the above file:

    $ sbt
    > testgen src/main/scala/com/example/models.scala
    "com.example.StaffSuite" is already in being.
    "com.example.CompanySuite" is already in being.
    "com.example.StockSuite" is created.

"src/main/scala" is omissible.

    $ sbt
    > testgen com/example/models.scala

#### Specify a class name

If you specify a class name, it must be the name of the source file.

"com.example.MyApp" will be translated as "src/main/scala/com/example/MyApp.scala".

    $ sbt
    > testgen com.example.MyApp
    "com.example.MyAppSuite" is created.

#### Specify a directory

"testgen" will search targets recursively under the directory.

    $ sbt
    > testgen src/main/scala/com/example

#### Specify a package name

As same as specifying a directory.

    $ sbt
    > testgen com.example
    "com.example.MyAppSuite" is created.
    "com.example.util.MyUtilSuite" is created.


### Configurations

Currently possbile by system properties.

    java -jar sbt-launch.jar \
      -Dtestgen.srcDir=src/main/scala \
      -Dtestgen.srcTestDir=src/test/scala \
      -Dtestgen.encoding=UTF-8 \
      -Dtestgen.testTemplate=scalatest.FunSuite \
      -Dtestgen.scalagtest.Matchers=ShouldMatchers

#### Configuration: "testgen.testTemplate"

* "scalatest.FunSuite" : default
* "scalatest.Assertions"
* "scalatest.Spec"
* "scalatest.WordSpec"
* "scalatest.FlatSpec"
* "scalatest.FeatureSpec"
* "specs.Specification"
* "specs2.Specification"

#### Configuration: "testgen.scalatest.Matchers"

* "ShouldMatchers" : default
* "MustMatchers"
* "" (empty string)

## Usage with maven 

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
          <!-- If you need
          <configuration>
            <srcDir>src/main/scala</srcDir>
            <srcTestDir>src/test/scala</srcTestDir>
            <encoding>UTF-8</encoding>
            <testTemplate>scalatest.FunSuite</testTemplate>
            <scalatest_Matchers>ShouldMatchers</scalatest_Matchers>
          </configuration>
          -->
        </plugin>
      </plugins>
    </build>
 
### run "testgen" goal

The rule to specify targets is same as sbt plugin.

    maven testgen:run -Dtarget=com.exmaple.MyApp

## then Happy testing! :)

Have fun!

