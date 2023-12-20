package org.aoc.days

import org.aoc.data.AoC
import java.lang.Error
import kotlin.math.abs

class Day13 : AoC<List<List<List<Char>>>, Long, Long>(){
    override fun part1(parsedData: List<List<List<Char>>>): Long {
        return parsedData.sumOf { it.getValue(false).toLong() }
    }

    override fun part2(parsedData: List<List<List<Char>>>): Long {
        return parsedData.sumOf { it.getValue(true).toLong() }
    }

    override fun parseInput(input: String, step: Int): List<List<List<Char>>> {
        val maps = mutableListOf<List<List<Char>>>()
        var map = mutableListOf<List<Char>>()
        input.split("\n").forEach { line ->
            if (line.isEmpty()) {
                maps.add(map)
                map = mutableListOf<List<Char>>()
            } else {
                val tiles = mutableListOf<Char>()
                line.forEach { tile ->
                    tiles.add(tile)
                }
                map.add(tiles)
            }
        }
        if (map.isNotEmpty()) {
            maps.add(map)
        }
        return maps
    }


    private fun List<List<Char>>.getValue(detectSmudge: Boolean): Int {
        val colReflections = this.colReflections(detectSmudge)
        val rowReflections = this.rowReflections(detectSmudge)
        val c = colReflections.firstOrNull { it.reached && (!detectSmudge || it.smudgeDetected) }
        val r = rowReflections.firstOrNull { it.reached && (!detectSmudge || it.smudgeDetected) }
        return c?.value ?: r?.value?.times(100) ?: throw Error("you shall not pass")
    }

    private fun List<List<Char>>.colReflections(detectSmudge: Boolean = false): List<Reflection> {
        return (0 until this[0].size - 1).map { colIdx ->
            val reflection = Reflection(0, colIdx)
            var offset = 0
            do {
                val p = colIdx - offset
                val n = colIdx + offset + 1
                val matching = indices.filter {
                    this[it][p] == this[it][n]
                }.size
                var found = matching == this.size
                if (detectSmudge && !reflection.smudgeDetected && !found) {
                    found = abs(matching - this.size) == 1
                    if (found)
                        reflection.detected()
                }
                if (found && (p == 0 || n == this[0].size - 1))
                    reflection.reachedEnd()
                if (found)
                    offset++
            } while (found && p > 0 && n < this[0].size - 1)
            reflection.setQuantityAndGet(offset)
        }
    }
    private fun List<List<Char>>.rowReflections(detectSmudge: Boolean = false): List<Reflection> {
        return (0 until this.size - 1).map { rowIdx ->
            val reflection = Reflection(0, rowIdx)
            var offset = 0
            do {
                val p = rowIdx - offset
                val n = rowIdx + offset + 1
                val matching = this[0].indices.filter {
                    this[p][it] == this[n][it]
                }.size
                var found = matching == this[0].size
                if (detectSmudge && !reflection.smudgeDetected && !found) {
                    found = abs(matching - this[0].size) == 1
                    if (found) {
                        reflection.detected()
                    }
                }
                if (found && (p == 0 || n == this.size - 1))
                    reflection.reachedEnd()
                if(found)
                    offset++
            } while (found && p > 0 && n < this.size - 1)
            reflection.setQuantityAndGet(offset )
        }
    }


    data class Reflection(var quantity: Int, val idx: Int) {
        var reached = false
        var smudgeDetected = false
        constructor( idx: Int): this(0, idx)

        val value: Int
            get() = idx + 1
        fun setQuantityAndGet(quantity: Int): Reflection {
            this.quantity = quantity
            return this
        }

        fun detected() {
            smudgeDetected = true
        }
        fun reachedEnd(): Reflection {
            reached = true
            return this
        }
    }

}
