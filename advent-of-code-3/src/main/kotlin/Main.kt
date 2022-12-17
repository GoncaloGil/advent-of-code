import java.awt.font.NumericShaper.Range
import java.io.File
import java.io.InputStream
import kotlin.properties.Delegates

fun main(args: Array<String>) {
    val inputStream: InputStream = File("src/main/resources/puzzle.txt").inputStream()
    val lineList = inputStream.bufferedReader().readLines()

    val elfGroups = mutableListOf<ElfGroup>()

    for(i in 0..(lineList.size-3) step 3) {
        elfGroups.add(ElfGroup(lineList.subList(i,i+3)))
    }

    val result = elfGroups.sumOf { it.badgePriority }
    println(result)
}

class Rucksack(
    private val puzzleLine: String
){
    private var compartments: List<CharSequence>
    var itemsMap: HashMap<Char, Int>
    var repeatedItem by Delegates.notNull<Char>()
    var repeatedItemPriority by Delegates.notNull<Int>()

    init {
        compartments = getCompartments(puzzleLine)
        itemsMap = orderItems(compartments)
        repeatedItemPriority = calculatePriority(repeatedItem)
    }

    private fun getCompartments(puzzleLine: String): List<CharSequence> {
        val compartments = mutableListOf<CharSequence>()
        compartments.add(puzzleLine.subSequence(0, puzzleLine.length/2))
        compartments.add(puzzleLine.subSequence(puzzleLine.length/2, puzzleLine.length))
        return compartments
    }

    private fun orderItems(compartments: List<CharSequence>): HashMap<Char, Int>{
        val itemsMap: HashMap<Char, Int> = HashMap()
        compartments.forEachIndexed { index, compartment ->
            compartment.forEach {
                if(!itemsMap.containsKey(it)){
                    itemsMap[it] = index
                } else {
                    if(itemsMap.get(it) != index) {
                        repeatedItem = it
                    }
                }
            }
        }
        return itemsMap;
    }

    private fun calculatePriority(char: Char): Int {
        if(char in 'a'..'z'){
            return char.code-96
        } else if (char in 'A' .. 'Z') {
            return char.code-38
        }
        return char.code
    }
}

class ElfGroup(
    private val puzzleSet: List<String>
) {
    val elfsSack: List<Rucksack> = puzzleSet.map { Rucksack(it) }
    val itemsElfMap: HashMap<Char,HashSet<Int>> = calculateItemsElfMap(elfsSack)
    val badge = itemsElfMap.keys.filter { itemsElfMap[it]?.size == 3 }.first()
    val badgePriority = calculatePriority(badge)

    private fun calculateItemsElfMap(elfSack: List<Rucksack>): HashMap<Char, HashSet<Int>> {
        val itemsElfMap = HashMap<Char, HashSet<Int>>()
        elfSack.forEachIndexed { index, rucksack ->
            rucksack.itemsMap.keys.forEach {
                if(!itemsElfMap.containsKey(it)) {
                    itemsElfMap[it] = HashSet()
                }
                itemsElfMap[it]?.add(index)
            }
        }
        return itemsElfMap
    }

    private fun calculatePriority(char: Char): Int {
        if(char in 'a'..'z'){
            return char.code-96
        } else if (char in 'A' .. 'Z') {
            return char.code-38
        }
        return char.code
    }

}