# Scala test generator sbt plugin 

## How to setup

### Requirement

[xsbt 0.11.x](https://github.com/harrah/xsbt)

### Add this plugin to project/plugins.sbt

Delete project/plugins directory if it exists and edit project/plugins.sbt as follows:

```scala
addSbtPlugin("com.github.seratch" %% "testgenerator" % "1.1.0")
```

or use ls:

[testgenerator @ ls.implicit.ly](http://ls.implicit.ly/seratch/testgenerator)

```
ls -n testgenerator
ls-install testgenerator
```

### Add the settings to built.sbt

```scala
seq(testgeneratorSettings: _*)
```


## Run sbt

"testgenerator" requires xsbt 0.11.x.

See also: [https://github.com/harrah/xsbt/wiki/Setup](https://github.com/harrah/xsbt/wiki/Setup)

### Specify a filename

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
> test-gen com/example/models.scala
"com.example.StaffSpec" already exists.
"com.example.CompanySpec" already exists.
"com.example.StockSpec" created.
```

### Specify a class name

If you specify a class name, it must be the name of the source file.

"com.example.MyApp" will be translated as "src/main/scala/com/example/MyApp.scala".

```sh
$ sbt
> test-gen com.example.MyApp
"com.example.MyAppSpec" created.
```

### Specify a directory

"test-gen" will search targets recursively under the directory.

```sh
$ sbt
> test-gen com/example
```

### Specify a package name

The same as specifying a directory.

```sh
$ sbt
> test-gen com.example
"com.example.MyAppSpec" created.
"com.example.util.MyUtilSpec" created.
```

## Configuration

Please add the following line at the top of your buid.sbt:

```scala
import testgenerator.SbtKeys._
```

and then,

```scala
seq(testgeneratorSettings: _*)

testgeneratorEncoding in Compile := "UTF-8"

testgeneratorTestTemplate in Compile := "scalatest.FlatSpec"

testgeneratorScalaTestMatchers in Compile := "ShouldMatchers"

testgeneratorWithJUnitRunner in Compile := false 

testgeneratorLineBreak in Compile := "LF"
```

### testgeneratorTestTemplate

- ["scalatest.FunSuite"](http://www.scalatest.org/scaladoc/1.6.1/#org.scalatest.FunSuite)
- ["scalatest.Assertions"](http://www.scalatest.org/scaladoc/1.6.1/#org.scalatest.Assertions)
- ["scalatest.Spec"](http://www.scalatest.org/scaladoc/1.6.1/#org.scalatest.Spec)
- ["scalatest.WordSpec"](http://www.scalatest.org/scaladoc/1.6.1/#org.scalatest.WordSpec)
- ["scalatest.FlatSpec"](http://www.scalatest.org/scaladoc/1.6.1/#org.scalatest.FlatSpec) (default)
- ["scalatest.FeatureSpec"](http://www.scalatest.org/scaladoc/1.6.1/#org.scalatest.FeatureSpec)
- ["specs.Specification"](http://code.google.com/p/specs/wiki/DeclareSpecifications)
- ["specs2.Specification"](http://etorreborre.github.com/specs2/guide/org.specs2.guide.QuickStart.html#Quick+Start)

### testgenScalaTestMatchers

- ["ShouldMatchers"](http://www.scalatest.org/scaladoc/1.6.1/#org.scalatest.matchers.ShouldMatchers) (default)
- ["MustMatchers"](http://www.scalatest.org/scaladoc/1.6.1/#org.scalatest.matchers.MustMatchers)
- "" (empty)


## Happy Testing! :)

