package org.aoc.days

import org.aoc.data.AoC

class Day12 : AoC<List<Pair<String, String>>, Long, Long>() {

    override fun part1(parsedData: List<Pair<String, String>>): Long {
        return parsedData.sumOf { it.getMatchingPatternsQuantity() }
    }

    override fun part2(parsedData: List<Pair<String, String>>): Long {
//        return parsedData.sumOf { it.getMatchingPatternsQuantity() }
        return 1L
    }

    private fun Pair<String, String>.getMatchingPatternsQuantity(): Long {
        return this.first.getPatterns(this.second).size.toLong()
    }

    private fun String.getPatterns(pattern: String): List<String> {
        return if ( this.contains("?") ) {
            listOf(this.replaceFirst('?', '.').getPatterns(pattern), this.replaceFirst('?', '#').getPatterns(pattern)).flatten()
        } else {
            val text = this.normalize().split(".").mapNotNull { if (it.isNotEmpty()) it.length else null }.joinToString(",")
            if (text == pattern) listOf(pattern) else listOf()
        }
    }

    private fun String.normalize(): String =
        this.replace("\\.{2,}".toRegex(), ".")

    override fun parseInput(input: String, step: Int): List<Pair<String, String>> {
        return input.split("\n").map { line ->
            val d = line.split(" ")
            if (step == 1)
                d[0] to d[1]
            else
                "${d[0]}?${d[0]}?${d[0]}?${d[0]}?${d[0]}" to "${d[1]},${d[1]},${d[1]},${d[1]},${d[1]}"
        }
    }
}