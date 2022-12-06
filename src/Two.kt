package aoc

import java.lang.IllegalArgumentException

object Two : Day(2) {
    enum class ChoiceType {
        Rock,
        Paper,
        Scissors,
    }
    sealed class Choice(val type: ChoiceType, val value: Int, val winsAgainst: ChoiceType, val losesAgainst: ChoiceType){
        companion object
    }
    object Rock : Choice(ChoiceType.Rock, value = 1, winsAgainst = ChoiceType.Scissors, losesAgainst = ChoiceType.Paper)
    object Paper : Choice(ChoiceType.Paper, value = 2, winsAgainst = ChoiceType.Rock, losesAgainst = ChoiceType.Scissors)
    object Scissors : Choice(ChoiceType.Scissors, value = 3, winsAgainst = ChoiceType.Paper, losesAgainst = ChoiceType.Rock)

    fun Choice.Companion.fromType(type: ChoiceType): Choice = when(type){
        ChoiceType.Rock -> Rock
        ChoiceType.Paper -> Paper
        ChoiceType.Scissors -> Scissors
    }

    enum class Result {
        Win,
        Loss,
        Draw,
    }

    fun Choice.resultAgainst(theirs: Choice): Result = when {
        winsAgainst == theirs.type -> Result.Win
        losesAgainst == theirs.type -> Result.Loss
        else -> Result.Draw
    }

    data class Row(val theirs: Choice, val ours: Choice) {
        fun getPoints(): Int {
            val winScore = when (ours.resultAgainst(theirs)) {
                Result.Win -> 6
                Result.Loss -> 0
                Result.Draw -> 3
            }
            return winScore + ours.value
        }

        companion object {
            fun parseA(row: String): Row {
                val (theirsString, oursString) = row.split(' ')
                val theirs = when (theirsString) {
                    "A" -> Rock
                    "B" -> Paper
                    "C" -> Scissors
                    else -> throw IllegalArgumentException("$theirsString is not a Choice")
                }
                val ours = when (oursString) {
                    "X" -> Rock
                    "Y" -> Paper
                    "Z" -> Scissors
                    else -> throw IllegalArgumentException("$theirsString is not a Choice")
                }
                return Row(theirs, ours)
            }

            fun parseB(row: String): Row {
                val (theirsString, oursString) = row.split(' ')
                val theirs = when (theirsString) {
                    "A" -> Rock
                    "B" -> Paper
                    "C" -> Scissors
                    else -> throw IllegalArgumentException("$theirsString is not a Choice")
                }
                val ours = when (oursString) {
                    "X" -> Choice.fromType(theirs.winsAgainst)
                    "Y" -> theirs
                    "Z" -> Choice.fromType(theirs.losesAgainst)
                    else -> throw IllegalArgumentException("$theirsString is not a Choice")
                }
                return Row(theirs, ours)
            }
        }
    }


    override fun a(): String {
        val result = puzzleInput.map { Row.parseA(it) }.sumOf { it.getPoints() }
        return "the sum if things go as planned is $result"
    }

    override fun b(): String {
        val result = puzzleInput.map { Row.parseB(it) }.sumOf { it.getPoints() }
        return "the sum if things go as planned is $result"
    }
}
