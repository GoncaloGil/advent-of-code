import java.io.File
import kotlin.math.absoluteValue

fun main(args: Array<String>) {
    val inputStream = File("src/main/resources/puzzle.txt").inputStream().bufferedReader()
    val lines = inputStream.readLines()

    val processor = Processor()
    lines.forEach { processor.readPuzzleLine(it) }
    println(processor.getSinalStrenght())
    processor.renderCrt()
    processor.crt.forEach {
        it.forEach { print(it) }
        println()

    }
}

class Processor() {
    val instructions: MutableList<Instruction> = mutableListOf()
    private var numberOfCycles: Int = 1
    private var registerValue: Int = 1
    val crt: Array<Array<Char>> = Array(6) { Array(40) {'.'} }

    fun readPuzzleLine(puzzleLine: String) {
        val args = puzzleLine.split(" ")
        if(args[0] == "addx") {
            instructions.add(Instruction(numberOfCycles,registerValue,2,args[1].toInt()))
            registerValue += args[1].toInt()
            numberOfCycles += 2
        } else if (args[0] == "noop") {
            instructions.add(Instruction(numberOfCycles, registerValue,1, 0))
            numberOfCycles ++;
        }
    }

    fun getRegisterValueAtCycle(cycle: Int): Int {
        val registerValue = 1
        val instruction = instructions.filter { it.firstCycle <= cycle && cycle <= it.lastCycle }.first()

        return if(cycle == instruction.lastCycle ) instruction.initialRegisterValue + instruction.instructionValue
        else instruction.initialRegisterValue
    }

    fun getSinalStrenght(): Int {
        return intArrayOf(20, 60, 100, 140, 180, 220).sumOf { it*getRegisterValueAtCycle(it) }
    }

    fun renderCrt() {
        for (cycle in 1..240) {
            var spritePosition = getRegisterValueAtCycle(cycle)
            val pixelRow = (cycle-1)/ CRT_COLUMNS
            val pixelColumn = (cycle-1) % CRT_COLUMNS
            if((pixelColumn-spritePosition).absoluteValue <= 1) {
                crt[pixelRow][pixelColumn] = '#'
            }
        }
    }

    companion object {
        const val CRT_COLUMNS = 40
    }

}
data class Instruction(
    val firstCycle: Int,
    val initialRegisterValue: Int,
    val numberOfCycles: Int,
    val instructionValue: Int,

) {
    val lastCycle = firstCycle + numberOfCycles
}