import java.awt.font.NumericShaper.Range
import java.io.File
import java.util.Stack

fun main(args: Array<String>) {
    val inputStream = File("src/main/resources/puzzle.txt").inputStream().bufferedReader()
    val lines = inputStream.readLines()

    val loadLines = lines.subList(0, lines.indexOfFirst { !it.contains("]") })
    val orderLines = lines.subList(lines.indexOfFirst { it.contains("move") }, lines.size)

    val work = Work(orderLines, loadLines)
    work.processOrdersMulti()
    var string = ""
    println(work.ship.stacks)
    work.ship.stacks.forEach { string += it.pop() }
    print(string)

}

class Work(val orderLines: List<String>, val loadLines: List<String>) {
    val ship = Ship(loadLines)
    val orders : List<Order> = readOrderLines()

    fun processOrdersOneByOne() {
        orders.forEach { order ->
            repeat(order.amount) {
                ship.moveOneCargo(order.origin, order.destination)
            }
        }
    }

    fun processOrdersMulti() {
        orders.forEach { order ->
            ship.moveMultipleCargo(order.origin, order.destination, order.amount)
        }
    }

    private fun readOrderLines(): List<Order> {
        val orders = mutableListOf<Order>()
        val regex = Regex("\\d+")
        orderLines.forEach {line ->
            val resultList = regex.findAll(line).map { it.value.toInt() }.toList()
            orders.add(
                Order(
                    amount = resultList[0],
                    origin = resultList[1] - 1,
                    destination = resultList[2] - 1
                )
            )
        }
        return orders
    }
}

data class Order(
    val amount: Int,
    val origin: Int,
    val destination: Int
)

class Ship(private val loadLines: List<String>) {
    val stacks = readLoadLines()


    private fun readLoadLines(): List<Stack<Char>> {
        val stacks = mutableListOf<Stack<Char>>()
        loadLines.asReversed().forEach { addLoad(it, stacks) }
        return stacks
    }

    private fun addLoad(puzzleLine: String, stacks: MutableList<Stack<Char>>) {
        val stackInputs = puzzleLine.chunked(4)
        while (stacks.size < stackInputs.size) {
            stacks.add(Stack())
        }
        stackInputs.forEachIndexed { index, s ->
            if (s[1] != ' ') {
                stacks[index].add(s[1])
            }
        }
    }

    fun moveOneCargo(origin: Int, destination: Int) {
        stacks[destination].push(stacks[origin].pop())
    }

    fun moveMultipleCargo(origin: Int, destination: Int, amount: Int) {
        stacks[destination].addAll( stacks[origin].takeLast(amount) )
        repeat(amount) {stacks[origin].pop()}
    }
}