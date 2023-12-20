package org.aoc.data

import org.aoc.parser.FileInput
import org.aoc.runWithPerformanceCheck

abstract class AoC<T, E, F> {

    private companion object {
        val fileInput = FileInput()
    }

    val day = this::class.java.simpleName.split("Day")[1].toInt()

    fun execute(testInput: Boolean): Solution<E, F> =
        Solution(
            day,
            runWithPerformanceCheck { part1(getInput(testInput, 1)) }.also {
                println("Day$day part1 took ${it.executionTime}ms")
                println("${it.solution}")
                println()
            },
            runWithPerformanceCheck { part2(getInput(testInput, 2)) }.also {
                println("Day$day part2 took ${it.executionTime}ms")
                println("${it.solution}")
                println()
            }
        )

    private fun getInput(testInput: Boolean, step: Int) =
        parseInput(fileInput.process(this, testInput), step)

    protected abstract fun part1(parsedData: T): E
    protected abstract fun part2(parsedData: T): F
    protected abstract fun parseInput(input: String, step: Int): T
}