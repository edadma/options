package xyz.hyperreal.options

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

class Options( switchNames: Seq[String], argumentNames: Seq[String],
	handlerMappings: Seq[(String, (String, OptionsInterface) => Unit)], defaults: (String, String)*)
{
	private val switches = HashMap[String, Boolean]( switchNames map (_ -> false): _* )
	private val arguments = Set( argumentNames:_* )
	private val map = HashMap( defaults:_* )
	private val handlers = HashMap( handlerMappings:_* )
	
	require( switchNames.length == switches.size, "there are duplicate switch names" )
	
	require( argumentNames.length == arguments.size, "there are duplicate argument names" )
	
	require( handlerMappings.length == handlers.size, "there are duplicate handlers" )
	
	require( (switches.keySet & arguments) isEmpty, "there is an argument in common with a switch" )
	
	require( (handlers.keySet & arguments) isEmpty, "there is a handler in common with an argument" )
	
	require( (handlers.keySet & switches.keySet) isEmpty, "there is a handler in common with a switch" )
	
	defaults find {case (k, _) => !(arguments contains k)} map {case (k, _) => sys.error( s"unknown argument '$k' in defaults: " )}
	
	def set( name: String ): Boolean = switches( name )
	
	def apply( name: String ): String = map( name )
	
	def get( name: String ): Option[String] = map get name
	
	def parse( args: Array[String] ): List[String] =
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
				
				map(s) = it.next
			}
			else if (handlers contains s)
				handlers( s )( s, interface )
			else
				result += s
		}
		
		result.toList
	}
	
	override def toString = "switches " + switches.toList.sortBy( _._1 ).toMap + ";  arguments " + map.toList.sortBy( _._1 ).toMap
}