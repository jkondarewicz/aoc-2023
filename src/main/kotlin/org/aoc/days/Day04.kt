package org.aoc.days

import org.aoc.data.AoC
import org.aoc.toward

class Day04: AoC<List<Day04.Hand>, Int, Int>() {

    data class Hand(
         val index: Int,
         val winningNumbers: List<Int>,
         val chosenNums: List<Int>
    )

    override fun part1(parsedData: List<Hand>): Int {
        return parsedData.map { hand ->
            val wins = hand.winningNumbers.associateWith { true }
            val correct = hand.chosenNums.filter { wins.containsKey(it) }.size - 1
            Math.pow(2.0, correct.toDouble()).toInt()
        }
            .sum()
    }

    override fun part2(parsedData: List<Hand>): Int {
        val instances = mutableMapOf<Int, Int>()
        parsedData.forEachIndexed { idx, hand ->
            instances[idx] = 1
        }
        parsedData.forEachIndexed { idx, hand ->
            val wins = hand.winningNumbers.associateWith { true }
            val correct = hand.chosenNums.filter { wins.containsKey(it) }.size
            (idx + 1 .. idx + correct).forEach { index ->
                if (instances.containsKey(index)) {
                    instances[index] = instances[index]!! + instances[idx]!!
                } else {
                    instances[index] = instances[idx]!!
                }
            }
        }
        return instances.map { it.value }.sum()
    }

    override fun parseInput(input: String, step: Int): List<Hand> =
        input
            .split("\n")
            .mapIndexed { idx, line ->
                val nums = line.split(": ")[1].split(" | ")
                val winning = nums[0].split(" ").mapNotNull { if (it.matches("\\d+".toRegex())) it.toInt() else null }
                val numbers = nums[1].split(" ").mapNotNull { if (it.matches("\\d+".toRegex())) it.toInt() else null }
                Hand(idx + 1, winning, numbers)
            }
}