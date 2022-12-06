package aoc

object Five : Day(5) {
    private enum class InputSection {
        Crates,
        Instructions,
    }

    private data class Instruction(val from: Int, val to: Int, val amount: Int) {
        companion object {
            private val regex = Regex("move (\\d+) from (\\d+) to (\\d+)")
            operator fun invoke(s: String): Instruction {
                val match = regex.find(s)
                val (amount, from, to) = match!!.destructured
                return Instruction(from.toInt(), to.toInt(), amount.toInt())
            }
        }
    }

    class Supplies {

        private val numCrates = (puzzleInput.first().length + 1) / 4
        val stacks = buildList<MutableList<Char>> {
            for (i in 1..numCrates) {
                add(mutableListOf())
            }
        }
        private val instructions = mutableListOf<Instruction>()

        fun parse() {
            val cratesStrings = mutableListOf<String>()
            val instructionsStrings = mutableListOf<String>()
            var section = InputSection.Crates
            puzzleInput.forEach {
                when (section) {
                    InputSection.Crates -> {
                        if (it.contains('[')) {
                            cratesStrings.add(it)
                        } else {
                            section = InputSection.Instructions
                        }
                    }

                    InputSection.Instructions -> {
                        if (it.startsWith("move")) {
                            instructionsStrings.add(it)
                        }
                    }
                }
            }
            cratesStrings.forEach {
                it.chunked(4).forEachIndexed { index, s ->
                    if (s.isNotBlank()) stacks[index].add(s[1])
                }
            }
            stacks.forEach { it.reverse() }

            instructionsStrings.map { Instruction(it) }.forEach { instructions.add(it) }
        }

        fun applyInstructions9000() {
            instructions.forEach { (from, to, amount) ->
                repeat(amount) {
                    stacks[to - 1].add(stacks[from - 1].removeLast())
                }
            }
        }

        fun applyInstructions9001() {
            instructions.forEach { (from, to, amount) ->
                val crates = stacks[from - 1].takeLast(amount)
                repeat(amount) {
                    stacks[from - 1].removeLast()
                }
                stacks[to - 1].addAll(crates)
            }
        }
    }

    override fun a(): String {
        val supplies = Supplies()
        supplies.parse()
        supplies.applyInstructions9000()
        val result = supplies.stacks.map { it.last() }.joinToString("")
        return result
    }

    override fun b(): String {
        val supplies = Supplies()
        supplies.parse()
        supplies.applyInstructions9001()
        val result = supplies.stacks.map { it.last() }.joinToString("")
        return result
    }
}
