package aoc

import kotlin.math.abs

object Nine : Day(9) {
    private fun Int.clamp1(): Int = coerceIn(-1, 1)

    enum class Direction(val coord: Coord) {
        U(Coord(0, 1)),
        D(Coord(0, -1)),
        L(Coord(-1, 0)),
        R(Coord(1, 0)),
        UR(U.coord + R.coord),
        DR(D.coord + R.coord),
        DL(D.coord + L.coord),
        UL(U.coord + L.coord),
        None(Coord(0, 0));

        companion object {
            fun fromCoord(coord: Coord): Direction {
                return enumValues<Direction>().find { it.coord == coord } ?: throw IllegalArgumentException()
            }
        }
    }

    data class Coord(val x: Int, val y: Int) {

        operator fun plus(other: Coord): Coord {
            return Coord(x + other.x, y + other.y)
        }

        operator fun minus(other: Coord): Coord {
            return Coord(x - other.x, y - other.y)
        }

        private fun clamped1(): Coord {
            return Coord(x.clamp1(), y.clamp1())
        }

        fun directionTo(other: Coord): Direction {
            return Direction.fromCoord((other - this).clamped1())
        }

        fun isAdjacent(other: Coord): Boolean {
            return abs(x - other.x) <= 1 && abs(y - other.y) <= 1
        }
    }

    class Rope(input: List<String>, knotCount: Int) {
        private val knots = MutableList(knotCount) { Coord(0, 0) }
        val tailVisitedPositions = mutableSetOf(knots.last())

        private fun runInstruction(instruction: String) {
            val (dirS, countS) = instruction.split(' ')
            val dir = enumValueOf<Direction>(dirS)
            val count = countS.toInt()
            repeat(count) {
                step(dir)
            }
        }

        private fun step(direction: Direction) {
            knots[0] += direction.coord
            for (i in 1 until knots.size) {
                val head = knots[i -1]
                val tail = knots[i]
                if (!tail.isAdjacent(head)) {
                    val directionToHead = tail.directionTo(head)
                    knots[i] += directionToHead.coord
                }
            }
            tailVisitedPositions.add(knots.last())
        }

        init {
            input.forEach {
                runInstruction(it)
            }
        }
    }

    override fun a(): String {
        val rope = Rope(puzzleInput, 2)
        return rope.tailVisitedPositions.size.toString()
    }

    override fun b(): String {
        val rope = Rope(puzzleInput, 10)
        return rope.tailVisitedPositions.size.toString()
    }
}
