package aoc

import java.math.BigInteger

private typealias MonkeyId = Int
private typealias WorryLevel = Long

private fun String.toWorryLevel(): WorryLevel = toLong()
private fun String.toWorryLevelOrNull(): WorryLevel? = toLongOrNull()

object Eleven : Day(11) {
    class Monkey(input: List<String>, var worryDecreaseFn: ((WorryLevel) -> WorryLevel)? = null) {
        private val items: MutableList<WorryLevel> = parseItems(input[1])
        private val inspect: Monkey.(item: WorryLevel) -> Pair<MonkeyId, WorryLevel> = parseInspectFun(input)
        val testNumber = parseTestNumber(input)
        var inspectedItems = 0L

        fun takeTurn(): List<Pair<MonkeyId, WorryLevel>> {
            val result = items.map { inspect(it) }
            items.clear()
            return result
        }

        fun addItem(worryLevel: WorryLevel) {
            items.add(worryLevel)
        }

        companion object {
            private fun parseItems(itemsInput: String): MutableList<WorryLevel> = itemsInput
                .removePrefix("  Starting items: ")
                .split(", ")
                .map(String::toWorryLevel)
                .toMutableList()

            private fun parseInspectFun(input: List<String>): Monkey.(WorryLevel) -> Pair<MonkeyId, WorryLevel> {
                val inspectOperation = parseInspectOperation(input[2])
                val nextMonkeyPassTest = input[4].removePrefix("    If true: throw to monkey ").toInt()
                val nextMonkeyFailTest = input[5].removePrefix("    If false: throw to monkey ").toInt()

                return { item ->
                    val afterInspect = inspectOperation(item)
                    val newWorryValue = worryDecreaseFn?.invoke(afterInspect) ?: afterInspect
                    val nextMonkey = if (newWorryValue % testNumber == 0L) nextMonkeyPassTest else nextMonkeyFailTest
                    inspectedItems += 1
                    nextMonkey to newWorryValue
                }
            }

            private fun parseInspectOperation(input: String): (WorryLevel) -> WorryLevel {
                val (operand1, operation, operand2) = input.removePrefix("  Operation: new = ").split(" ")
                return { old ->
                    val op1 = operand1.toWorryLevelOrNull() ?: old
                    val op2 = operand2.toWorryLevelOrNull() ?: old
                    when (operation) {
                        "+" -> op1 + op2
                        "-" -> op1 - op2
                        "*" -> op1 * op2
                        "/" -> op1 / op2
                        else -> throw IllegalArgumentException("Uknown math operation $operation")
                    }
                }
            }

            private fun parseTestNumber(input: List<String>): WorryLevel {
                return input[3].removePrefix("  Test: divisible by ").toWorryLevel()
            }
        }
    }

    override fun a(): String {
        val monkeys = puzzleInput.chunked(7).map { Monkey(it, worryDecreaseFn = { before -> before / 3L }) }
        repeat(20) {
            monkeys.forEach {
                val thrownItems = it.takeTurn()
                thrownItems.forEach { (monkeyId, worryLevel) ->
                    monkeys[monkeyId].addItem(worryLevel)
                }
            }
        }
        val (mostActive, secondMostActive) = monkeys.map { it.inspectedItems }.sortedDescending().take(2)

        return "The sum of monkey business is ${mostActive * secondMostActive}"
    }

    override fun b(): String {
        val monkeys = puzzleInput.chunked(7).map { Monkey(it, worryDecreaseFn = null) }
        val productOfTestNumbers = monkeys
            .map { it.testNumber }
            .reduce { acc, testNumber -> acc * testNumber }
        val worryDecreaseFn = { old: WorryLevel -> old % productOfTestNumbers }
        monkeys.forEach { it.worryDecreaseFn = worryDecreaseFn }
        repeat(10000) {
            monkeys.forEach {
                val thrownItems = it.takeTurn()
                thrownItems.forEach { (monkeyId, worryLevel) ->
                    monkeys[monkeyId].addItem(worryLevel)
                }
            }
        }
        val (mostActive, secondMostActive) = monkeys.map { it.inspectedItems }.sortedDescending().take(2)

        return "The sum of monkey business is ${mostActive * secondMostActive}"
    }
}
