package aoc

import kotlin.math.max
import kotlin.math.min

object Fourteen : Day(14) {

    fun parseInput(input: List<String>): MutableSet<Coord> {
        val result = mutableSetOf<Coord>()
        input.forEach { line ->
            line
                .split(" -> ")
                .map { coordStr ->
                    val (x, y) = coordStr.split(',').map(String::toInt)
                    Coord(x, y)
                }
                .windowed(2)
                .forEach { (start, end) ->
                    when {
                        start.x == end.x -> {
                            for (y in min(start.y, end.y)..max(start.y, end.y)) {
                                result.add(Coord(start.x, y))
                            }
                        }
                        start.y == end.y -> {
                            for (x in min(start.x, end.x)..max(start.x, end.x)) {
                                result.add(Coord(x, start.y))
                            }
                        }
                    }
                }
        }
        return result
    }

    class Cave(val coords: MutableSet<Coord>) {
        val bottom = coords.maxOf { it.y }
        val sandEntryPos = Coord(500, 0)
        var amountOfSand = 0
        val directionsToSearch = listOf<Direction>(Direction.U, Direction.UL, Direction.UR)

        fun runSand() {
            var finished = false
            while (!finished) {
                var currentSandPos = sandEntryPos
                while (true) {
                    val nextFree = directionsToSearch
                        .firstOrNull { !coords.contains(currentSandPos + it.coord) }
                        ?.coord
                        ?.plus(currentSandPos)

                    if((nextFree?.y ?: 0) >= bottom) {
                        finished = true
                        break
                    }

                    if(nextFree == null){
                        coords.add(currentSandPos)
                        amountOfSand += 1
                        break
                    }
                    else {
                        currentSandPos = nextFree
                    }
                }
            }
        }
    }

    class CaveB(val coords: MutableSet<Coord>) {
        val bottom = coords.maxOf { it.y } + 2
        val sandEntryPos = Coord(500, 0)
        var amountOfSand = 0
        val directionsToSearch = listOf<Direction>(Direction.U, Direction.UL, Direction.UR)

        fun isFree(coord: Coord): Boolean = coord.y < bottom && !coords.contains(coord)

        fun runSand() {
            var finished = false
            while (!finished) {
                var currentSandPos = sandEntryPos
                while (true) {
                    val nextFree = directionsToSearch
                        .firstOrNull { isFree(currentSandPos + it.coord) }
                        ?.coord
                        ?.plus(currentSandPos)

                    if(nextFree == null){
                        coords.add(currentSandPos)
                        amountOfSand += 1
                        if(currentSandPos == Coord(500, 0))
                            finished = true
                        break
                    }
                    else {
                        currentSandPos = nextFree
                    }
                }
            }
        }
    }

    override fun a(): String {
        val cave = Cave(parseInput(puzzleInput))
        cave.runSand()
        return "the amount of sand is ${cave.amountOfSand}"
    }

    override fun b(): String {
        val cave = CaveB(parseInput(puzzleInput))
        cave.runSand()
        return "the amount of sand is ${cave.amountOfSand}"
    }
}
