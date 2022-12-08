package aoc

import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

@ExperimentalTime
fun main(args: Array<String>) {
    val dayArg = args.firstOrNull()?.toIntOrNull()
    val days = if(dayArg != null) {
        val day = allDays.find { it.day == dayArg }
        day?.run { listOf(this) } ?: emptyList()
    }
    else {
        allDays
    }
    days
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
