package org.aoc.days

import org.aoc.data.AoC
import org.aoc.utils.Coord2D

class Day18 : AoC<List<Day18.Dig>, Int, Long>() {
    override fun part1(parsedData: List<Dig>): Int {
        return parsedData.generatePath().calculate().toInt()
    }

    override fun part2(parsedData: List<Dig>): Long {
        return parsedData.generatePath().calculate()
    }

    override fun parseInput(input: String, step: Int): List<Dig> {
        return input.split("\n").map { line ->
            val d = line.split(" ")
            if (step == 1) {
                val direction = when (d[0]) {
                    "R" -> Coord2D(1, 0)
                    "L" -> Coord2D(-1, 0)
                    "U" -> Coord2D(0, -1)
                    "D" -> Coord2D(0, 1)
                    else -> throw Error("sadf")
                }
                Dig(direction, d[1].toInt())
            } else {
                val hex = d[2].split("(#")[1].split(")")[0]
                val direction = when (hex[hex.length - 1]) {
                    '0' -> Coord2D(1, 0)
                    '2' -> Coord2D(-1, 0)
                    '3' -> Coord2D(0, -1)
                    '1' -> Coord2D(0, 1)
                    else -> throw Error("sadf")
                }
                val num = hex.substring(0, hex.length - 1)
                val l = Integer.parseInt(num, 16)
                Dig(direction, l)
            }
        }
    }

    data class Dig(
        val direction: Coord2D,
        val quantity: Int
    )

    enum class Direction(val prev: Coord2D, val next: Coord2D, val sign: Char) {
        F(Coord2D(0, -1), Coord2D(1, 0), 'F'),
        J(Coord2D(1, 0), Coord2D(0, -1), 'J'),
        L(Coord2D(0, 1), Coord2D(1, 0), 'L'),
        S(Coord2D(1, 0), Coord2D(0, 1), '7'),
        N(Coord2D(1, 0), Coord2D(1, 0), '-'),
        P(Coord2D(0, 1), Coord2D(0, 1), '|'),
        Q(Coord2D(0, 0), Coord2D(0, 0), '.');

        companion object {
            fun fromCoords(prev: Coord2D, next: Coord2D): Direction {
                return entries.firstOrNull { (it.prev == prev && it.next == next) || (it.prev == next.inv() && it.next == prev.inv()) } ?: Q
            }
        }

    }

    data class Path(
        val position: Coord2D,
    ) {
        lateinit var direction: Direction

        constructor(position: Coord2D, direction: Direction): this(position) {
            this.direction = direction
        }
    }

    private fun List<Dig>.generatePath(): MutableMap<Coord2D, Path> {
        val signs = "|FJL7"
        var position = Coord2D(0, 0)
        val path = mutableMapOf<Coord2D, Path>()
        this.forEachIndexed { x, dig ->
            lateinit var npath: Path
            if (dig.direction.y != 0) {
                (0 until dig.quantity).forEach { idx ->
                    position = position.addCoord2D(dig.direction)
                    npath = (Path(position, Direction.fromCoords(dig.direction, dig.direction)))
                    if (signs.contains(npath.direction.sign)) {
                        path[position] = npath
                    }
                }
            } else {
                position = position.addCoord2D(Coord2D(dig.direction.x * dig.quantity, 0))
                npath = Path(position, Direction.fromCoords(dig.direction, dig.direction))
                path[position] = npath
            }
            val index = if (x + 1 >= this.size) 0 else x + 1
            npath.direction = Direction.fromCoords(dig.direction, this[index].direction)
            if (signs.contains(npath.direction.sign)) {
                path[npath.position] = npath
            }
        }
        return path
    }

    private fun MutableMap<Coord2D, Path>.calculate(): Long {
        return this.values.groupBy { it.position.y }.values.fold(0L) { acc, paths ->
            val sorted = paths.sortedBy { it.position.x }
            var isInside = false
            var hasLOpening = false
            var hasFOpening = false
            var posX = sorted.first().position.x
            var insides = 0L
            sorted.forEach { path ->
                if (path.direction.sign == '|') {
                    isInside = !isInside
                    if (!isInside) {
                        insides += (path.position.x - posX) + 1
                    }
                    posX = path.position.x
                } else if (path.direction.sign == 'F') {
                    if (!isInside) posX = path.position.x
                    hasFOpening = true
                } else if (path.direction.sign == 'L') {
                    if (!isInside) posX = path.position.x
                    hasLOpening = true
                } else if (path.direction.sign == 'J') {
                    if (hasFOpening) {
                        isInside = !isInside
                        if (!isInside) {
                            insides += (path.position.x - posX) + 1
                        }
                        hasFOpening = false
                    } else if (hasLOpening) {
                        if(!isInside) {
                            insides += (path.position.x - posX) + 1
                        }
                    }
                } else if (path.direction.sign == '7') {
                    if (hasLOpening) {
                        isInside = !isInside
                        if (!isInside) {
                            insides += (path.position.x - posX) + 1
                        }
                        hasLOpening = false
                    } else if (hasFOpening) {
                        if(!isInside) {
                            insides += (path.position.x - posX) + 1
                        }
                    }
                }
            }
            acc + insides
        }
    }

    private fun MutableMap<Coord2D, Path>.print() {
        val minx = this.minBy { it.value.position.x }.value.position.x
        val maxx = this.maxBy { it.value.position.x }.value.position.x
        val miny = this.minBy { it.value.position.y }.value.position.y
        val maxy = this.maxBy { it.value.position.y }.value.position.y
        (miny .. maxy).forEach { y ->
            (minx ..maxx).forEach { x ->
                val p = Coord2D(x, y)
                if (this.containsKey(p)) print(this[p]?.direction?.sign)
                else print('.')
            }
            println()
        }
    }

}
