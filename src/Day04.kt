import ScratchGame.Companion.toGame

class ScratchGame(val winning: List<Int>, val card: List<Int>) {
    companion object {
        private val game_regex = Regex(""":\s+(?<win>(\d+\s+)*)\|(?<card>(\s+\d+)*)""")
        private val number_regex = Regex("""\b\d+\b""")
        private fun MatchResult.getWin(): String = groups["win"]?.value ?: ""
        private fun MatchResult.getCard(): String = groups["card"]?.value ?: ""
        private fun Sequence<MatchResult>.getNumbers(): List<Int> = toList().flatMap { listOf(it.value.toInt()) }
        fun String.toGame(): ScratchGame? {
            game_regex.find(this)?.let {match ->
                return ScratchGame(number_regex.findAll(match.getWin()).getNumbers(),
                    number_regex.findAll(match.getCard()).getNumbers())
            }
            return null
        }
    }

    fun getScore() = winning.intersect(card).fold(0) {i, _ -> if (i == 0) 1 else i * 2 }
}

fun main() {
    val day = "Day04"
    fun Lines.part1() = sumOf { it.toGame()?.getScore() ?: 0 }

//    fun Lines.part2() =

    // test if implementation meets criteria from the description, like:
    readInput("${day}_test")
        .part1()
        .check(13)

    // Calculate the real output
    readInput(day)
        .part1()
        .println("Part 1: ")

//    readInput(day)
//        .part2()
//        .println("Part 2: ")
}
