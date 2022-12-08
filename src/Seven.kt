package aoc

val testInput = listOf<String>(
"$ cd /",
"$ ls",
"dir a",
"14848514 b.txt",
"8504156 c.dat",
"dir d",
"$ cd a",
"$ ls",
"dir e",
"29116 f",
"2557 g",
"62596 h.lst",
"$ cd e",
"$ ls",
"584 i",
"$ cd ..",
"$ cd ..",
"$ cd d",
"$ ls",
"4060174 j",
"8033020 d.log",
"5626152 d.ext",
"7214296 k",
)

object Seven : Day(7) {
    abstract class Path(val name: String) {
        abstract val size: Long
    }

    class File(name: String, override val size: Long) : Path(name)
    class Dir(name: String) : Path(name) {
        var cachedSize: Long? = null

        override val size: Long
            get() {
                if (cachedSize == null) {
                    cachedSize = children.fold(0L) { acc, child -> acc + child.size }
                }
                return cachedSize!!
            }

        private val children: MutableList<Path> = mutableListOf()

        fun addChild(path: Path) {
            cachedSize = null
            children.add(path)
        }

    }

    class Machine(val input: List<String>) {
        val fileSystemSize = 70_000_000L
        val spaceNeeded = 30_000_000L

        val output = puzzleInput.toMutableList()
        val directories: MutableMap<String, Dir> = mutableMapOf("/" to Dir("/"))
        var cwd: String = "/"

        fun currentDir(): Dir = directories[cwd]!!
        fun rootDir(): Dir = directories["/"]!!

        fun changePath(path: String) {
            cwd = when (path) {
                "/" -> path
                ".." -> {
                    require(cwd.length > 1)
                    val cwdWithoutTrailingSlash = cwd.removeSuffix("/")
                    val indexOfParentEnd = cwdWithoutTrailingSlash.indexOfLast { it == '/' } + 1
                    cwd.removeRange(indexOfParentEnd until cwd.length)
                }
                else -> {
                    val newpath = "$cwd$path/"
                    directories.putIfAbsent(newpath, Dir(path))
                    newpath
                }
            }
        }

        fun addDir(name: String) {
            val newDir = Dir(name)
            currentDir().addChild(newDir)
            directories.put("$cwd$name/", newDir)
        }

        fun addFile(name: String, size: Long) {
            currentDir().addChild(File(name, size))
        }

        fun handleLine(line: String) {
            when {
                line.startsWith("$ ls") -> Unit
                line.startsWith("$ cd") -> changePath(line.removePrefix("$ cd "))
                line.startsWith("dir") -> addDir(line.removePrefix("dir "))
                line.first().isDigit() -> {
                    val (sizeStr, name) = line.split(' ')
                    addFile(name, sizeStr.toLong())
                }
            }
        }

        fun sumOfDirsSmallerThan100k(): Long {
            val dirs = directories.values.filter { it.size <= 100000L }
            return dirs.sumOf { it.size }
        }

        fun spaceToDelete(): Long {
            val freeSpace = fileSystemSize - rootDir().size
            return spaceNeeded - freeSpace
        }

        fun getDirectoryToDelete(): Dir {
            val spaceToDelete = spaceToDelete()
            return directories.values.filter { it.size >= spaceToDelete }.minByOrNull { it.size }!!
        }

        fun getSizeOfDirToDelete(): Long = getDirectoryToDelete().size

        init {
            input.forEach { handleLine(it) }
        }
    }


    override fun a(): String {
        val machine = Machine(puzzleInput)
        val result = machine.sumOfDirsSmallerThan100k()
        return "sum is $result"
    }

    override fun b(): String {
        val machine = Machine(puzzleInput)
        val result = machine.getSizeOfDirToDelete()
        return "sum is $result"
    }
}
