import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class Day15KtTest {

    @Test
    fun `should find the shortest way in example`() = runBlocking {
        val inputs = readInput("testInput15.txt").toCavePositions()
        val startRisk = inputs[0][0]
        val path = searchOptimalPath(inputs, 10_000)!!
        assertEquals(40, path.sumOf { it.risk } - startRisk)
    }

    @Test
    fun `should find the shortest way in extended`() = runBlocking {
        val inputs = readInput("testInput15_extended.txt").toCavePositions()
        val startRisk = inputs[0][0]
        val path = searchOptimalPath(inputs, 10_000)!!
        assertEquals(49, path.sumOf { it.risk } - startRisk)
    }

    @Test
    fun `should find the shortest way in custom example`() = runBlocking {
        val inputs = readInput("testInput15_alt.txt").toCavePositions()
        val startRisk = inputs[0][0]
        val path = searchOptimalPath(inputs, 25_000)!!
        assertEquals(32, path.sumOf { it.risk } - startRisk)
    }

    @Test
    fun `detects proximity as expected`() = runBlocking {
        assertFalse(CavePosition(0, 0, 0).inProximityTo(CavePosition(2, 0, 0)))
        assertTrue(CavePosition(0, 0, 0).inProximityTo(CavePosition(0, 1, 0)))
        assertFalse(CavePosition(0, 0, 0).inProximityTo(CavePosition(1, 1, 0)))
        assertFalse(CavePosition(0, 0, 0).inProximityTo(CavePosition(2, 0, 0)))
    }
}
