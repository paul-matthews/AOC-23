import ScratchGame.Companion.toGame
import ScratchGame.Companion.toGamesMap
import java.util.*

class ScratchGame(val id: Int, val winning: List<Int>, val card: List<Int>) {
    companion object {
        private val game_regex = Regex("""(?<id>\d+):\s+(?<win>(\d+\s+)*)\|(?<card>(\s+\d+)*)""")
        private val number_regex = Regex("""\b\d+\b""")
        private fun MatchResult.getId(): Int = groups["id"]?.value?.toInt() ?: -1
        private fun MatchResult.getWin(): String = groups["win"]?.value ?: ""
        private fun MatchResult.getCard(): String = groups["card"]?.value ?: ""
        private fun Sequence<MatchResult>.getNumbers(): List<Int> = toList().flatMap { listOf(it.value.toInt()) }
        fun Lines.toGamesMap() = fold(mutableMapOf<Int, ScratchGame>()) {games, line ->
            line.toGame()?.let {
                games.put(it.id, it)
            }
            games
        }

        fun String.toGame(): ScratchGame? {
            game_regex.find(this)?.let {match ->
                return ScratchGame(match.getId(), number_regex.findAll(match.getWin()).getNumbers(),
                    number_regex.findAll(match.getCard()).getNumbers())
            }
            return null
        }
    }

    fun getWinningNumbers() = winning.intersect(card.toSet())
    fun getScore() = getWinningNumbers().fold(0) {i, _ -> if (i == 0) 1 else i * 2 }
}

fun main() {
    val day = "Day04"
    fun Lines.part1() = sumOf { it.toGame()?.getScore() ?: 0 }

    fun Lines.part2(): Int {
        val toProcessScratchCards = LinkedList<Int>()
        var scratchcards = 0
        val games = toGamesMap()
        games.forEach() {(id, _) ->
            toProcessScratchCards.push(id)
            while (toProcessScratchCards.size > 0) {
                val current = toProcessScratchCards.pop() ?: break
                scratchcards += 1

                games[current]?.let {currentGame ->
                    val winningCount = currentGame.getWinningNumbers().count()
                    if (winningCount > 0) {
                        val startAt = current + 1
                        val endAt = startAt+winningCount - 1
                        (startAt..endAt).map {id ->
                            toProcessScratchCards.push(id)
                        }
                    }
                } ?: break
            }
        }
        return scratchcards
    }

    // test if implementation meets criteria from the description, like:
    readInput("${day}_test")
        .part1()
        .check(13)

    readInput("${day}_test")
        .part2()
        .check(30)

    // Calculate the real output
    readInput(day)
        .part1()
        .println("Part 1: ")

    readInput(day)
        .part2()
        .println("Part 2: ")
}
