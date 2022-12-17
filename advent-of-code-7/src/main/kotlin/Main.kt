import java.io.File
import kotlin.properties.Delegates

fun main(args: Array<String>) {
    val inputStream = File("src/main/resources/puzzle.txt").inputStream().bufferedReader()
    val lines = mutableListOf<String>()
    inputStream.forEachLine { lines.add(it) }

    val rootDirectory = Directory("/", null)
    rootDirectory.readPuzzleLine(lines)
    rootDirectory.calculateFullSize()
    val requiredSpace = 30000000 - (70000000 - rootDirectory.fullSize)
    val directoriesToDelete = rootDirectory.getDirectoryToDelete(requiredSpace, HashSet()).sortedBy { it.fullSize }
    println(directoriesToDelete.first().fullSize)
}


data class Directory(
    val name: String,
    val father: Directory?
)
{
    val directories: HashMap<String, Directory> = HashMap()
    val files: MutableList<FileElement> = mutableListOf()
    var partialSize by Delegates.notNull<Int>()
    var fullSize by Delegates.notNull<Int>()

    private fun handleLsOutput(puzzleLines: List<String>) {
        puzzleLines.forEach {
            it.split(" ")
        }
    }

    private fun getRootDirectory(directory: Directory): Directory {
        return if(directory.father == null) {
            directory
        } else {
            getRootDirectory(directory.father)
        }
    }

    private fun changeDirectory(directory: Directory, destination: String): Directory {
        return when(destination) {
            ".." -> directory.father!!
            "/" -> getRootDirectory(directory)
            else -> directory.directories[destination]!!
        }
    }

    private fun populateDirectory(puzzleLines: List<String>){
        puzzleLines.forEach {
            val elementData = it.split(" ")
            when(elementData[0]) {
                "dir" -> directories[elementData[1]] = Directory(elementData[1], this)
                else -> files.add(FileElement(elementData[1],elementData[0].toInt()))
            }
        }
    }

    fun readPuzzleLine(puzzleLines: List<String>) {
        val line = puzzleLines.first()
        if (line.startsWith("$")) {
            var command = line.split(" ")
            when(command[1]) {
                "cd" -> changeDirectory(this, command[2]).readPuzzleLine(puzzleLines.drop(1))
                "ls" -> {
                    val numberOfFiles = puzzleLines.drop(1).indexOfFirst { it.startsWith("$") }
                    if(numberOfFiles != -1) {
                        populateDirectory(puzzleLines.drop(1).subList(0,numberOfFiles))
                        readPuzzleLine(puzzleLines.drop(1).drop(numberOfFiles))
                    } else {
                        populateDirectory(puzzleLines.drop(1))
                    }

                }
            }
        }
    }

    fun calculateFullSize() {
        var counter = 0
        calculatePartialSize()
        directories.forEach{
            it.value.calculateFullSize()
            counter += it.value.fullSize
        }
        fullSize = counter + partialSize
    }
    fun calculatePartialSize() {
        partialSize = files.sumOf { it.size }
    }

    fun sumOfDirectoriesBelow(limit: Int): Int {
        calculateFullSize()
        var counter = 0
        directories.forEach {
            counter+= it.value.sumOfDirectoriesBelow(limit)
        }
        if (fullSize <= limit) {
            counter += fullSize
        }
        return counter
    }

    fun getDirectoryToDelete(requiredSpace: Int, list: HashSet<Directory>): HashSet<Directory> {
        directories.forEach {
            list.addAll(it.value.getDirectoryToDelete(requiredSpace, list))
        }
        if (fullSize >= requiredSpace) {
            list.add(this)
        }
        return list
    }
}

data class FileElement(
    val name: String,
    val size: Int
)