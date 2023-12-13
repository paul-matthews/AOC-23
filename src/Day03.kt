import Schematic.Companion.toSchematic

fun Int.isWithin(start:Int, end:Int) = (start..end).contains(this)
typealias Point = Pair<Int, Int>
class Item(private val start: Point, private val end: Point, val value: String) {
    fun getMatchedSymbols(symbols: List<Item>): List<Item> =
        symbols.filter {
            // Check there's a symbol on current, previous or next line
            it.start.first.isWithin(start.first - 1, start.first + 1) &&
                    // That is next to the number
                    it.start.second.isWithin(start.second - 1, end.second + 1)
        }
}
data class Schematic(val items: List<Item>, val symbols: List<Item>) {
    companion object {
        private val sym_regex = Regex("""(?<sym>[^\w.\n])""")
        private val num_regex = Regex("""(?<num>\d+)""")

        fun Lines.toSchematic(): Schematic {
            val symbols: MutableList<Item> = mutableListOf()
            // For Each Line
            return Schematic(flatMapIndexed {i, line ->
                // Find all the symbols
                sym_regex.findAll(line).forEach {match ->
                    match.groups["sym"]?.let {
                        // Store them
                        val pt = Point(i, it.range.first)
                        symbols.add(Item(pt, pt, it.value))
                    }
                }
                // Find all the numbers
                num_regex.findAll(line).fold(mutableListOf()) {items, match ->
                    match.groups["num"]?.let {
                        // Store them
                        items.add(Item(Point(i, it.range.first), Point(i, it.range.last), it.value))
                    }
                    items
                }
            }, symbols)
        }
    }

    fun getPartNumbers(): List<Item> =
        items.filter { it.getMatchedSymbols(symbols).isNotEmpty() }

    fun getGearNumbers(): List<Pair<Item, Item>> {
        val gearSymbols = symbols.filter { it.value == "*" }
        val gearMatches: Map<Item, MutableList<Item>> = gearSymbols.associateWith { mutableListOf() }
        items.forEach {item ->
            item.getMatchedSymbols(gearSymbols).map {
                gearMatches[it]?.add(item)
            }
        }
        return gearMatches
            .filter {(_, items) -> items.size == 2}
            .flatMap { listOf(Pair(it.value.first(), it.value[1]))}

    }
}

fun main() {
    val day = "Day03"
    fun Lines.part1() =
        toSchematic()
            .getPartNumbers()
            .sumOf { it.value.toInt() }

    fun Lines.part2() =
        toSchematic()
            .getGearNumbers()
            .sumOf { it.first.value.toInt() * it.second.value.toInt() }

    // test if implementation meets criteria from the description, like:
    readInput("${day}_test")
        .part1()
        .check(4361)

    // Calculate the real output
    val part1 = readInput(day)
        .part1()
    part1
        .println("Part 1: ")
    part1
        .check(540212)

    readInput(day)
        .part2()
        .println("Part 2: ")
}
