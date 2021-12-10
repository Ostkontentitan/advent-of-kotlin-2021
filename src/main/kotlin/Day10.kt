fun puzzleDayTenPartOne() {
    val inputs = readInput(10)
    val score = inputs.mapNotNull { findFirstIllegalChar(it) }.sumOf { ERROR_SCORE_MAP[it]!! }
    println("Syntax error score: $score")
}

fun puzzleDayTenPartTwo() {
    val inputs = readInput(10)
    val sortedCompleteScores = calculateCompletionScoresAndSort(inputs)
    val middleIndex = sortedCompleteScores.size / 2
    val middleScore = sortedCompleteScores[middleIndex]
    println("Autocompletion score: $middleScore")
}

fun calculateCompletionScoresAndSort(inputs: List<String>) =
    inputs.filter { findFirstIllegalChar(it) == null }.map {
        openBracketsFor(it).reversed().fold(0L) { acc, bracket ->
            acc * 5 + bracket.autoCompleteScore
        }
    }.sorted()

fun findFirstIllegalChar(line: String): Char? {
    val openBrackets: MutableList<OpenBracket> = mutableListOf()

    line.forEach { currentChar ->
        when {
            isClosingChar(currentChar) -> {
                if (currentChar == openBrackets.last().closingChar) {
                    openBrackets.removeAt(openBrackets.lastIndex)
                } else {
                    return@findFirstIllegalChar currentChar
                }
            }
            isOpenChar(currentChar) -> {
                openBrackets += bracketFor(currentChar)
            }
        }
    }

    return null
}

fun openBracketsFor(line: String): List<OpenBracket> {
    val openBrackets: MutableList<OpenBracket> = mutableListOf()

    line.forEach { currentChar ->
        when {
            isClosingChar(currentChar) -> {
                if (currentChar == openBrackets.last().closingChar) {
                    openBrackets.removeAt(openBrackets.lastIndex)
                }
            }
            isOpenChar(currentChar) -> {
                openBrackets += bracketFor(currentChar)
            }
        }
    }

    return openBrackets
}

fun bracketFor(char: Char): OpenBracket = when (char) {
    '(' -> OpenBracket.ROUND
    '{' -> OpenBracket.CURLY
    '[' -> OpenBracket.RECTY
    '<' -> OpenBracket.POINTY
    else -> throw IllegalArgumentException("Unhandled bracked type: $char")
}

private val CLOSING_CHARS = setOf(')', ']', '}', '>')
private val OPEN_CHARS = setOf('(', '[', '{', '<')

fun isClosingChar(char: Char) = CLOSING_CHARS.contains(char)
fun isOpenChar(char: Char) = OPEN_CHARS.contains(char)

enum class OpenBracket(val closingChar: Char, val autoCompleteScore: Int) {
    CURLY('}', 3),
    RECTY(']', 2),
    ROUND(')', 1),
    POINTY('>', 4);
}

private val ERROR_SCORE_MAP: Map<Char, Int> = mapOf(
    ')' to 3,
    ']' to 57,
    '}' to 1197,
    '>' to 25137
)