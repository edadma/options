Simple Command Line Options Parser
==================================

This is a very simple and easy to use command line options parser for Scala applications.  Althought it doesn't have a dazzling range of cleverly implemented features, it is quite useful for most applications that take command line options, and you will understand how to use it in about 3 minutes flat.

Here is an example of how to use it:

	import ca.hyperreal.options

	object TestMain extends App
	{
		val options = new Options( List("-a", "-d"), List("-b", "-c"), List("-e" -> ((a, b) => b.resultBuffer += "happy")), "-c" -> "happy" )
		
		println( options parse Array( "-a", "wow", "-b", "awesome", "be", "-e" ) )
		println( options )
	}
	
The above example produces the following output:

	List(wow, be, happy)
	switches Map(-a -> true, -d -> false);  arguments Map(-c -> happy, -b -> awesome)

The hypothetical command line arguments that are being parsed are

	-a wow -b awesome be -e
	
where `-a` is a switch (on/off), `wow` is just a string (e.g. filename) to be gathered, `-b` is an argument (associated with what comes after it), `be` is another string to be gathered, and `-e` causes the word `happy` to be appended to the result.

In the example above, we begin by creating an `Options` object by providing a list of switches, a list of arguments, a list of special handlers, followed by default values for argument options.  Then we parse the command line options by calling `parse`, which returns a list of strings that appeared on the command line not associated with any argument options.  The `Options` object can now be querried using: `set( name: String ): Boolean` to check if a switch is set, `apply( name: String ): String` to get the value of an argument if you are sure it was set (i.e. it was given a default value), and `get( name: String ): Option[String]` (which returns `Option[String]`) to get the value of an argument whether it's set or not.


## License

Options is distributed under the MIT License, meaning that you are free to use it in your free or proprietary software.


## Usage

Use the following elements to use Options in your Maven project:

	<repository>
		<id>hyperreal</id>
		<url>http://hyperreal.ca/maven2</url>
	</repository>

	<dependency>
		<groupId>ca.hyperreal</groupId>
		<artifactId>options</artifactId>
		<version>0.1</version>
	</dependency>

Add the following to your `build.sbt` file to use Options in your SBT project:

	resolvers += "Hyperreal Repository" at "http://hyperreal.ca/maven2"

	libraryDependencies += "ca.hyperreal" %% "options" % "0.1"
