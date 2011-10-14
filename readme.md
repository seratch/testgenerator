# testgen : Scala unit test template generator

## Overview

The default template is "FunSuite with ShouldMatchers", but it's also possible to specify other templates, such as ScalaTest or specs/specs2.

### Class

```scala
package com.example
case class Name(first: String, last: String)
```

When you run "testgen" via sbt:

```sh
$ sbt
> testgen com.example.Name
"com.example.NameSuite" created.
```

the following code will be generated:

```scala
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
```

Also for classes which import other types:

```scala
package com.example
import entity.Bean
class BeanHolder(val bean: Bean) extends AbstractHolder
```

Run "testgen":

```sh
$ sbt
> testgen com.example.BeanHolder
"com.example.BeanHolderSpec" created.
```

"entity.Bean" will be imported in the generated test:

```scala
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
```

Following is an example with specs/specs2:

```scala
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
```

### Object

```scala
package com.example
object Util
```

Run "testgen" via sbt:

```sh
$ sbt
> testgen com.example.Util
"com.example.UtilSuite" created.
```

The following code will be generated:

```scala
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
```

### Trait

```scala
package com.example
trait Writable
```

Run "testgen" via sbt:

```sh
$ sbt
> testgen com.example.Writable
"com.example.WritableSuite" created.
```

The following code will be generated:

```scala
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
```

## Usage with sbt

### How to setup

#### xsbt 0.10.x : put project/plugins/build.sbt

Edit project/plugins/build.sbt as follows:

```scala
resolvers ++= Seq(
  "seratch" at "http://seratch.github.com/mvn-repo/releases"
)

libraryDependencies ++= Seq (
  "com.github.seratch" %% "testgen-sbt" % "0.1"
)
```

#### xsbt 0.11.x : put project/plugins.sbt

Delete project/plugins directory if it exists and edit project/plugins.sbt as follows:

```scala
resolvers ++= Seq(
  "seratch" at "http://seratch.github.com/mvn-repo/releases"
)

addSbtPlugin("com.github.seratch" %% "testgen-sbt" % "0.1")
```

### Run sbt

#### xsbt 0.10.x / 0.11.x

"testgen" requires xsbt 0.10.x / 0.11.x (sorry, sbt 0.7.x is not supported). 

See also: [https://github.com/harrah/xsbt/wiki/Setup](https://github.com/harrah/xsbt/wiki/Setup)

#### Specify a filename

src/main/scala/com/example/models.scala:

```scala
package com.example
case class Staff(id: Long, name: String, ...)
case class Company(id: Long, name: String, ...)
case class Stock(id: Long, itemId: Long, ...)
```

And specify the above file:

```sh
$ sbt
> testgen src/main/scala/com/example/models.scala
"com.example.StaffSuite" already exists.
"com.example.CompanySuite" already exists.
"com.example.StockSuite" created.
```

"src/main/scala" is omissible.

```sh
$ sbt
> testgen com/example/models.scala
```

#### Specify a class name

If you specify a class name, it must be the name of the source file.

"com.example.MyApp" will be translated as "src/main/scala/com/example/MyApp.scala".

```sh
$ sbt
> testgen com.example.MyApp
"com.example.MyAppSuite" created.
```

#### Specify a directory

"testgen" will search targets recursively under the directory.

```sh
$ sbt
> testgen src/main/scala/com/example
```

#### Specify a package name

The same as specifying a directory.

```sh
$ sbt
> testgen com.example
"com.example.MyAppSuite" created.
"com.example.util.MyUtilSuite" created.
```

### Configuration

Currently configurable using system properties.

```sh
java -jar sbt-launch.jar \
  -Dtestgen.srcDir=src/main/scala \
  -Dtestgen.srcTestDir=src/test/scala \
  -Dtestgen.encoding=UTF-8 \
  -Dtestgen.testTemplate=scalatest.FunSuite \
  -Dtestgen.scalatest.Matchers=ShouldMatchers
```

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

### How to setup

#### pom.xml

```xml
<pluginRepositories>
  <pluginRepository>
    <id>seratch.github.com releases</id>
    <name>seratch.github.com releases</name>
    <url>http://seratch.github.com/mvn-repo/releases</url>
  </pluginRepository>
</pluginRepositories>

<build>
  <plugins>
    <plugin>
      <groupId>com.github.seratch</groupId>
      <artifactId>maven-testgen-plugin</artifactId>
      <version>0.1</version>
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
```

### Run "testgen" goal

The rule to specify targets is the same as with the sbt plugin.

```sh
maven testgen:run -Dtarget=com.exmaple.MyApp
```

## Happy Testing! :)

