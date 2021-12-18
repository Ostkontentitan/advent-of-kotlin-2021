import java.lang.IllegalStateException

fun puzzleDayFourPartOne() {
    val inputs = readInput(4).filter { it.isNotEmpty() }

    val drawnNumbers = inputs[0].split(",")
    val boards: List<BingoBoard> = constructBoards(inputs)

    var finished = false

    drawnNumbers.forEach { number ->
        if (finished) {
            return@forEach
        }

        boards.forEach { it.checkNumber(number) }
        val completedBoard = boards.firstOrNull { it.isCompleted() }
        if (completedBoard != null) {

            val uncheckedValues =
                completedBoard.rows.flatMap { row -> row.filter { !it.checked }.map { it.value.toInt() } }
            val finalResults = uncheckedValues.sum() * number.toInt()
            println("First winning bingo board score: $finalResults")
            finished = true
            return
        }
    }
}

fun puzzleDayFourPartTwo() {
    val inputs = readInput(4).filter { it.isNotEmpty() }

    val drawnNumbers = inputs[0].split(",")
    var boards: List<BingoBoard> = constructBoards(inputs)

    var lastWinningNumber = -1
    var lastWinningBoard: BingoBoard? = null

    drawnNumbers.forEach { number ->
        boards.forEach { it.checkNumber(number) }
        val (completedBoards, notCompletedBoards) = boards.partition { it.isCompleted() }
        boards = notCompletedBoards
        if (completedBoards.isNotEmpty()) {
            lastWinningNumber = number.toInt()
            lastWinningBoard = completedBoards.last()
        }
    }

    val uncheckedValues =
        lastWinningBoard!!.rows.flatMap { row -> row.filter { !it.checked }.map { it.value.toInt() } }
    val finalResults = uncheckedValues.sum() * lastWinningNumber

    println("Last winning bingo board score: $finalResults")
}

fun constructBoards(inputs: List<String>): List<BingoBoard> {
    val boardsInputs = inputs.subList(1, inputs.lastIndex + 1)
    return boardsInputs.windowed(5, 5).map { rawRows ->
        val rows = rawRows.map { rowValue ->
            rowValue.trim().replace("  ", " ").split(" ").map {
                if (it.isNotEmpty()) Entry(it) else throw IllegalStateException("Malformed input for row value in row.: $rowValue")
            }
        }
        val columns = rows.indices.map { index ->
            rows.map { it[index] }
        }
        BingoBoard(rows, columns)
    }
}

private fun BingoBoard.checkNumber(number: String) {
    rows.forEach { row ->
        row.forEach {
            if (it.value == number) {
                it.check()
            }
        }
    }
}

private fun BingoBoard.isCompleted(): Boolean {
    val completedRow = rows.firstOrNull { it.isComplete() }
    val completedColumn = columns.firstOrNull { it.isComplete() }
    return completedRow != null || completedColumn != null
}

private fun List<Entry>.isComplete() = all { it.checked }

data class BingoBoard(val rows: List<Row>, val columns: List<Column>)

typealias Row = List<Entry>
typealias Column = List<Entry>

data class Entry(val value: String) {

    var checked: Boolean = false
        private set

    fun check() {
        checked = true
    }
}
