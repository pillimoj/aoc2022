package aoc

object Ten : Day(10) {
    class CommsDevice(input: List<String>) {
        class CRT(spritePos: Int) {
            private val data = MutableList(6 * 40) { '.' }
            private var spritePixels = IntRange.EMPTY
            private var cycle = 0

            init {
                setSpritePosition(spritePos)
            }

            fun setSpritePosition(pos: Int) {
                spritePixels = (pos - 1)..(pos + 1)
            }

            fun runCycle() {
                if (cycle % 40 in spritePixels) {
                    data[cycle] = '#'
                }
                cycle += 1
            }

            fun getOutput(): String {
                val separator = "\n" + List(40) { '=' }.joinToString("") + "\n"
                return separator +
                        data.chunked(40).joinToString("\n") { it.joinToString("") } +
                        separator
            }
        }

        sealed class Progress
        object Cycle : Progress()
        class Add(val addx: Int) : Progress()


        private val cycles = input.map {
            if (it == "noop") {
                listOf(Cycle)
            } else {
                val x = it.split(' ')[1].toInt()
                listOf(Cycle, Cycle, Add(x))
            }
        }.flatten()

        fun drawScreen(): String {
            var xRegister = 1
            val crtScreen = CRT(xRegister)

            cycles.forEach {
                when (it) {
                    is Cycle -> {
                        crtScreen.runCycle()
                    }
                    is Add -> {
                        xRegister += it.addx
                        crtScreen.setSpritePosition(xRegister)
                    }
                }
            }
            return crtScreen.getOutput()
        }

        fun runStrengthSampling(): List<Int> {
            val strengthAtIntervals = mutableListOf<Int>()
            var signalStrength = 1
            var cycleCounter = 0

            cycles.forEach {
                when (it) {
                    is Cycle -> {
                        cycleCounter += 1
                        if ((cycleCounter + 20) % 40 == 0) strengthAtIntervals.add(cycleCounter * signalStrength)
                    }
                    is Add -> signalStrength += it.addx
                }
            }
            return strengthAtIntervals
        }
    }

    override fun a(): String {
        val device = CommsDevice(puzzleInput)
        val samples = device.runStrengthSampling()
        val result = samples.sum()
        return "$result"
    }

    override fun b(): String {
        val device = CommsDevice(puzzleInput)
        val result = device.drawScreen()
        return result
    }
}
