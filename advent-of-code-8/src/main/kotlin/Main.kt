import com.sun.source.tree.Tree
import java.io.File
import java.lang.Exception

fun main(args: Array<String>) {
    val inputStream = File("src/main/resources/puzzle.txt").inputStream().bufferedReader()
    val lines = inputStream.readLines()

    val treeMap = TreeMap(lines)

    treeMap.getBestHousePosition()
    println(treeMap.bestPositionPoints)

}

class TreeMap(
    private val puzzleLines: List<String>
) {
    val grid: List<List<Int>> = createGrid(puzzleLines)
    val transposedGrid = transposeGrid(grid)
    private val visibleTrees: HashSet<Position> = HashSet()
    var bestPositionPoints: Int = 0

    init {
        calculateVisibleTrees()
    }

    private fun createGrid(puzzleLines: List<String>): MutableList<MutableList<Int>> {
        val grid: MutableList<MutableList<Int>> = mutableListOf()

        puzzleLines.forEachIndexed { rowIndex, row ->
            grid.add(mutableListOf())
            row.forEach { height -> grid[rowIndex].add(height.digitToInt()) }
        }

        return grid
    }

    private fun transposeGrid(grid: List<List<Int>>): List<List<Int>> {
        val transposedGrid: MutableList<MutableList<Int>> = mutableListOf()

        repeat(grid[0].size) { transposedGrid.add(mutableListOf()) }

        grid.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { column, height ->
                transposedGrid[column].add(height)
            }
        }
        return transposedGrid
    }
    private fun calculateVisibleTrees() {
        grid.first().forEachIndexed { index, i -> addPosition(index,0,false,false) }
        grid.last().forEachIndexed { index, i -> addPosition(index,grid.size-1,false,false) }
        transposedGrid.first().forEachIndexed { index, i -> addPosition(index,0,false,true) }
        transposedGrid.last().forEachIndexed { index, i -> addPosition(index,transposedGrid.size-1,false,true) }

        grid.forEachIndexed { index, it ->
            calculateVisibleTreesRowByDirection(it.reversed(), index, reversed = true, transposed = false)
            calculateVisibleTreesRowByDirection(it, index, reversed = false, transposed = false)
        }
        transposedGrid.forEachIndexed { index, it ->
            calculateVisibleTreesRowByDirection(it.reversed(), index, reversed = true, transposed = true)
            calculateVisibleTreesRowByDirection(it, index, reversed = false, transposed = true)
        }
    }

    private fun calculateVisibleTreesRowByDirection(row: List<Int>, rowIndex: Int, reversed: Boolean, transposed: Boolean) {
        val maxHeight = row.max()
        val limitEdge = row.first()

        if(maxHeight == limitEdge) return

        val maxIndex = row.indexOf(maxHeight)
        addPosition(maxIndex, rowIndex, reversed, transposed)
        calculateVisibleTreesRowByDirection(row.take(maxIndex), rowIndex, reversed, transposed)
    }

    private fun addPosition(x: Int, y: Int, reversed: Boolean, transposed: Boolean) {
        var trueX = x
        var trueY = y
        val realRowSize = when(transposed) {
            true -> transposedGrid[y].size
            false -> grid[y].size
        }
        if(reversed) {
            trueX = realRowSize-x-1
        }

        if (transposed) {
            val temp = trueX
            trueX = trueY
            trueY = temp
        }
        visibleTrees.add( Position(trueX, trueY) )
    }

    fun getBestHousePosition() {
        grid.forEachIndexed { x, row ->
            row.forEachIndexed { y, houseHeight ->
                var positionValue = calculatePoints(Position(x,y),houseHeight)
                bestPositionPoints = arrayOf(bestPositionPoints, positionValue).max()
            }
        }
    }
    fun calculatePoints(pos: Position, houseHeight: Int): Int {
        val leftValue = calculateViewSize(houseHeight, grid[pos.y].take(pos.x).reversed())
        val rightValue = calculateViewSize(houseHeight, grid[pos.y].drop(pos.x +1))
        val upValue = calculateViewSize(houseHeight, transposedGrid[pos.x].take(pos.y).reversed())
        val downValue = calculateViewSize(houseHeight, transposedGrid[pos.x].drop(pos.y +1))

        return leftValue * rightValue * upValue * downValue
    }

    private fun calculateViewSize(houseHeight: Int, view: List<Int>): Int {
        var counter = 0;
        for(i in view) {
            counter ++
            if (i >= houseHeight)
                break;
        }
        return counter
    }
}

data class Position(
    val x: Int,
    val y: Int
)