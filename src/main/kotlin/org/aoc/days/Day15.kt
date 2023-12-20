package org.aoc.days

import org.aoc.data.AoC

class Day15: AoC<List<String>, Long, Long>() {
    override fun part1(parsedData: List<String>): Long {
        return parsedData.sumOf { it.calculateHash() }
    }

    override fun part2(parsedData: List<String>): Long {
        val boxes = (0..255).associateWith { Box(it) }
        parsedData.forEach {
            if (it.contains("-")) {
                val label = it.split("-")[0]
                val hash = label.calculateHash()
                boxes[hash.toInt()]?.labels?.remove(Label(label))
            } else if (it.contains("=")) {
                val values = it.split("=")
                val label = values[0]
                val value = values[1].toInt()
                val hash = label.calculateHash()
                val box = boxes[hash.toInt()]!!
                if (box.labels.contains(Label(label))) {
                    box.labels.first { it == Label(label) }.value = value
                } else {
                    box.labels.add(Label(label, value))
                }
            }
        }
        return boxes.toList().fold(0) { acc, data ->
            val box = data.second
            if (box.labels.isNotEmpty()) {
                val value = box.labels.foldIndexed(0) { index, value, label ->
                    value + (box.hash + 1) * (index + 1) * label.value
                }
                acc +  value
            } else {
                acc
            }
        }
    }

    fun String.calculateHash(idx: Int = 0, value: Long = 0): Long {
        if (idx >= this.length) return value
        val c: Int = this[idx].code
        return this.calculateHash(idx + 1, ((value + c) * 17) % 256)
    }

    override fun parseInput(input: String, step: Int): List<String> {
        return input.split(",")
    }

    data class Box(
        val hash: Int,
        val labels: MutableSet<Label>
    ) {
        constructor(hash: Int): this(hash, mutableSetOf())
    }

    data class Label(
        val name: String
    ) {
        var value: Int = 0
        constructor(name: String, value: Int): this(name) {
            this.value = value
        }
    }
}