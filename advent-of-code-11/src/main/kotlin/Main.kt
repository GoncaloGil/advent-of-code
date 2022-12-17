import java.io.File
import java.lang.Exception
import java.math.BigInteger

fun main(args: Array<String>) {
    val lines = File("src/main/resources/puzzle.txt").readLines()
    val groups = lines.chunked(7)

    val monkeyGroup = MonkeyGroup(lines)
    repeat(10000) {
        monkeyGroup.doRound()
    }
    val result = monkeyGroup.monkeys.sortedByDescending { it.inspectCounter }.take(2)
    monkeyGroup.monkeys.forEach {
        println(it.inspectCounter)
    }
    println("---RESULT---")
    println(result[0].inspectCounter * result[1].inspectCounter)
}