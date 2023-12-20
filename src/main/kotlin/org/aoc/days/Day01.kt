package org.aoc.days

import org.aoc.data.AoC

class Day01: AoC<List<String>, Int, Long>() {
    override fun part1(parsedData: List<String>): Int {
        return parsedData
            .map(this::getNum)
            .sum()
    }
    
    override fun part2(parsedData: List<String>): Long {
        return parsedData
            .map(this::getNumsWithLiteral)
            .map(this::getNum)
            .sumOf { it.toLong() }
    }
   
    private fun getNumsWithLiteral(line: String): String {
        val nums = mapOf(
            "one" to 1,
            "two" to 2,
            "three" to 3,
            "four" to 4,
            "five" to 5,
            "six" to 6,
            "seven" to 7,
            "eight" to 8,
            "nine" to 9
        )
        var a = line
        nums.forEach { (word, number) ->
            a = a.replace(word.toRegex(), word[0] + number.toString() + word[word.length - 1])
        }
        return a
    }
    
    override fun parseInput(input: String, step: Int): List<String> {
        return input.split("\n")
    }
    
    private fun getNum(line: String): Int {
        val nums = mutableListOf<Int>()
        line.forEach {
            if (it.toString().matches("\\d".toRegex())) {
                nums.add(it.toString().toInt())
            }
        }
        val num = nums[0] * 10 + nums[nums.size - 1]
        return num
    }

    data class ElfFood(
        val food: List<Int>
    ) {
        val all = food.sum()
    }

}