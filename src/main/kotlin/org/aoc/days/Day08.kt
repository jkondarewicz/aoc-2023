package org.aoc.days

import org.aoc.data.AoC
import kotlin.math.abs

class Day08: AoC<Pair<List<String>, Map<String, Day08.Node>>, Int, Long>() {
    override fun part1(parsedData: Pair<List<String>, Map<String, Day08.Node>>): Int {

        var steps = 0
        var found = false
        val directions = parsedData.first
        var currentNode = parsedData.second["AAA"]!!
        do {
            val direction = directions[steps % directions.size]
            currentNode = parsedData.second[currentNode.getNextNode(direction)]!!
            if (currentNode.name == "ZZZ") {
                found = true
            }
            steps++
        } while (!found)
        return steps
    }

    override fun part2(parsedData: Pair<List<String>, Map<String, Day08.Node>>): Long {
        val currentNodes = parsedData.second.filter { it.key[2] == 'A' }
        val reachedEnds = currentNodes.calculateStepsToReachEnds(parsedData.second, parsedData.first)
        val lcm = reachedEnds.lcm()
        return lcm
    }


    fun Map<String, Node>.calculateStepsToReachEnds(nodes: Map<String, Node>, directions: List<String>): List<Long>{
        return this.map {
            var currentNode = it.value
            var steps = 0L
            do {
                if (currentNode.name[2] == 'Z') {
                    break
                }
                currentNode = nodes[currentNode.getNextNode(directions[(steps % directions.size).toInt()])]!!
                steps++
            } while (true)
            steps
        }
    }

    override fun parseInput(input: String, step: Int): Pair<List<String>, Map<String, Day08.Node>> {

        val nodes = mutableMapOf<String, Node>()
        val directions = mutableListOf<String>()
        input.split("\n")
            .forEachIndexed { index, line ->
                if(index == 0) {
                    line.toCharArray().forEach { it.toString().let { directions.add(it) } }
                } else if (line.contains("=")) {
                    val dir = line.split(" = ")
                    val name = dir[0]
                    val goto = dir[1].split("(")[1].split(")")[0].split(", ")
                    nodes[name] = Node(name, goto[0], goto[1])
                }
            }
        return directions to nodes
    }

    data class Node(
        val name: String,
        val left: String,
        val right: String
    ) {
        fun getNextNode(direction: String) =
            if (direction == "R") right
            else left
    }

    private fun List<Long>.lcm(): Long {
        return (0 until this.size - 1).fold(this[0]) { acc, idx ->
            calculateLcm(acc, this[idx + 1])
        }
    }

    private fun calculateLcm(a: Long, b: Long): Long {
        return abs(a) * (abs(b) / calculateGCD(a, b))
    }

    fun calculateGCD(a: Long, b: Long): Long {
        var num1 = a
        var num2 = b
        while (num2 != 0L) {
            val temp = num2
            num2 = num1 % num2
            num1 = temp
        }
        return num1
    }

}