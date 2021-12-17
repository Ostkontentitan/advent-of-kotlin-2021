
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

suspend fun puzzleDayFourteenPartOne() {
    val inputs = readInput(14)
    val instructions = extractInstructionPairs(inputs)
    val pairs = extractPolyTemplate(inputs.first())
    val final = performReplacementStepsAlt(pairs, instructions, 15)
    val counts = countCharsIn(final).values.sorted()
    val sum = counts.maxOf { it } - counts.minOf { it }
    println("Sum after 10 steps: $sum")
}

suspend fun puzzleDayFourteenPartTwo() {
    val inputs = readInput(14)
    val instructions = extractInstructionPairs(inputs)
    val pairs = extractPolyTemplate(inputs.first())
    val final = performReplacementStepsAlt(pairs, instructions, 40)
    val counts = countCharsIn(final).values.sorted()
    val sum = counts.maxOf { it } - counts.minOf { it }
    println("Sum after 40 steps: $sum")
}

suspend fun countCharsIn(sequence: Flow<String>) = withContext(Dispatchers.Default) {
    sequence.fold(mapOf<Char, Long>()) { acc, value ->
        val new = value.first()
        acc + (new to (acc[new] ?: 0L) + 1L)
    }
}

tailrec fun performReplacementStepsAlt(
    polyChain: Flow<String>,
    instructions: Map<String, Char>,
    steps: Int
): Flow<String> {
    return if (steps > 0) {
        performReplacementStepsAlt(performReplacementStep(polyChain, instructions), instructions, steps - 1)
    } else {
        polyChain
    }
}

@OptIn(FlowPreview::class)
fun performReplacementStep(polyChain: Flow<String>, instructions: Map<String, Char>): Flow<String> =
    polyChain.map { x -> performInsertion(x[0], x.getOrNull(1), instructions) }.flattenMerge()

suspend fun performInsertion(x: Char, y: Char?, instructions: Map<String, Char>): Flow<String> =
    flow {
        if(y == null) {
            emit(x.toString())
            return@flow
        }
        val segment = "$x$y"
        val replacementElement = instructions[segment]
        if (replacementElement != null) {
            emit("$x$replacementElement")
            emit("$replacementElement$y")
        } else {
            emit("$x$y")
        }
    }

fun extractInstructionPairs(inputs: List<String>): Map<String, Char> = inputs.subList(2, inputs.size).associate {
    val (target, replace) = it.split(" -> ")
    target to replace[0]
}

fun extractPolyTemplate(input: String): Flow<String> = input.windowed(size = 2, partialWindows = true).asFlow()
