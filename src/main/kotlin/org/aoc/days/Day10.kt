package org.aoc.days

import org.aoc.data.AoC
import org.aoc.utils.Coord2D
import java.lang.Error
import kotlin.math.abs

class Day10 : AoC<Pair<List<String>, List<Day10.MapTile>>, Int, Int>() {


    data class MapTile(
        val coord2D: Coord2D,
        var next: MapTile?,
        var prev: MapTile?,
        var startPosition: Boolean = false,
        var pipe: Char? = null
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as MapTile

            return coord2D == other.coord2D
        }

        override fun hashCode(): Int {
            return coord2D.hashCode()
        }

        override fun toString(): String {
            return "MapTile(coord2D=$coord2D)"
        }

    }

    override fun part1(data: Pair<List<String>, List<Day10.MapTile>>): Int {
        val parsedData = data.second
        var startPosition = parsedData.first { it.startPosition }
        var tiles = parsedData.filter { it.next?.startPosition == true || it.prev?.startPosition == true }
        val distances = mutableMapOf(startPosition to 0)
        var distance = 1
        do {
            val first = tiles[0]
            val second = tiles[1]
            distances[first] = distance
            distances[second] = distance

            val nd = mutableListOf<MapTile>()
            if (first.next != null && !distances.containsKey(first.next)) {
                nd.add(first.next!!)
            } else if (first.prev != null && !distances.containsKey(first.prev)) {
                nd.add(first.prev!!)
            }

            if (second.next != null && !distances.containsKey(second.next)) {
                nd.add(second.next!!)
            } else if (second.prev != null && !distances.containsKey(second.prev)) {
                nd.add(second.prev!!)
            }
            if (nd.size != 2) {
                break
            }
            tiles = nd
            distance++
        } while (true)
        return distance
    }

    override fun part2(parsedData: Pair<List<String>, List<Day10.MapTile>>): Int {
        var lockedInside = 0
        val loopTiles = parsedData.second.findLoopTiles()
        parsedData.first.forEachIndexed { y, row ->
            var isInside = false
            var hasLOpening = false
            var hasFOpening = false
            row.forEachIndexed { x, tile ->
                val contains = loopTiles.contains(Coord2D(x, y))
                if (tile == '|' && contains) {
                    isInside = !isInside
                } else if (tile == 'L' && contains) {
                    hasLOpening = true
                } else if (tile == 'F' && contains) {
                    hasFOpening = true
                } else if (tile == 'J' && contains) {
                    if (hasFOpening) isInside = !isInside
                    hasLOpening = false
                    hasFOpening = false
                } else if (tile == '7' && contains) {
                    if (hasLOpening) isInside = !isInside
                    hasFOpening = false
                    hasLOpening = false
                } else if (!contains && isInside) {
                    lockedInside++
                }
            }
        }
        return lockedInside
    }

    private fun List<MapTile>.findLoopTiles(): Map<Coord2D, Char> {
        val adjacent = this.filter { it.next?.startPosition == true || it.prev?.startPosition == true }
        val startPosition = this.first { it.startPosition }
        val distance = distance(adjacent[0].coord2D, startPosition.coord2D)
        val distance2 = distance(adjacent[1].coord2D, startPosition.coord2D)
        startPosition.apply {
            pipe = getPipe(distance, distance2)
        }
        val loopTiles = mutableMapOf(startPosition.coord2D to startPosition.pipe!!)
        var tile = adjacent[0]
        do {
            loopTiles[tile.coord2D] = tile.pipe!!

            tile = if (!loopTiles.containsKey(tile.next!!.coord2D)) {
                tile.next!!
            } else if (!loopTiles.containsKey(tile.prev!!.coord2D)) {
                tile.prev!!
            } else {
                break
            }
        } while(true)
        return loopTiles.toMap()
    }

    private fun getPipe(distance: Coord2D, distance2: Coord2D): Char {
        return when {
            abs(distance.y) - abs(distance2.y) == 0 -> '|'
            abs(distance.x) - abs(distance2.x) == 0 -> '-'
            (distance.x == 1 && distance2.y == -1) || (distance2.x == 1 && distance.y == -1) -> 'L'
            (distance.x == 1 && distance2.y == 1) || (distance2.x == 1 && distance.y == 1) -> 'F'
            (distance.x == -1 && distance2.y == -1) || (distance2.x == -1 && distance.y == -1) -> 'J'
            (distance.x == -1 && distance2.y == 1) || (distance2.x == -1 && distance.y == 1) -> '7'
            else -> throw Error("")
        }
    }

    private fun distance(point: Coord2D, secondPoint: Coord2D): Coord2D {
        return Coord2D(point.x - secondPoint.x, point.y - secondPoint.y)
    }

    override fun parseInput(input: String, step: Int): Pair<List<String>, List<Day10.MapTile>> {
        val allTiles = mutableMapOf<Coord2D, MapTile>()
        val tiles = mutableListOf<MapTile>()
        input.split("\n").forEachIndexed { y, line ->
            line.forEachIndexed { x, tile ->
                var nextTile: MapTile? = null
                var prevTile: MapTile? = null
                var startPosition = false
                when (tile) {
                    '|' -> {
                        nextTile = allTiles.getOrPut(Coord2D(x, y + 1)) { MapTile(Coord2D(x, y + 1), null, null) }
                        prevTile = allTiles.getOrPut(Coord2D(x, y - 1)) { MapTile(Coord2D(x, y - 1),null, null)}
                    }
                    '-' -> {
                        nextTile = allTiles.getOrPut(Coord2D(x + 1, y)) { MapTile(Coord2D(x + 1, y),null, null) }
                        prevTile = allTiles.getOrPut(Coord2D(x - 1, y)) { MapTile(Coord2D(x - 1, y),null, null)}
                    }
                    'L' -> {
                        nextTile = allTiles.getOrPut(Coord2D(x + 1, y)) { MapTile(Coord2D(x + 1, y),null, null) }
                        prevTile = allTiles.getOrPut(Coord2D(x, y - 1)) { MapTile(Coord2D(x, y - 1),null, null)}
                    }
                    'J' -> {
                        nextTile = allTiles.getOrPut(Coord2D(x - 1, y)) { MapTile(Coord2D(x - 1, y),null, null) }
                        prevTile = allTiles.getOrPut(Coord2D(x , y - 1)) { MapTile(Coord2D(x, y - 1),null, null)}
                    }
                    '7' -> {
                        nextTile = allTiles.getOrPut(Coord2D(x - 1, y)) { MapTile(Coord2D(x - 1, y),null, null) }
                        prevTile = allTiles.getOrPut(Coord2D(x, y + 1)) { MapTile(Coord2D(x, y + 1),null, null)}
                    }
                    'F' -> {
                        nextTile = allTiles.getOrPut(Coord2D(x + 1, y)) { MapTile(Coord2D(x + 1, y),null, null) }
                        prevTile = allTiles.getOrPut(Coord2D(x , y + 1)) { MapTile(Coord2D(x, y + 1),null, null)}
                    }
                    '.' -> {
                    }
                    'S' -> {
                        startPosition = true
                    }
                    else -> throw Error("Wrong tile")
                }
                val mapTile = allTiles.getOrDefault(Coord2D(x, y), MapTile(Coord2D(x, y), null, null, startPosition) )

                allTiles[Coord2D(x, y)] = mapTile.apply {
                    this.pipe = tile
                    this.next = nextTile
                    this.prev = prevTile
                    this.startPosition = startPosition
                }
                if ( startPosition || nextTile != null || prevTile != null ) {
                    tiles.add(mapTile)
                }
            }
        }
        return input.split("\n") to tiles
    }
}
