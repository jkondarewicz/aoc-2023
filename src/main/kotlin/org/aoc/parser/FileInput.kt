package org.aoc.parser

import org.aoc.data.AoC
import java.nio.file.Files
import java.nio.file.Path

class FileInput {

    fun<T: AoC<*, *, *>> process(aocDay: T, testInput: Boolean): String =
        Files.readString(
            Path.of("src", "main", "resources", "day_${aocDay.day}", testInput.fileName)
        )

    private val Boolean.fileName: String
        get() = if(this) "test" else "real"

}