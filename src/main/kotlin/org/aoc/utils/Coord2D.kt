package org.aoc.utils

data class Coord2D(
	val x: Int,
	val y: Int
) {
	fun addCoord2D(coord: Coord2D) =
		Coord2D(x + coord.x, y + coord.y)

	fun inv(): Coord2D =
		Coord2D(x * -1, y * -1)

	fun adjacent() =
		listOf(
			Coord2D(x - 1, y),
			Coord2D(x + 1, y),
			Coord2D(x, y - 1),
			Coord2D(x, y + 1)
		)

}