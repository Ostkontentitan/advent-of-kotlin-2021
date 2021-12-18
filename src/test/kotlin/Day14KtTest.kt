import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals

internal class Day14KtTest {

    @Test
    fun `should properly perform 1 replaces correctly`() = runBlocking {
        val inputs = readInput("testInput14.txt")
        val instructions = extractInstructionPairs(inputs)
        val polys = extractPolyTemplate(inputs.first())

        val result = performReplacementStepsAlt(polys, instructions, 1).map { it[0] }.toList().joinToString("")

        assertEquals("NCNBCHB", result)
    }

    @Test
    fun `should properly perform 2 replaces correctly`() = runBlocking {
        val inputs = readInput("testInput14.txt")
        val instructions = extractInstructionPairs(inputs)
        val polys = extractPolyTemplate(inputs.first())

        val result = performReplacementStepsAlt(polys, instructions, 2).map { it[0] }.toList().joinToString("")

        assertEquals("NBCCNBBBCBHCB", result)
    }

    @Test
    fun `should properly perform 3 replaces correctly`() = runBlocking {
        val inputs = readInput("testInput14.txt")
        val instructions = extractInstructionPairs(inputs)
        val polys = extractPolyTemplate(inputs.first())

        val result = performReplacementStepsAlt(polys, instructions, 3).map { it[0] }.toList().joinToString("")

        assertEquals("NBBBCNCCNBBNBNBBCHBHHBCHB", result)
    }

    @Test
    fun `should properly perform 4 replaces correctly`() = runBlocking {
        val inputs = readInput("testInput14.txt")
        val instructions = extractInstructionPairs(inputs)
        val polys = extractPolyTemplate(inputs.first())

        val result = performReplacementStepsAlt(polys, instructions, 4).map { it[0] }.toList().joinToString("")

        assertEquals("NBBNBNBBCCNBCNCCNBBNBBNBBBNBBNBBCBHCBHHNHCBBCBHCB", result)
    }

    @Test
    fun `should properly perform 40 steps and give correct substract`() = runBlocking(CoroutineName("test")) {
        val inputs = readInput("testInput14.txt")
        val instructions = extractInstructionPairs(inputs)
        val polys = extractPolyTemplate(inputs.first())

        val result = performReplacementStepsAlt(polys, instructions, 25)
        val counts = countCharsIn(result).values.sorted()
        val sum = counts.maxOf { it } - counts.minOf { it }

        assertEquals(2188189693529L, sum)
    }
}
