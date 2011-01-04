import sbt._

class SyncliciousProject(info: ProjectInfo) extends DefaultProject(info){
  override def repositories = Set(
    "Gson Google Code Repository" at "http://google-gson.googlecode.com/svn/mavenrepo"
  )

  override def libraryDependencies = Set(
      "org.apache.httpcomponents" % "httpcore" % "4.1" % "compile",
      "org.apache.httpcomponents" % "httpclient" % "4.0.3" % "compile",
      "com.google.code.gson" % "gson" % "1.6" % "compile"
  )
}

