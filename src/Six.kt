package aoc

object Six : Day(6) {

    override fun a(): String {
        val sequencesBefore = puzzleInput.first().windowed(4, 1).takeWhile {
            it.toCharArray().toSet().size < 4
        }
        val result = sequencesBefore.size + 4
        return "the start is after $result characters"
    }

    override fun b(): String {
        val sequencesBefore = puzzleInput.first().windowed(14, 1).takeWhile {
            it.toCharArray().toSet().size < 14
        }
        val result = sequencesBefore.size + 14
        return "the start is after $result characters"
    }
}
