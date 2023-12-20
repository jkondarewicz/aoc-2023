package org.aoc.days

import org.aoc.data.AoC

class Day09: AoC<List<List<Int>>, Int, Int>(){
    override fun part1(parsedData: List<List<Int>>): Int {
        return parsedData.sumOf { it.extrapolate() }
    }

    override fun part2(parsedData: List<List<Int>>): Int {
        return parsedData.sumOf { it.extrapolateBackward() }
    }

    private fun List<Int>.extrapolate(): Int {
        var searchingDiffsFor = this
        val lastNums = mutableListOf<Int>(this[this.size - 1])
        do {
            var diffZero = true
            val diffs = (1 until searchingDiffsFor.size ).map {
                val diff = searchingDiffsFor[it] - searchingDiffsFor[it - 1]
                if (diff != 0) diffZero = false
                diff
            }
            searchingDiffsFor = diffs
            lastNums.add(searchingDiffsFor[searchingDiffsFor.size - 1])
        } while (!diffZero)
        return lastNums.sum()
    }

    private fun  List<Int>.extrapolateBackward(): Int {
        var searchingDiffsFor = this
        val firstNums = mutableListOf(this[0])
        do {
            var diffZero = true
            val diffs = (1 until searchingDiffsFor.size ).map {
                val diff = searchingDiffsFor[it] - searchingDiffsFor[it - 1]
                if (diff != 0) diffZero = false
                diff
            }
            searchingDiffsFor = diffs
            firstNums.add(searchingDiffsFor[0])
        } while (!diffZero)
        return firstNums.extrapolateBackwards(0, firstNums.size - 2)
    }

    private fun List<Int>.extrapolateBackwards(diffNum: Int, index: Int): Int {
        val prevNum = this[index] - diffNum
        return if (index == 0) prevNum else this.extrapolateBackwards(prevNum, index - 1)
    }
    override fun parseInput(input: String, step: Int): List<List<Int>> {
        return input
            .split("\n")
            .map { it.split(" ").map { it.toInt() } }
    }


}