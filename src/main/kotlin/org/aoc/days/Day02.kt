package org.aoc.days

import org.aoc.data.AoC

class Day02: AoC<List<Day02.Cubes>, Int, Long>() {
    
    val maxCubes = Cubes(12, 13, 14)
    
    override fun part1(parsedData: List<Cubes>): Int {
        return parsedData
            .foldIndexed(0) { idx, acc, cubes ->
                if (cubes.red <= maxCubes.red && cubes.green <= maxCubes.green && cubes.blue <= maxCubes.blue) {
                    acc + idx + 1
                } else {
                    acc
                }
            }
    }
    
    override fun part2(parsedData: List<Cubes>): Long {
        return parsedData
            .fold(0L) { acc, cubes ->
                val power = cubes.red * cubes.green * cubes.blue
                acc + power
            }
    }
   

    override fun parseInput(input: String, step: Int): List<Cubes> {
        return input
            .split("\n")
            .map { line ->
                var cubes = Cubes(0, 0, 0)
                val allCubes = line.split(": ")[1].split("; ")
                allCubes.forEach {
                    it.split(", ").forEach { cube ->
                        val cubeInfo = cube.split(" ")
                        val quantity = cubeInfo[0].toInt()
                        val color = cubeInfo[1]
                        when (color) {
                            "red" -> cubes = cubes.checkRed(quantity)
                            "green" -> cubes = cubes.checkGreen(quantity)
                            "blue" -> cubes = cubes.checkBlue(quantity)
                        }
                    }
                }
                cubes
            }
    }
    
    data class Cubes(
        val red: Int,
        val green: Int,
        val blue: Int
    ) {
        fun checkRed(red: Int) = if (red > this.red) Cubes(red, green, blue) else this
        fun checkGreen(green: Int) = if (green > this.green) Cubes(red, green, blue) else this
        fun checkBlue(blue: Int) = if (blue > this.blue) Cubes(red, green, blue) else this
    }

}