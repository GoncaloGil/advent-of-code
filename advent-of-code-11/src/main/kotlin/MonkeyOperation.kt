import java.lang.Exception

class MonkeyOperation(
    val operator: Char,
    val constant: String
) {
    fun calculateResult(value: WorryLevel) {
        if(constant == "old") {
            return value.square()
        }
        return when (operator) {
            '*' -> value.multiply(constant.toInt())
            '+' -> value.add(constant.toInt())
            else -> throw Exception("Operation not allowed")
        }
    }

    fun verifyCondition(value: WorryLevel): Boolean {
        return value.isDivisableBy(constant.toInt())
    }
}