import java.io.File
import sbt._

class SbtEclipseUtilsPlugin(info: ProjectInfo) extends PluginProject(info) {
  override def managedStyle = ManagedStyle.Maven
  lazy val publishTo = Resolver.file("GitHub Pages", new java.io.File("../ashewring.github.com/maven/"))
	
  val sbtIdeaRepo = "sbt-idea-repo" at "http://mpeltonen.github.com/maven/"
  val sbtIdea = "com.github.mpeltonen" % "sbt-idea-processor_2.7.7" % "0.4.0"
}
