import day14.ActualPolymerization
import day14.CountProjection
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals

internal class Day14KtTest {

    @Test
    fun `should properly perform 10 steps and give correct substract`() = runBlocking(CoroutineName("test")) {
        val inputs = readInput("testInput14.txt")
        val sum = ActualPolymerization().performAndCollectSum(10, inputs)

        assertEquals(1588, sum)
    }

    @Test
    fun `should properly perform 1 step and give correct substract via count prediction`() {
        val inputs = readInput("testInput14.txt")
        val sum = CountProjection().perform(1, inputs)

        assertEquals(1, sum)
    }

    @Test
    fun `should properly perform 2 steps and give correct substract via count prediction`() {
        val inputs = readInput("testInput14.txt")
        val sum = CountProjection().perform(2, inputs)
        val example = "NBCCNBBBCBHCB".groupingBy { it }.eachCount().values.sorted()
        val expected = example.maxOf { it } - example.minOf { it }

        assertEquals(expected.toLong(), sum)
    }

    @Test
    fun `should properly perform 10 steps and give correct substract via count prediction`() {
        val inputs = readInput("testInput14.txt")
        val sum = CountProjection().perform(10, inputs)

        assertEquals(1588, sum)
    }

    @Test
    fun `should properly perform 40 steps and give correct substract via count prediction`() {
        val inputs = readInput("testInput14.txt")
        val sum = CountProjection().perform(40, inputs)

        assertEquals(2188189693529L, sum)
    }
}
