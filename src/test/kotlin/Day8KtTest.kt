import org.junit.Test
import kotlin.test.assertEquals

internal class Day8KtTest {
    @Test
    fun `counts 1,4,7,8 correctly for test input`() {
        val inputs = readInput("testInput8.txt")
        val structured = structureInputs(inputs)
        val count = structured.countOneFourSevenEight()
        assertEquals(26, count)
    }

    @Test
    fun `decodes and sums exmaple correctly`() {
        val inputs = readInput("testInput8.txt")
        val structured = structureInputs(inputs)

        val sum = structured.sumOf { pairsList ->
            val codes = decode(pairsList.first)
            pairsList.second.map { codes[it.toSet()] }.joinToString(separator = "").toInt()
        }

        assertEquals(61229, sum)
    }
}