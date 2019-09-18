package xyz.hyperreal.options

import org.scalatest._
import prop.PropertyChecks


class Tests extends FreeSpec with PropertyChecks with Matchers {
	
	"tests" in
	{
		val options = new Options( List("-a", "-d"), List("-b", "-c"), List("-e" -> ((a, b) => b.resultBuffer += "happy")), "-c" -> "happy" )
		
		options parse Array( "-a", "wow", "-b", "awesome", "be", "-e" ) shouldBe List( "wow", "be", "happy" )
		options.toString shouldBe "switches Map(-a -> true, -d -> false);  arguments Map(-b -> awesome, -c -> happy)"
	}
	
}