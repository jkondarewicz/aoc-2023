package org.aoc.days

import org.aoc.data.AoC
import org.aoc.utils.Coord2D

class Day14 : AoC<String, Long, Long>() {
    override fun part1(parsedData: String): Long {
        val lines = parsedData.split("\n")
        val width = lines[0].length
        return (0 until width).sumOf { x ->
            var freeSpace = 0
            lines.mapIndexedNotNull { idx, line ->
                val tile = line[x]
                if (tile == 'O') {
                    val weight = lines.size - freeSpace
                    freeSpace++
                    weight
                } else if (tile == '#') {
                    freeSpace = idx + 1
                    null
                } else {
                    null
                }
            }.sum().toLong()
        }
    }

    override fun part2(parsedData: String): Long {
        val obstacles = mutableMapOf<Coord2D, Boolean>()
        var rocks = mutableMapOf<Coord2D, Boolean>()
        parsedData.split("\n").forEachIndexed { y, line ->
            line.forEachIndexed { x, tile ->
                if (tile == '#') obstacles[Coord2D(x, y)] = true
                else if (tile == 'O') rocks[Coord2D(x, y)] = true
            }
        }
        val i = parsedData.split("\n")
        val l = mutableListOf<Int>()
        val cycleStartIdx = 200
        repeat(300) { cycle ->
            rocks = rocks.cycle(obstacles, i)
            if(cycle >= cycleStartIdx) {
                val weight = calculateWeight(rocks, i.size)
                l.add(weight)
            }
        }
        val cycle = findCycle(l)
        return cycle[(1000000000 - cycleStartIdx - 1) % cycle.size].toLong()
    }

    fun findCycle(l: List<Int>): List<Int> {
        val cycleQuantity = (5 until l.size / 2).first { quantity ->
            (0 until quantity).all {
                l[it] == l[quantity + it]
            }
        }
        return l.subList(0, cycleQuantity)
    }

    private fun calculateWeight(rocks: MutableMap<Coord2D, Boolean>, height: Int): Int {
        return rocks.map { rock -> height - rock.key.y }.sum()
    }

    private fun draw(rocks: MutableMap<Coord2D, Boolean>, obstacles: MutableMap<Coord2D, Boolean>, width: Int, height: Int) {
        (0 until height).forEach { y ->
            (0 until width).forEach { x ->
                val pos = Coord2D(x, y)
                if (rocks.containsKey(pos)) {
                    print('O')
                } else if (obstacles.containsKey(pos)) {
                    print('#')
                } else {
                    print('.')
                }
            }
            print("\n")
        }
        println()
    }

    private fun Map<Coord2D, Boolean>.cycle(obstacles: Map<Coord2D, Boolean>, input: List<String>): MutableMap<Coord2D, Boolean> {
        return this.north(obstacles, input).west(obstacles, input).south(obstacles, input).east(obstacles, input)
    }

    private fun Map<Coord2D, Boolean>.north(obstacles: Map<Coord2D, Boolean>, input: List<String>): MutableMap<Coord2D, Boolean> {
        val width = input[0].length
        val height = input.size
        val rocks = this.toMutableMap()
        (0 until width).forEach { x ->
            var free = 0
            (0 until height).forEach { y ->
                val pos = Coord2D(x, y)
                if (rocks.containsKey(pos)) {
                    rocks.remove(pos)
                    rocks[Coord2D(x, free)] = true
                    free++
                } else if (obstacles.containsKey(pos)) {
                    free = y + 1
                }
            }
        }
        return rocks
    }

    private fun Map<Coord2D, Boolean>.west(obstacles: Map<Coord2D, Boolean>, input: List<String>): MutableMap<Coord2D, Boolean> {
        val width = input[0].length
        val height = input.size
        val rocks = this.toMutableMap()
        (0 until height).forEach { y ->
            var free = 0
            (0 until width).forEach { x ->
                val pos = Coord2D(x, y)
                if (rocks.containsKey(pos)) {
                    rocks.remove(pos)
                    rocks[Coord2D(free, y)] = true
                    free++
                } else if (obstacles.containsKey(pos)) {
                    free = x + 1
                }
            }
        }
        return rocks
    }

    private fun Map<Coord2D, Boolean>.south(obstacles: Map<Coord2D, Boolean>, input: List<String>): MutableMap<Coord2D, Boolean> {
        val width = input[0].length
        val height = input.size
        val rocks = this.toMutableMap()
        (0 until width).forEach { x ->
            var free = height - 1
            (height - 1 downTo 0).forEach { y ->
                val pos = Coord2D(x, y)
                if (rocks.containsKey(pos)) {
                    rocks.remove(pos)
                    rocks[Coord2D(x, free)] = true
                    free--
                } else if (obstacles.containsKey(pos)) {
                    free = y - 1
                }
            }
        }
        return rocks
    }

    private fun Map<Coord2D, Boolean>.east(obstacles: Map<Coord2D, Boolean>, input: List<String>): MutableMap<Coord2D, Boolean> {
        val width = input[0].length
        val height = input.size
        val rocks = this.toMutableMap()
        (0 until height).forEach { y ->
            var free = width - 1
            (width - 1 downTo 0).forEach { x ->
                val pos = Coord2D(x, y)
                if (rocks.containsKey(pos)) {
                    rocks.remove(pos)
                    rocks[Coord2D(free, y)] = true
                    free--
                } else if (obstacles.containsKey(pos)) {
                    free = x - 1
                }
            }
        }
        return rocks
    }
    override fun parseInput(input: String, step: Int): String {
        return input
    }
}