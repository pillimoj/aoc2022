package aoc

object Six : Day(6) {

    fun stepsToSequenceStart(data: String, sequencePreambleLength: Int): Int{
        val sequencesBefore = data.windowed(sequencePreambleLength, 1).takeWhile {
            it.toCharArray().toSet().size < sequencePreambleLength
        }
        return sequencesBefore.size + sequencePreambleLength
    }

    override fun a(): String {
        val data = puzzleInput.first()
        val result = stepsToSequenceStart(data, 4)
        return "the start is after $result characters"
    }

    override fun b(): String {
        val data = puzzleInput.first()
        val result = stepsToSequenceStart(data, 14)
        return "the start is after $result characters"
    }
}
