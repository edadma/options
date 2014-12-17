package ca.hyperreal.options


object TestMain extends App
{
	val options = new Options( List("-a", "-d"), List("-b", "-c"), List("-e" -> ((a, b) => b.resultBuffer += "happy")), "-c" -> "happy" )
	
	println( options parse Array( "-a", "wow", "-b", "awesome", "be", "-e" ) )
	println( options )
}