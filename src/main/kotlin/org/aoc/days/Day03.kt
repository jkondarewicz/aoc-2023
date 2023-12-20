package org.aoc.days

import org.aoc.data.AoC
import org.aoc.utils.Coord2D
import kotlin.math.abs

class Day03: AoC<List<String>, Int, Int>() {
	override fun part1(parsedData: List<String>): Int {
		val operators = mutableSetOf<Coord2D>()
		val engineNumbers: List<EngineNumber> = parsedData.flatMapIndexed { y, line ->
			line.mapIndexedNotNull { x, character ->
				val s = character.toString()
				if (!s.matches("\\d".toRegex()) && s != ".") {
					operators.add(Coord2D(x, y))
				}
				EngineNumberBuilder.checkNumber(x, y, s, line.length)
			}
		}
		val correct = engineNumbers
			.filter { engineNumber ->
				operators.firstOrNull(engineNumber::isAdjacent) != null
			}
		return correct.sumOf { it.number }
	}
	
	override fun part2(parsedData: List<String>): Int {
		val operators = mutableSetOf<Coord2D>()
		val engineNumbers: List<EngineNumber> = parsedData.flatMapIndexed { y, line ->
			line.mapIndexedNotNull { x, character ->
				val s = character.toString()
				if (s == "*") {
					operators.add(Coord2D(x, y))
				}
				EngineNumberBuilder.checkNumber(x, y, s, line.length)
			}
		}
		return operators
			.mapNotNull { operator ->
				val adjacent = engineNumbers
					.filter { engineNumber -> engineNumber.isAdjacent(operator) }
				if (adjacent.size == 2) {
					adjacent[0].number * adjacent[1].number
				} else {
					null
				}
			}
			.sum()
	}
	
	override fun parseInput(input: String, step: Int): List<String> {
		return input.split("\n")
	}
	
	data class EngineNumber(
		val number: Int,
		val startX: Int,
		val startY: Int,
		val length: Int
	) {
		fun isAdjacent(operator: Coord2D): Boolean =
			abs(operator.y - startY) <= 1 &&
				operator.x >= (startX - 1) &&
				operator.x <= (startX + length )
	}
	
	object EngineNumberBuilder {
		private var startX: Int? = null
		private var startY: Int? = null
		private var number: String? = null
		
		fun checkNumber(startX: Int, startY: Int, character: String, lineSize: Int): EngineNumber? {
			if (character.matches("\\d".toRegex())) {
				if (this.number == null) {
					this.startX = startX
					this.startY = startY
					this.number = character
				} else {
					number += character
				}
				if (startX + 1 == lineSize) {
					val engineNumber = EngineNumber(this.number!!.toInt(), this.startX!!, this.startY!!, this.number!!.length)
					clear()
					return engineNumber
				}
			} else if (this.number != null && this.startX != null && this.startY != null) {
				val engineNumber = EngineNumber(this.number!!.toInt(), this.startX!!, this.startY!!, this.number!!.length)
				clear()
				return engineNumber
			}
			return null
		}
		
		fun clear() {
			this.startX = null
			this.startY = null
			this.number = null
		}
	}
	
}