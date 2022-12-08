package aoc

object Eight : Day(8) {
    private val testInput = listOf<String>(
        "30373",
        "25512",
        "65332",
        "33549",
        "35390",
    )

    class Forest(input: List<String>) {
        private val rows = input.size
        private val cols = input.first().length
        private val treeRows = input.map { row ->
            row.map(Char::digitToInt)
        }
        private val treeColumns = treeRows.transpose()

        private fun isTreeVisible(rowIndex: Int, colIndex: Int): Boolean {
            val height = treeRows[rowIndex][colIndex]
            val row = treeRows[rowIndex]
            val col = treeColumns[colIndex]
            return row.subList(0, colIndex).all { it < height }
                    || row.subList(colIndex + 1, row.size).all { it < height }
                    || col.subList(0, rowIndex).all { it < height }
                    || col.subList(rowIndex + 1, col.size).all { it < height }
        }

        fun scenicScore(rowIndex: Int, colIndex: Int): Int {
            val height = treeRows[rowIndex][colIndex]
            val row = treeRows[rowIndex]
            val col = treeColumns[colIndex]
            val west = row.subList(0, colIndex).reversed().viewDistance(height)
            val east = row.subList(colIndex + 1, row.size).viewDistance(height)
            val north = col.subList(0, rowIndex).reversed().viewDistance(height)
            val south = col.subList(rowIndex + 1, col.size).viewDistance(height)
            return west * east * north * south
        }

        private fun List<Int>.viewDistance(height: Int): Int{
            return when(val idx = indexOfFirst { it >= height }) {
                -1 -> size
                else -> idx + 1
            }
        }

        fun numberOfVisibleTrees(): Int = treeRows.flatMapIndexed { x, y, _ ->
            isTreeVisible(x, y)
        }.count { it }

        fun highestScenicScore(): Int = treeRows.flatMapIndexed { x, y, _ ->
            scenicScore(x, y)
        }.max()
    }

    override fun a(): String {
        val forest = Forest(puzzleInput)
        val result = forest.numberOfVisibleTrees()
        return "The number of visible trees is $result"
    }

    override fun b(): String {
        val forest = Forest(puzzleInput)
        val result = forest.highestScenicScore()
        return "The biggest scenic score is $result"
    }
}
