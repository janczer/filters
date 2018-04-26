name := "filters"

version := "0.3"

scalaVersion := "2.12.1"

libraryDependencies += "org.scalatest" % "scalatest_2.12" % "3.0.5" % "test"

mainClass in (Compile, run) := Some("janczer.filters.Filters")
        
