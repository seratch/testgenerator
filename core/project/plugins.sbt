resolvers ++= Seq(
  "seratch" at "http://seratch.github.com/mvn-repo/releases",
  "sbt-idea-repo" at "http://mpeltonen.github.com/maven/"
)

resolvers += Classpaths.typesafeResolver

addSbtPlugin("com.github.seratch" %% "testgen-sbt" % "0.2")

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.0.0")

addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.0.0-M3")

addSbtPlugin("com.typesafe.sbtscalariform" % "sbtscalariform" % "0.3.0")

// for sonatype publishment

resolvers += Resolver.url("sbt-plugin-releases", new URL("http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases/"))(Resolver.ivyStylePatterns)

addSbtPlugin("com.jsuereth" % "xsbt-gpg-plugin" % "0.5")


