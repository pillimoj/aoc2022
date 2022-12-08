package aoc


fun <T, R> List<List<T>>.flatMapIndexed(transform: (x: Int, y: Int, element: T) -> R): List<R>{
    val origin = this
    return buildList {
        for (x in origin.indices){
            val row = origin[x]
            for(y in row.indices){
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