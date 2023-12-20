package org.aoc

infix fun Int.toward(to: Int): IntProgression {
    val step = if (this > to) -1 else 1
    return IntProgression.fromClosedRange(this, to, step)
}

data class PerformanceResult<T>(
    val executionTime: Long,
    val solution: T
)

fun<T> runWithPerformanceCheck(block: () -> T): PerformanceResult<T> {
    val start = System.currentTimeMillis()
    val response = block()
    val end = System.currentTimeMillis()
    return PerformanceResult(end - start, response)
}
