# scala-testgen : Scala Unit Test Template Generator

## Setup

Please check the following page.

### [testgen-sbt/readme.md](https://github.com/seratch/testgen-sbt/blob/master/readme.md)

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

class NameSuite extends FunSuite with ShouldMatchers {

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
import entity.Bean

class BeanHolderSpec extends FlatSpec with MustMatchers {

  behaivor of "BeanHolder"

  it should "be available" in {
    val bean: Bean = null
    val instance = new BeanHolder(bean)
    instance must not be null
  }

}
```

Following is an example with specs/specs2:

```scala
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

class UtilSuite extends FunSuite with ShouldMatchers {

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

class WritableSuite extends FunSuite with ShouldMatchers {

  test("available") {
    val mixedin = new Object with Writable
    mixedin should not be null
  }

}
```

## Usage with sbt

Please check the following page.

### [testgen-sbt/readme.md](https://github.com/seratch/testgen-sbt/blob/master/readme.md)


## Usage with maven 

### How to setup

#### pom.xml

```xml
<build>
  <plugins>
    <plugin>
      <groupId>com.github.seratch</groupId>
      <artifactId>maven-testgen-plugin</artifactId>
      <version>1.0.1</version>
      <!-- If you need
      <configuration>
        <srcDir>src/main/scala</srcDir>
        <srcTestDir>src/test/scala</srcTestDir>
        <encoding>UTF-8</encoding>
        <testTemplate>scalatest.FunSuite</testTemplate>
        <scalatest_Matchers>ShouldMatchers</scalatest_Matchers>
        <withJUnitRunner>false</withJUnitRunner>
        <lineBreak>CRLF</lineBreak>
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

