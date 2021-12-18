import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class Day15KtTest {

    @Test
    fun `should find the shortest way in example`() = runBlocking {
        val inputs = readInput("testInput15.txt").toCavePositions()
        val lowestRisk = bruteForce(CavePosition(9, 9, 1), inputs, runs = 15_000)
        assertEquals(40, lowestRisk.totalRisk)
    }

    @Test
    fun `detects proximity as expected`() = runBlocking {
        assertFalse(CavePosition(0, 0, 0).inProximityTo(CavePosition(2, 0, 0)))
        assertTrue(CavePosition(0, 0, 0).inProximityTo(CavePosition(0, 1, 0)))
        assertFalse(CavePosition(0, 0, 0).inProximityTo(CavePosition(1, 1, 0)))
    }
}
