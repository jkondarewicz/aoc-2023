package org.aoc.days

import org.aoc.data.AoC
import org.aoc.utils.Coord2D

class Day17 : AoC<List<Day17.Graph>, Int, Int>(){
    override fun part1(parsedData: List<Graph>): Int {
        val minx = 0
        val miny = 0
        val maxx = parsedData.maxBy { it.coord.x }.coord.x
        val maxy = parsedData.maxBy { it.coord.y }.coord.y
        val distances = mutableMapOf<Coord2D, Int>()
        (minx until maxx).forEach { x ->
            (miny until maxy).forEach { y ->
                distances[Coord2D(x, y)] = Int.MAX_VALUE
            }
        }
        (minx until maxx step 3).forEach { x ->
            (miny until maxy step 3).forEach { y ->

            }
        }
        TODO("Not yet implemented")
    }

    override fun part2(parsedData: List<Graph>): Int {
        TODO("Not yet implemented")
    }

    override fun parseInput(input: String, step: Int): List<Graph> {
        return input.split("\n").mapIndexed { y, line ->
            line.mapIndexed { x, weight ->
                Graph(Coord2D(x, y), weight.toString().toInt())
            }
        }.flatten()
    }

    data class Graph(
        val coord: Coord2D,
        val weight: Int
    )

}