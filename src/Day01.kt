fun main() {
    val day = "Day01"
    val numberWords: Map<String, String> = (1..9).associate { it.toString() to it.toString() } +
            mapOf("one" to 1, "two" to 2, "three" to 3, "four" to 4, "five" to 5, "six" to 6, "seven" to 7,
                "eight" to 8, "nine" to 9).mapValues { it.value.toString() }

    fun List<String>.part1(): Int = sumOf {line ->
        (line.first{it.isDigit()}.toString() + line.last{it.isDigit()}.toString()).toInt()
    }

    fun List<String>.part2(): Int = sumOf {line ->
        val valueFirst = with(numberWords) {
            numberWords.getOrDefault(
                line.findAnyOf(keys, ignoreCase = true)?.second ?: "",
                "0")
        }
        val valueLast = with(numberWords) {
            numberWords.getOrDefault(
                line.findLastAnyOf(keys, ignoreCase = true)?.second ?: "",
                "0")
        }
        (valueFirst + valueLast).toInt()
    }

    // test if implementation meets criteria from the description, like:
    val testP1Input = readInput("${day}_test_p1")
    val testP2Input = readInput("${day}_test_p2")
    check(testP1Input.part1() == 142)
    check(testP2Input.part2() == 281)

    val input = readInput("Day01")
    println("$day Part1: " + input.part1())
    println("$day Part2: " + input.part2())
}
