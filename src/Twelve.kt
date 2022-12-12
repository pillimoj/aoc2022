package aoc

object Twelve : Day(12) {

    data class Node(
        val coord: Coord,
        val height: Char,
        var distance: Int = Int.MAX_VALUE,
        val goal: Boolean = false
    ) {
        companion object {

            fun fromInput(input: List<String>, createNode: (Coord, Char) -> Node): MutableMap<Coord, Node> {
                return buildList {
                    for (y in input.indices) {
                        for (x in 0 until input[y].length) {
                            val coord = Coord(x, y)
                            val char = input[y][x]
                            val node = createNode(coord, char)
                            add(node)
                        }
                    }
                }.associateBy { it.coord }.toMutableMap()
            }
        }
    }

    private fun algorithm(unvisited: MutableMap<Coord, Node>, heightFilter: (Char, Char) -> Boolean): Node {
        var currentNode = unvisited.minBy { it.value.distance }.value
        val gridSizeX = unvisited.keys.maxBy { it.x }.x + 1
        val gridSizeY = unvisited.keys.maxBy { it.y }.y + 1

        do {
            val neighbors = currentNode.coord
                .adjacentCoords(gridSizeX, gridSizeY)
                .mapNotNull { unvisited.get(it) }
                .filter { heightFilter(currentNode.height, it.height) }

            neighbors.forEach { neighbor ->
                neighbor.distance = minOf(currentNode.distance + 1, neighbor.distance)
            }
            unvisited.remove(currentNode.coord)
            currentNode = unvisited.minBy { it.value.distance }.value
        } while (!currentNode.goal)
        return currentNode
    }

    override fun a(): String {
        val nodes = Node.fromInput(puzzleInput){ coord, char ->
            when(char) {
                'S' -> Node(coord, 'a', distance = 0)
                'E' -> Node(coord, 'z', goal = true)
                else -> Node(coord, char)
            }
        }
        val result = algorithm(nodes, heightFilter = { current, neighbourCandidate ->
            neighbourCandidate - current <= 1
        })
        return "distance ${result.distance}"
    }

    override fun b(): String {
        val nodes = Node.fromInput(puzzleInput){ coord, char ->
            when(char) {
                'S', 'a' -> Node(coord, 'a', goal = true)
                'E' -> Node(coord, 'z', distance = 0)
                else -> Node(coord, char)
            }
        }
        val result = algorithm(nodes, heightFilter = { current, neighbourCandidate ->
            current - neighbourCandidate <= 1
        })
        return "distance ${result.distance}"
    }
}
