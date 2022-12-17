class MonkeyGroup(puzzleLines: List<String>) {
    private val listOfDivisors = puzzleLines.chunked(7).map { getDivisor(it) }
    val monkeys = puzzleLines.chunked(7).map { Monkey(it, listOfDivisors) }

    fun doRound() {
        monkeys.forEach {
            while (it.items.isNotEmpty()) {
                val condition = it.handleItem()
                val item = it.removeFirst()
                when (condition) {
                    false -> monkeys[it.destinationMonkeys[0]].addItem(item)
                    true -> monkeys[it.destinationMonkeys[1]].addItem(item)
                }
            }

        }
    }

    fun getDivisor(monkeyLines: List<String>): Int {
        return Regex("[0-9]+").find(monkeyLines[3])!!.value.toInt()
    }
}