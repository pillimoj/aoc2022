package aoc

import java.io.File
import java.io.FileNotFoundException

sealed class Day(val day: Int) {
    abstract fun a(): String
    abstract fun b(): String
    val puzzleInput: List<String> = File("inputs/input$day.txt").readLines()
    val exampleInput: List<String>
        get() = File("inputs/example$day.txt").run {
            if (exists()) readLines() else throw FileNotFoundException(name)
        }
}

val allDays = Day::class.sealedSubclasses
    .mapNotNull { it.objectInstance }
    .sortedBy { it.day }
