package aoc

import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

@ExperimentalTime
fun main() {
    allDays
        .flatMap {
            listOf(
                measureTimedValue { "Day ${it.day}a: ${it.a()}" },
                measureTimedValue { "Day ${it.day}b: ${it.b()}" },
            )
        }
        .forEach {
            val timeMs = "%8.${2}f".format(it.duration.toDouble(DurationUnit.MILLISECONDS))
            val result = it.value
            println("$timeMs ms\t\t$result")
        }
}
