package ca.hyperreal.options

import scala.collection.mutable.{ListBuffer, Buffer, Map => MutableMap, HashMap}


trait OptionsInterface
{
	/**
	Returns the mutable map that holds the switch settings.  The map keys are the actual command
	line switches that would be typed to turn the switches on.
	@return switch map
	*/
	def switchesMap: MutableMap[String, Boolean]
	
	def argumentsMap: MutableMap[String, String]
	
	def resultBuffer: Buffer[String]
	
	def hasNext: Boolean
	
	def next: String
}

class Options( switchNames: List[String], argumentNames: List[String],
	handlerMappings: List[(String, (String, OptionsInterface) => Unit)], defaults: (String, String)*)
{	
	private val switches = new HashMap[String, Boolean]
	private val arguments = Set( argumentNames:_* )
	private val map = HashMap( defaults:_* )
	private val handlers = HashMap( handlerMappings:_* )
	
	for (s <- switchNames)
		switches += s -> false
	
	if (switchNames.length > switches.size)
		sys.error( "there are duplication switch names" )
	
	if (argumentNames.length > arguments.size)
		sys.error( "there are duplication argument names" )
	
	if (!(switches.keySet & arguments).isEmpty)
		sys.error( "there is an argument in common with a switch" )
	
	if (!(handlers.keySet & arguments).isEmpty)
		sys.error( "there is a handler in common with an argument" )
	
	if (!(handlers.keySet & switches.keySet).isEmpty)
		sys.error( "there is a handler in common with a switch" )
		
	for ((k, v) <- defaults)
		if (!arguments( k )) sys.error( "unknown argument name: " + k )
		
	def set( name: String ) = switches( name )
	
	def apply( name: String ) = map( name )
	
	def get( name: String ) = map get name
	
	def parse( args: Array[String] ) =
	{
	val result = new ListBuffer[String]
	val it = args.iterator
	val interface =
		new OptionsInterface
		{
			def switchesMap = switches
			def argumentsMap = map
			def resultBuffer = result
			def hasNext = it.hasNext
			def next = it.next
		}
		
		while (it hasNext)	
		{
		val s = it.next
		
			if (switches contains s)
				switches += s -> true
			else if (arguments( s ))
			{
				if (!it.hasNext) sys.error( "missing value for option: " + s )
				
				map += s -> it.next
			}
			else if (handlers contains s)
				handlers( s )( s, interface )
			else
				result += s
		}
		
		result.toList
	}
	
	override def toString = "switches " + switches + ";  arguments " + map
}