import java.io.File
import java.io.InputStream

fun main(args: Array<String>) {
    val inputStream: InputStream = File("src/main/resources/puzzle.txt").inputStream()
    val lineList = inputStream.bufferedReader().readLines()

    val result = lineList.map { SectionPair(it) }.filter { it.partialOverlaped }.size

    println(result)
}

class Section(private val sectionLine: String) {
    val minimum: Int= sectionLine.split("-")[0].toInt()
    val maximum: Int = sectionLine.split("-")[1].toInt()
}

class SectionPair(private val puzzleLine: String) {
    val sections = puzzleLine.split(",").map { Section(it) }
    val fullyOverlaped = sectionFullyOverlaped(sections[0],sections[1]) ||  sectionFullyOverlaped(sections[1],sections[0])
    val partialOverlaped = sectionPartialOverlaped(sections[0],sections[1]) ||  sectionPartialOverlaped(sections[1],sections[0])

    private fun sectionFullyOverlaped(a: Section, b: Section) : Boolean {
        return (a.minimum in b.minimum .. b.maximum &&
            a.maximum in b.minimum .. b.maximum)
    }

    private fun sectionPartialOverlaped(a: Section, b: Section): Boolean {
        return (a.minimum in b.minimum .. b.maximum ||
                a.maximum in b.minimum .. b.maximum)
    }
}