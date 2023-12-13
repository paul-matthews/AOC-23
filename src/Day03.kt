import Schematic.Companion.toSchematic

fun Int.isWithin(start:Int, end:Int) = (start..end).contains(this)
typealias Point = Pair<Int, Int>
class Item(val start: Point, val end: Point, val value: String) {
    fun hasAdjacentSymbol(symbols: List<Point>): Boolean =
        symbols.any {
            // Check there's a symbol on current, previous or next line
            it.first.isWithin(start.first - 1, start.first + 1) &&
                    // That is next to the number
                    it.second.isWithin(start.second - 1, end.second + 1)
        }
}
data class Schematic(val items: List<Item>, val symbols: List<Point>) {
    companion object {
        private val sym_regex = Regex("""(?<sym>[^\w.\n]{1})""")
        private val num_regex = Regex("""(?<num>\d+)""")

        fun Lines.toSchematic(): Schematic {
            val symbols: MutableList<Point> = mutableListOf()
            // For Each Line
            return Schematic(flatMapIndexed {i, line ->
                // Find all the symbols
                sym_regex.findAll(line).forEach {match ->
                    match.groups["sym"]?.let {
                        // Store them
                        symbols.add(Point(i, it.range.first))
                    }
                }
                // Find all the numbers
                num_regex.findAll(line).fold(mutableListOf<Item>()) {items, match ->
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
        items.filter { it.hasAdjacentSymbol(symbols) }
}

fun main() {
    val day = "Day03"
    fun Lines.part1() =
        toSchematic()
            .getPartNumbers()
            .sumOf { it.value.toInt() }

//    fun Lines.part2() =

    // test if implementation meets criteria from the description, like:
    readInput("${day}_test")
        .part1()
        .check(4361)

    // Calculate the real output
    readInput(day)
        .part1()
        .println("Part 1: ")
//
//    readInput(day)
//        .part2()
//        .println("Part 2: ")
}
