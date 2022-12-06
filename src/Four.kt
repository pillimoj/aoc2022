package aoc

object Four : Day(4) {
    data class Assignment(val start: Int, val end: Int){
        fun includes(other: Assignment): Boolean = start <= other.start && end >= other.end
        fun overlaps(other: Assignment): Boolean = start in other.start..other.end
            || end in other.start..other.end
            || includes(other)
    }
    data class AssignmentPair(val first: Assignment, val second: Assignment){
        fun hasCompleteOverlap() = first.includes(second) || second.includes(first)
        fun hasOverlap() = first.overlaps(second)
    }

    private fun parse(assignmentPair: String): AssignmentPair {
        val (one , two) = assignmentPair.split(',').map { it.split('-') }.map { (first, second) -> Assignment(first.toInt(), second.toInt()) }
        return AssignmentPair(one, two)
    }

    override fun a(): String {
        val result = puzzleInput.map { parse(it) }.count { it.hasCompleteOverlap() }
        return "$result"
    }

    override fun b(): String {
        val result = puzzleInput.map { parse(it) }.count { it.hasOverlap() }
        return "$result"
    }
}
