import java.io.File

fun main(args: Array<String>) {
    println("Hello World!")

    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    println("Program arguments: ${args.joinToString()}")
    puzzleDayTwoPartTwo()
}

fun readInput(number: Int): List<String> = readInput("input${number}.txt")
fun readInput(filename: String): List<String> = File(ClassLoader.getSystemResource(filename).file).readLines()