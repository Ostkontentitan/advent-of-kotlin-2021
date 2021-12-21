package day14

import readInput

suspend fun puzzle() {
    val inputs = readInput(14)
    val sumPartOne = ActualPolymerization().performAndCollectSum(10, inputs)
    println("Sum after 10 steps: $sumPartOne")

    val sumPartTwo = CountProjection().perform(40, inputs)
    println("Sum after 40 steps: $sumPartTwo")
}