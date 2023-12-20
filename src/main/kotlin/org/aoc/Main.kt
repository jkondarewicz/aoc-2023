package org.aoc
import org.aoc.data.AoC
import org.aoc.days.*

fun main() {
    val testInput = false
    val allCheck = runWithPerformanceCheck {
        listOf<AoC<*, *, *>>(
//            Day01(),
//            Day02(),
//            Day03(),
//            Day04(),
//            Day08(),
//            Day09(),
//            Day10(),
//            Day11(),
//            Day12(),
//            Day13(),
//            Day14()
//            Day15()
//            Day16()
//            Day17()
//            Day18()
            Day19()
        )
            .forEach { dailyTask ->
                val executedDailyTask = runWithPerformanceCheck { dailyTask.execute(testInput) }
                println("Day${dailyTask.day} full execution time: ${executedDailyTask.executionTime}ms")
            }
    }
    println("All days took: ${allCheck.executionTime}ms")
}
