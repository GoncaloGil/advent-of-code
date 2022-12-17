import java.io.File

fun main(args: Array<String>) {
    val inputStream = File("src/main/resources/puzzle.txt").inputStream().bufferedReader()
    val lines = inputStream.readLines()

    val message = Message(lines[0])
    println(message.markerPosition)
}

class Message(puzzleLine: String) {
    val markerPosition: Int = getStarterMarker(puzzleLine)

    private fun getStarterMarker(message: String): Int {
        val charSet: HashSet<Char> = HashSet()
        for(i in MARKER_SIZE until message.length) {
            charSet.clear()
            message.substring(i-MARKER_SIZE,i).forEach { charSet.add(it) }
            if(charSet.size == MARKER_SIZE) {
                return i
            }
        }
        return -1
    }

    companion object {
        const val MARKER_SIZE = 14
    }

}