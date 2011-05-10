/**
 * Copyright (c) 2011, Andrew Shewring
 * Licensed under the new BSD License (see the LICENSE.txt file for details).
 */
package com.github.ashewring.sbteclipseutils

import sbt._
import scala.xml._
import java.io.File

trait SbtEclipseUtils {
	this: ParentProject =>

	/**
	 * Creates an Eclipse project by name
	 */
	lazy val createProject = task { args =>
		if (args.length == 1) {
			createEclipseProject(args(0))
		} else {
			task {
				Some("Usage: create-project <project-name>") 
			}
		}
	} describedAs("Creates an Eclipse project by name")
	
	private def createEclipseProject(name: String) = task {
		val pluginsDir = new File(System.getProperty("user.dir"))
		val projectDir = new File(pluginsDir, name)
		if (projectDir.exists) {
			Some("The project '" + name + "' already exists")
		} else {
			projectDir.mkdir()

			// create .project
			val dotProjectXml =
<projectDescription>
	<name>{name}</name>
	<comment></comment>
	<buildSpec>
		<buildCommand>
			<name>org.eclipse.jdt.core.javabuilder</name>
			<arguments>
			</arguments>
		</buildCommand>
		<buildCommand>
			<name>org.eclipse.pde.ManifestBuilder</name>
			<arguments>
			</arguments>
		</buildCommand>
		<buildCommand>
			<name>org.eclipse.pde.SchemaBuilder</name>
			<arguments>
			</arguments>
		</buildCommand>
	</buildSpec>
	<natures>
		<nature>org.eclipse.jdt.core.javanature</nature>
		<nature>org.eclipse.pde.PluginNature</nature>
	</natures>
</projectDescription>

			writeXml(new File(projectDir, ".project"), dotProjectXml)

			// create .classpath
			val dotClasspathXml =
<classpath>
	<classpathentry kind="con" path="org.eclipse.jdt.launching.JRE_CONTAINER/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/J2SE-1.5"/>
	<classpathentry kind="con" path="org.eclipse.pde.core.requiredPlugins"/>
	<classpathentry kind="src" path="src"/>
	<classpathentry kind="output" path="bin"/>
</classpath>

			writeXml(new File(projectDir, ".classpath"), dotClasspathXml )
			
			// create build.properties
			val buildPropertiesContents = List(
				"source.. = src/",
				"bin.includes = META-INF/,\\",
				"               ."
			)
			
			writeLines(new File(projectDir, "build.properties"), buildPropertiesContents)
			
			// create source directory
			val sourceDir = new File(projectDir, "src")
			sourceDir.mkdir()
			
			// TODO create initial packages
			
			// create META-INF and MANIFEST.MF
			val metaInfDir = new File(projectDir, "META-INF")
			metaInfDir.mkdir()
			
			val manifestVersion = "Manifest-Version: 1.0"
			val bundleManifestVersion = "Bundle-ManifestVersion: 2"
			val bundleName = "Bundle-Name: " + name
			val bundleSymbolicName = "Bundle-SymbolicName: " + name + "; singleton:=true"
			val bundleVersion = "Bundle-Version: 0.0.0"
			val bundleRequiredExecEnv = "Bundle-RequiredExecutionEnvironment: J2SE-1.5"
			val manifestContents = List(manifestVersion, bundleManifestVersion, bundleName, bundleSymbolicName, bundleVersion, bundleRequiredExecEnv)
			
			writeLines(new File(metaInfDir, "MANIFEST.MF"), manifestContents)
			
			// TODO create a test project given a "normal" project
			None
		}
	}
	
	private def writeLines(file: File, lines: List[String]) {
		FileUtilities.write(file, lines.mkString("\n"), log)
	}
	
	private def writeXml(file: File, xml: scala.xml.Elem) {
		val w = new java.io.StringWriter
		XML.write(w, xml, "UTF-8", true, null)
		FileUtilities.write(file, w.toString, log)
	}
}
