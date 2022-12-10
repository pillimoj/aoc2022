package aoc

import java.io.File

sealed class Day(val day: Int) {
    abstract fun a(): String
    abstract fun b(): String
    val puzzleInput: List<String> = File("inputs/input$day.txt").readLines()
    val exampleInput: List<String>? = File("inputs/example$day.txt").run {
        if(exists()) readLines() else null
    }
}

val allDays = Day::class.sealedSubclasses
    .mapNotNull { it.objectInstance }
    .sortedBy { it.day }
