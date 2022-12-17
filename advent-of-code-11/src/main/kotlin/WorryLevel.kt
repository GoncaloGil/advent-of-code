class WorryLevel(
    val initialValue: Int,
    val listOfDivisorsConditions: List<Int>
) {
    val modCounter: HashMap<Int,Int> = initializeModCounter(listOfDivisorsConditions)

    private fun initializeModCounter(listOfDivisorsConditions: List<Int>): HashMap<Int,Int> {
        val modCounter = HashMap<Int,Int>()
        listOfDivisorsConditions.forEach {
            modCounter[it] = initialValue % it
        }
        return modCounter
    }

    fun add(value: Int) {
        for (divisor in modCounter.keys) {
            modCounter[divisor] = (modCounter[divisor]!! + value) % divisor
        }
    }

    fun multiply(value: Int) {
        for (divisor in modCounter.keys) {
            modCounter[divisor] = (modCounter[divisor]!! * value) % divisor
        }
    }

    fun square() {
        for (divisor in modCounter.keys) {
            modCounter[divisor] = (modCounter[divisor]!! * modCounter[divisor]!!) % divisor
        }
    }

    fun isDivisableBy(divisor: Int): Boolean {
        return modCounter[divisor] == 0
    }



}