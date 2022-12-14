package aoc

import kotlin.math.abs

private fun Int.clamp1(): Int = coerceIn(-1, 1)


fun <T, R> List<List<T>>.flatMapIndexed(transform: (x: Int, y: Int, element: T) -> R): List<R> {
    val origin = this
    return buildList {
        for (x in origin.indices) {
            val row = origin[x]
            for (y in row.indices) {
                add(transform(x, y, row[y]))
            }
        }
    }
}

fun <T> List<List<T>>.transpose(): List<List<T>> {
    val rows = indices
    val cols = first().indices
    val result = buildList<MutableList<T>> { for (i in cols) add(mutableListOf()) }
    for (i in rows) {
        for (j in cols) {
            result[j].add(this[i][j])
        }
    }
    return result.map { it.toList() }
}

data class Coord(val x: Int, val y: Int) {

    operator fun plus(other: Coord): Coord {
        return Coord(x + other.x, y + other.y)
    }

    operator fun minus(other: Coord): Coord {
        return Coord(x - other.x, y - other.y)
    }

    private fun clamped1(): Coord {
        return Coord(x.clamp1(), y.clamp1())
    }

    fun directionTo(other: Coord): Direction {
        return Direction.fromCoord((other - this).clamped1())
    }

    fun isAdjacent(other: Coord): Boolean {
        return abs(x - other.x) <= 1 && abs(y - other.y) <= 1
    }

    fun adjacentCoords(gridSizeX: Int, gridSizeY: Int, includeDiagonal: Boolean = false): List<Coord> {
        return buildList {
            if (x > 0) add(copy(x = x - 1))
            if (x < gridSizeX - 1) add(copy(x = x + 1))
            if (y > 0) add(copy(y = y - 1))
            if (y < gridSizeY - 1) add(copy(y = y + 1))
            if (includeDiagonal) TODO("Implement getting diagonal adjacent Coords")
        }
    }
}

enum class Direction(val coord: Coord) {
    U(Coord(0, 1)),
    D(Coord(0, -1)),
    L(Coord(-1, 0)),
    R(Coord(1, 0)),
    UR(U.coord + R.coord),
    DR(D.coord + R.coord),
    DL(D.coord + L.coord),
    UL(U.coord + L.coord),
    None(Coord(0, 0));

    companion object {
        fun fromCoord(coord: Coord): Direction {
            return enumValues<Direction>().find { it.coord == coord } ?: throw IllegalArgumentException()
        }
    }
}