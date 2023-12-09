import Game.Companion.toGame

data class Game(val id: Int = -1, val blue: Int = 0, val red: Int = 0, val green: Int = 0) {
    companion object {
        private val color_regex = Regex("""(Game (?<id>\d+)|(?<cubes>\d+) (?<color>blue|red|green))+""")
        private val getNumMatch: (MatchResult) -> Int = {match -> match.groups["cubes"]?.value?.toInt() ?: 0}
        private val getColorOrGame: (MatchResult) -> String = {match -> match.groups["color"]?.value ?: "game"}
        private val getId: (MatchResult) -> Int = {match -> match.groups["id"]?.value?.toInt() ?: -1}

        fun String.toGame(): Game {
            val colorMatches = color_regex.findAll(this)
            return colorMatches.fold(Game()) {g, match ->
                when(getColorOrGame(match)) {
                    "red" -> g.maxOfRed(getNumMatch(match))
                    "green" -> g.maxOfGreen(getNumMatch(match))
                    "blue" -> g.maxOfBlue(getNumMatch(match))
                    else -> g.setId(getId(match))
                }
            }
        }
    }
    fun setId(v: Int) = copy(id = v)
    fun maxOfBlue(v: Int) = copy(blue = maxOf(blue, v))
    fun maxOfRed(v: Int) = copy(red = maxOf(red, v))
    fun maxOfGreen(v: Int) = copy(green = maxOf(green, v))

    fun isPossible(bag: Game): Boolean = (red <= bag.red && green <= bag.green && blue <= bag.blue)
}

fun main() {
    val day = "Day02"
    fun Lines.part1(bag: Game) =
        map {it.toGame()}
        .filter { it.isPossible(bag) }
        .sumOf { it.id }

    // test if implementation meets criteria from the description, like:
    readInput("${day}_test_p1")
        .part1(Game(red = 12, green = 13, blue = 14))
        .check(8)

    // test if implementation meets criteria from the description, like:
    readInput(day)
        .part1(Game(red = 12, green = 13, blue = 14))
        .println("Part 1: ")
}
