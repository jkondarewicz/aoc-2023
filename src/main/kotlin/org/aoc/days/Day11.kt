package org.aoc.days

import org.aoc.data.AoC
import kotlin.math.abs

class Day11 : AoC<Day11.Galaxies, Long, Long>() {
    override fun part1(parsedData: Galaxies): Long {
        return calculateDistance(parsedData, 2)
    }

    private fun Coord2D.plus(second: Coord2D) =
        Coord2D(x + second.x, y + second.y)

    private fun Coord2D.dist(second: Coord2D) =
        abs(y - second.y) + abs(x - second.x)

    override fun part2(parsedData: Galaxies): Long {
        return calculateDistance(parsedData, 1000000)
    }

    private fun calculateDistance(parsedData: Galaxies, galaxyExpandMultiplier: Long): Long {
        val galaxiesY = parsedData.galaxies.map { it.coord2D.y }.toSet()
        val galaxiesX = parsedData.galaxies.map { it.coord2D.x }.toSet()
        val missingGalaxiesY = (0 .. parsedData.height).filter { !galaxiesY.contains(it.toLong()) }
        val missingGalaxiesX = (0 .. parsedData.width).filter { !galaxiesX.contains(it.toLong()) }
        missingGalaxiesX.forEach {
            parsedData.galaxies.forEach { galaxy ->
                if (galaxy.coord2D.x > it) {
                    galaxy.offset = Coord2D(galaxy.offset.x + galaxyExpandMultiplier - 1, galaxy.offset.y)
                }
            }
        }
        missingGalaxiesY.forEach {
            parsedData.galaxies.forEach { galaxy ->
                if (galaxy.coord2D.y > it) {
                    galaxy.offset = Coord2D(galaxy.offset.x, galaxy.offset.y + galaxyExpandMultiplier - 1)
                }
            }
        }
        val galaxies = parsedData.galaxies.toList()
        return galaxies.indices.sumOf { firstIdx ->
            val first = galaxies[firstIdx]
            (firstIdx + 1 until galaxies.size).sumOf {
                val second = galaxies[it]
                val dist = first.coord2D.plus(first.offset).dist(second.coord2D.plus(second.offset))
                dist
            }
        }
    }
    override fun parseInput(input: String, step: Int): Galaxies {
        val lines = input.split("\n")
        val height = lines.size - 1
        val width = lines[0].length - 1
        val galaxies = mutableSetOf<Galaxy>()
        lines.forEachIndexed { y, line ->
            line.forEachIndexed { x, tile ->
                if (tile == '#') {
                    galaxies.add(Galaxy(Coord2D(x.toLong(), y.toLong())))
                }
            }
        }
        return Galaxies(width, height, galaxies)
    }

    data class Galaxies(
        val width: Int,
        val height: Int,
        val galaxies: Set<Galaxy>
    )

    data class Galaxy(
        val coord2D: Coord2D,
        var offset: Coord2D = Coord2D(0, 0)
    )

    data class Coord2D(
        val x: Long,
        val y: Long
    )

}