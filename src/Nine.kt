package aoc

import kotlin.math.abs

object Nine : Day(9) {
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
