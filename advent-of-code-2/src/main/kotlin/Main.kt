import java.io.File
import java.io.InputStream
import java.lang.Exception
import kotlin.properties.Delegates

fun main(args: Array<String>) {
    println("Hello World!")

    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    println("Program arguments: ${args.joinToString()}")
    val rounds = Round.readFile()
    val result = rounds.sumOf { it.points }
    println(result)
}

enum class RoundResult(val points: Int) {
    LOST(0), DRAW(3), WIN(6)
}

enum class Options(val points: Int){
    ROCK(1), PAPER(2), SCISSORS(3)
}

enum class OptionsMapper(val wins: Options, val loose: Options, val draw: Options) {
    ROCK(Options.SCISSORS, Options.PAPER, Options.ROCK),
    PAPER(Options.ROCK, Options.SCISSORS, Options.PAPER),
    SCISSORS(Options.PAPER, Options.ROCK, Options.SCISSORS)
}

class Round(
    private val puzzleLine: String
) {
    lateinit var opponent: OptionsMapper
    lateinit var me: Options
    lateinit var result: RoundResult
    var points by Delegates.notNull<Int>()

    init {
        this.convertLineToResult(puzzleLine)
        this.calculatePoints()
    }

    private fun calculateResult(me: Options, opponent: OptionsMapper): RoundResult {
        return when(me){
            opponent.draw -> RoundResult.DRAW
            opponent.loose -> RoundResult.WIN
            opponent.wins -> RoundResult.LOST
            else -> { throw Exception("Round result not allowed")}
        }
    }

    private fun calculatePlay(opponent: OptionsMapper, result: RoundResult): Options {
        return when(result) {
            RoundResult.LOST -> opponent.wins
            RoundResult.DRAW -> opponent.draw
            RoundResult.WIN -> opponent.loose
        }
    }

    private fun calculatePoints() {
        points = result.points + me.points
    }

    private fun convertLineToPlays(line: String) {
        val roundBet = line.split(" ")
        opponent = when(roundBet[0]) {
            "A" -> OptionsMapper.ROCK
            "B" -> OptionsMapper.PAPER
            "C" -> OptionsMapper.SCISSORS
            else -> { throw Exception("Opponent option not allowed")}
        }
        me = when(roundBet[1]) {
            "X" -> Options.ROCK
            "Y" -> Options.PAPER
            "Z" -> Options.SCISSORS
            else -> {
                throw Exception("Me option not allowed")
            }
        }

        result =  calculateResult(me, opponent)
    }

    private fun convertLineToResult(line: String) {
        val roundBet = line.split(" ")
        opponent = when(roundBet[0]) {
            "A" -> OptionsMapper.ROCK
            "B" -> OptionsMapper.PAPER
            "C" -> OptionsMapper.SCISSORS
            else -> { throw Exception("Opponent option not allowed")}
        }
        result = when(roundBet[1]) {
            "X" -> RoundResult.LOST
            "Y" -> RoundResult.DRAW
            "Z" -> RoundResult.WIN
            else -> {
                throw Exception("Me option not allowed")
            }
        }

        me = calculatePlay(opponent, result)
    }

    companion object {
        fun readFile(): List<Round> {
            val inputStream: InputStream = File("src/main/resources/puzzle.txt").inputStream()
            val lineList = mutableListOf<String>()

            inputStream.bufferedReader().forEachLine { lineList.add(it) }
            val results = lineList.map { Round(it) }

            println(results.toString())
            return results
        }
    }
}

