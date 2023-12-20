package org.aoc.days

import org.aoc.data.AoC
import org.aoc.utils.Coord2D
import kotlin.math.max

class Day16 : AoC<Pair<Coord2D, Map<Coord2D, Day16.Mirror>>, Long, Long>(){

    data class Bean(var position: Coord2D, var direction: Direction)
    enum class Direction(val coord2D: Coord2D) {
        R(Coord2D(1, 0)), L(Coord2D(-1, 0)), U(Coord2D(0, 1)), D(Coord2D(0, -1))
    }

    enum class Mirror(val sign: String, val r: List<Direction>, val l: List<Direction>, val u: List<Direction>, val d: List<Direction>) {
        NONE(".", listOf(Direction.R), listOf(Direction.L), listOf(Direction.U), listOf(Direction.D)),
        MIRROR_1("/", listOf(Direction.D), listOf(Direction.U), listOf(Direction.L), listOf(Direction.R)),
        MIRROR_2("\\", listOf(Direction.U), listOf(Direction.D), listOf(Direction.R), listOf(Direction.L)),
        SPLITTER_1("-", listOf(Direction.R), listOf(Direction.L), listOf(Direction.R, Direction.L), listOf(Direction.L, Direction.R)),
        SPLITTER_2("|", listOf(Direction.U, Direction.D), listOf(Direction.U, Direction.D), listOf(Direction.U), listOf(Direction.D));

        companion object {
            fun fromCharacter(character: Char): Mirror =
                entries.firstOrNull { it.sign == character.toString() } ?: Mirror.NONE
        }

        fun getDirection(direction: Direction): List<Direction> {
            return when (direction) {
                Direction.R -> r
                Direction.L -> l
                Direction.U -> u
                Direction.D -> d
            }
        }

    }

    private fun inrange(bean: Coord2D, range: Coord2D) =
        bean.x >= 0 && bean.x < range.x && bean.y >= 0 && bean.y < range.y

    override fun part1(parsedData: Pair<Coord2D, Map<Coord2D, Day16.Mirror>>): Long {
        return calculateEnergized(parsedData, Bean(Coord2D(0, 0), Direction.R))
    }

    override fun part2(parsedData: Pair<Coord2D, Map<Coord2D, Day16.Mirror>>): Long {
        return max(
            (0 until parsedData.first.x).map { calculateEnergized(parsedData, Bean(Coord2D(it, 0), Direction.U)) }.max(),
            (0 until parsedData.first.y).map { calculateEnergized(parsedData, Bean(Coord2D(0, it), Direction.R)) }.max(),
            (0 until parsedData.first.y).map { calculateEnergized(parsedData, Bean(Coord2D(parsedData.first.x - 1, it), Direction.L)) }.max(),
            (0 until parsedData.first.x).map { calculateEnergized(parsedData, Bean(Coord2D(it, parsedData.first.y - 1), Direction.D)) }.max()

        )
    }

    fun max(vararg values: Long): Long {
        return values.max()
    }

    fun calculateEnergized(parsedData: Pair<Coord2D, Map<Coord2D, Day16.Mirror>>, startPosition: Bean): Long {
        val energized = mutableSetOf<Coord2D>(Coord2D(0, 0))
        var beans = mutableSetOf(startPosition)
        val visited = mutableSetOf<Bean>()
        visited.add(startPosition)
        while (true) {
            beans = beans.filter { inrange(it.position, parsedData.first) }.toMutableSet()
            val nb = mutableSetOf<Bean>()
            val s = visited.size
            beans.forEach { bean ->
                val mirror = parsedData.second[bean.position]!!
                energized.add(bean.position)
                val ng = mutableSetOf<Bean>()
                ng.addAll(mirror.getDirection(bean.direction).map { Bean(bean.position, it) })
                ng.forEach { nbean ->
                    nbean.position = nbean.position.addCoord2D(nbean.direction.coord2D)
                    if (inrange(nbean.position, parsedData.first)) {
                        visited.add(nbean)
                        energized.add(nbean.position)
                    }
                }
                nb.addAll(ng)
            }
            beans = nb
            if (visited.size == s) {
                break
            }
        }
        return energized.size.toLong()
    }
    override fun parseInput(input: String, step: Int): Pair<Coord2D, Map<Coord2D, Day16.Mirror>> {
        return input.split("\n").let { lines ->
            val mirrors = mutableMapOf<Coord2D, Mirror>()
            lines.forEachIndexed { y, line ->
                line.forEachIndexed { x, c ->
                    mirrors[Coord2D(x, y)] = Mirror.fromCharacter(c)
                }
            }
            Coord2D(lines[0].length, lines.size) to mirrors
        }
    }
}