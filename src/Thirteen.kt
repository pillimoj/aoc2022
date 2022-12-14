package aoc

object Thirteen : Day(13) {
    private const val DIGITS = "0123456789"

    private class UnmatchedBracketException : Exception()

    private sealed class Package : Comparable<Package>
    private class IntPackage(val value: Int) : Package() {
        override fun compareTo(other: Package): Int {
            return when (other) {
                is IntPackage -> value.compareTo(other.value)
                is ListPackage -> ListPackage(listOf(this)).compareTo(other)
                is DividerPackage -> compareTo(ListPackage(other.packages))
            }
        }

        override fun toString(): String {
            return value.toString()
        }
    }

    private class ListPackage(val packages: List<Package>) : Package() {
        override fun compareTo(other: Package): Int {
            return when (other) {
                is IntPackage -> -other.compareTo(this)
                is ListPackage -> {
                    val result = packages.zip(other.packages).firstOrNull { it.first.compareTo(it.second) != 0 }?.let { it.first.compareTo(it.second) }
                    result ?: packages.size.compareTo(other.packages.size)
                }
                is DividerPackage -> compareTo(ListPackage(other.packages))
            }
        }

        override fun toString(): String {
            return packages.toString()
        }
    }
    private class DividerPackage(value: Int) : Package() {
        val packages = listOf(ListPackage(listOf(IntPackage(value))))
        override fun compareTo(other: Package): Int {
            return ListPackage(packages).compareTo(other)
        }
    }

    private fun String.listPackageLength(): Int {
        require(first() == '[')
        var depth = 1
        substring(1).forEachIndexed { index, c ->
            when (c) {
                '[' -> depth += 1
                ']' -> {
                    depth -= 1
                    if (depth == 0) return index
                }
            }
        }
        throw UnmatchedBracketException()

    }

    private fun parseListPackage(input: String): ListPackage {
        val packages = buildList<Package> {
            var rest = input
            while (rest.isNotEmpty()) {
                when (rest.first()) {
                    in DIGITS -> {
                        val numberStr = rest.takeWhile { it.isDigit() }.toCharArray().concatToString()
                        rest = rest.substring(numberStr.length)
                        add(IntPackage(numberStr.toInt()))
                    }

                    '[' -> {
                        if (rest == "[]") {
                            add(ListPackage(emptyList()))
                            break
                        }
                        val listPackageLength = rest.listPackageLength()
                        add(parseListPackage(rest.substring(1..listPackageLength)))
                        rest = rest.substring(listPackageLength + 1)
                    }

                    else -> rest = rest.substring(1)
                }
            }
        }
        return ListPackage(packages)
    }

    private fun parsePackagePair(input: List<String>): List<Package> {
        return input.take(2).map {
            val indices = 1 until it.length - 1
            parseListPackage(it.substring(indices))
        }
    }

    override fun a(): String {
        val result = puzzleInput
                .chunked(3)
                .map { parsePackagePair(it) }
                .foldIndexed(0) { i, acc, pkgs ->
            val (left, right) = pkgs
            if (left < right) acc + i + 1 else acc
        }
        return "$result"
    }

    override fun b(): String {
        val packages: MutableList<Package> = puzzleInput.mapNotNull {
            if(it.isBlank()) {
                null
            }
            else {
                val indices = 1 until it.length - 1
                parseListPackage(it.substring(indices))
            }
        }.toMutableList()
        packages.add(DividerPackage(2))
        packages.add(DividerPackage(6))
        packages.sort()
        val result = packages.foldIndexed(1) { i, acc, p ->
            if(p is DividerPackage) acc * (i+1) else acc
        }
        return "$result"
    }
}
