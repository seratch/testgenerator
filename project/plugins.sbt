externalResolvers ~= (_.filter(_.name != "Scala-Tools Maven2 Repository"))

resolvers +="sbt-idea-repo" at "http://mpeltonen.github.com/maven/"

resolvers += "Sonatype releases" at "http://oss.sonatype.org/content/repositories/releases"

resolvers += "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots"

resolvers += Classpaths.typesafeResolver

addSbtPlugin("com.github.mpeltonen" %% "sbt-idea" % "1.0.0")

//addSbtPlugin("com.typesafe.sbtscalariform" % "sbtscalariform" % "0.4.0")

// for sonatype publishment

resolvers += Resolver.url("sbt-plugin-releases", new URL("http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases/"))(Resolver.ivyStylePatterns)

addSbtPlugin("com.jsuereth" % "xsbt-gpg-plugin" % "0.6")


