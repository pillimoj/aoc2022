package aoc

object Three : Day(3) {
    private val priorities by lazy {
        val lowercase = "abcdefghijklmnopqrstuvwxyz"
        val all = lowercase + lowercase.uppercase()
        all.mapIndexed { index, c -> c to index + 1 }.toMap()
    }

    private fun getItemScore(item: Char): Int {
        require(priorities.contains(item))
        return priorities[item]!!
    }

    private fun getItemInBothCompartments(backPack: String): Char {
        val (compartment1, compartment2) = backPack.chunked(backPack.length / 2).map(CharSequence::toSet)
        val charsInBoth = compartment1.intersect(compartment2)
        assert(charsInBoth.size == 1)
        return charsInBoth.first()
    }

    private fun getBadge(group: List<String>): Char {
        require(group.size == 3)
        val badge = group
            .map(CharSequence::toSet)
            .reduce { acc, groupMember -> acc.intersect(groupMember) }
        assert(badge.size == 1)
        return badge.first()
    }

    override fun a(): String {
        val result = puzzleInput
            .map { getItemInBothCompartments(it) }
            .sumOf { getItemScore(it) }
        return "the sum is $result"
    }

    override fun b(): String {
        val result = puzzleInput.chunked(3)
            .map { getBadge(it) }
            .sumOf { getItemScore(it) }
        return "the sum is $result"
    }
}
