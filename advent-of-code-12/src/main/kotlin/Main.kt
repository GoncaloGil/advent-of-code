import java.io.File

fun main(args: Array<String>) {
    val lines = File("src/main/resources/puzzle.txt").readLines()
    val grid: Array<CharArray> = lines.map { it.toCharArray() }.toTypedArray()

    val square = Square(0, 0, 'a')
    square.getNeighbors(grid)
    println(square.neighbors)
}


data class Square(
    val x: Int,
    val y: Int,
    val height: Char
) {
    lateinit var neighbors: List<Square>

    fun getNeighbors(grid: Array<CharArray>) {
        val neighborsAux = mutableListOf<Square>()

        if(x > 0) {

        }
        val left = grid[y][x-1].let {  }
        for (tempY in intArrayOf(y - 1, 0).max()..intArrayOf(y + 1, grid.size).min()) {
            for (tempX in intArrayOf(x - 1, 0).max()..intArrayOf(x + 1, grid[tempY].size - 1).min()) {

            }
        }
        neighbors = neighborsAux

    }

    
}