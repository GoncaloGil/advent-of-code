class Monkey(
    val puzzleLines: List<String>,
    val listOfDivisors: List<Int>
) {
    var items: MutableList<WorryLevel> = mutableListOf()
    lateinit var operation: MonkeyOperation
    lateinit var condition: MonkeyOperation
    lateinit var destinationMonkeys: List<Int>
    var inspectCounter: Long = 0

    init {
        initializeMonkey(puzzleLines)
    }

    fun initializeMonkey(puzzleLines: List<String>) {
        items = Regex("[0-9]+").findAll(puzzleLines[1])
            .map {
                WorryLevel(it.value.toInt(),listOfDivisors)
            }
            .toMutableList()
        val operator = Regex("[/*/+]").find(puzzleLines[2])!!.value.single()
        operation = MonkeyOperation(
            operator,
            puzzleLines[2].removePrefix("  Operation: new = old $operator ")
        )
        condition = MonkeyOperation(
            '/',
            Regex("[0-9]+").find(puzzleLines[3])!!.value
        )
        destinationMonkeys = listOf(
            Regex("[0-9]+").find(puzzleLines[5])!!.value.toInt(),
            Regex("[0-9]+").find(puzzleLines[4])!!.value.toInt()
        )
    }

    fun addItem(newValue: WorryLevel) {
        items.add(newValue)
    }

    fun removeFirst(): WorryLevel {
        return items.removeFirst()
    }

    fun handleItem(): Boolean {
        operation.calculateResult(items[0])
        //items[0] /= 3
        inspectCounter++
        return condition.verifyCondition(items[0])
    }
}