package aoc

object One : Day(1) {

    private val calorieSizes: List<Int> = run {
        val calorieSizes = buildList {
            var currentCalories = 0
            this@One.puzzleInput.forEach {
                if (it.isBlank()) {
                    this.add(currentCalories)
                    currentCalories = 0
                } else {
                    currentCalories += it.toInt()
                }
            }
        }
        calorieSizes
    }
    override fun a(): String {
        val result = calorieSizes.maxOrNull()!!
        return "the largest number of calories is $result"
    }

    override fun b(): String {
        val topThree = calorieSizes.sortedDescending().take(3).sum()
        return "the sum of top three calorie sizes is $topThree"
    }
}
