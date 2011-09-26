# "testgen": A Scala unit test template generator

"testgen" is a Scala unit test template generator.

You can use "testgen" as a sbt 0.10.x plugin or a maven plugin.

## output example

The default template is "FunSuite with ShouldMathcers", but it's possible to specify other ScalaTest templates or specs/specs2.

### Class

    package com.example
    case class Name(first: String, last: String)

When you run "testgen" via sbt:

    sbt
    > testgen com.example.Name
    > "com.example.NameSuite" is created.

And following will be generated:

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
    class BeanHolder(bean: Bean) extends AbstractHolder

Run "testgen":

    sbt
    > testgen com.example.BeanHolder
    > "com.example.BeanHolderSuite" is created.

entity.Bean will be imported in the generated test:

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

    sbt
    > testgen com.example.Util
    > "com.example.UtilSuite" is created.

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

    sbt
    > testgen com.example.Writable
    > "com.example.WritableSuite" is created.

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

### configurations

Currently possbile by system properties.

    java -jar sbt-launch.jar \
      -Dtestgen.srcDir=src/main/scala \
      -Dtestgen.srcTestDir=src/test/scala \
      -Dtestgen.encoding=UTF-8 \
      -Dtestgen.testTemplate=scalatest.FunSuite \
      -Dtestgen.scalagtest.Matchers=ShouldMatchers

### configuration ("testgen.testTemplate")

* "scalatest.FunSuite(default)"
* "scalatest.Assertions"
* "scalatest.Spec"
* "scalatest.WordSpec"
* "scalatest.FlatSpec"
* "scalatest.FeatureSpec"
* "specs.Specification"
* "specs2.Specification"

### configuration ("testgen.scalatest.Matchers")

* "ShouldMatchers"
* "MustMatchers"
* ""(empty)

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

The usage is same as sbt plugin.

    maven testgen:run -Dtarget=com.exmaple.MyApp

## then Happy testing! :)

Have fun!

