import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.runBlocking
import java.io.File

fun main(args: Array<String>) = runBlocking(CoroutineName("AdventOfCoroutines")) {
    println("Happy Kotlin-Advent!")

    val puzzleNumber = args[0].toInt()
    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    println("Solving Puzzle number: $puzzleNumber")
    solvePuzzle(puzzleNumber)
}

suspend fun solvePuzzle(puzzleNumber: Int): Unit = when (puzzleNumber) {
    1 -> {
        puzzleDayOnePartOne()
        puzzleDayOnePartTwo()
    }
    2 -> {
        puzzleDayTwoPartOne()
        puzzleDayTwoPartTwo()
    }
    3 -> {
        puzzleDayThreePartOne()
        puzzleDayThreePartTwo()
    }
    4 -> {
        puzzleDayFourPartOne()
        puzzleDayFourPartTwo()
    }
    5 -> {
        puzzleDayFivePartOne()
        puzzleDayFivePartTwo()
    }
    6 -> {
        puzzleDaySixPartOne()
        puzzleDaySixPartTwo()
    }
    7 -> {
        puzzleDaySevenPartOne()
        puzzleDaySevenPartTwo()
    }
    8 -> {
        puzzleDayEightPartOne()
        puzzleDayEightPartTwo()
    }
    9 -> {
        puzzleDayNinePartOne()
        puzzleDayNinePartTwo()
    }
    10 -> {
        puzzleDayTenPartOne()
        puzzleDayTenPartTwo()
    }
    11 -> {
        puzzleDayElevenPartOne()
        puzzleDayElevenPartTwo()
    }
    12 -> {
        puzzleDayTwelvePartOne()
        puzzleDayTwelvePartTwo()
    }
    13 -> {
        puzzleDayThirteenPartOne()
        puzzleDayThirteenPartTwo()
    }
    14 -> {
        puzzleDayFourteenPartOne()
        puzzleDayFourteenPartTwo()
    }
    15 -> {
        puzzleDayFifteenPartOne()
    }
    16 -> {
        puzzleDaySixteenPartOne()
    }
    else -> {
        println("Sorry, looks like puzzle number $puzzleNumber is not solved yet. ")
    }
}

fun readInput(number: Int): List<String> = readInput("input$number.txt")
fun readInput(filename: String): List<String> = File(ClassLoader.getSystemResource(filename).file).readLines()
