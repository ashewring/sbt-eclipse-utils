import sbt._

class SbtEclipseUtilsPlugin(info: ProjectInfo) extends PluginProject(info) {
	override def managedStyle = ManagedStyle.Maven

	lazy val publishTo = {
		val suffix = if (version.toString.trim.endsWith("SNAPSHOT")) {
			"snapshots/"
		} else {
			"releases/"
		}
		val path = "../ashewring.github.com/maven/" + suffix
		Resolver.file("GitHub Pages", new java.io.File(path))
	}

	val sbtIdeaRepo = "sbt-idea-repo" at "http://mpeltonen.github.com/maven/"
	val sbtIdea = "com.github.mpeltonen" % "sbt-idea-processor_2.7.7" % "0.4.0"
}
