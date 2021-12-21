import day15.CavePosition
import day15.revealActualCave
import day15.searchOptimalPath
import day15.toRiskMap
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class Day15KtTest {

    @Test
    fun `should find the shortest way in example`() = runBlocking {
        val inputs = readInput("testInput15.txt").toRiskMap()
        val path = searchOptimalPath(inputs, 10_000)
        assertEquals(40, path)
    }

    @Test
    fun `should find the shortest way in extended`() = runBlocking {
        val inputs = readInput("testInput15_extended.txt").toRiskMap()
        val path = searchOptimalPath(inputs, 10_000)
        assertEquals(49, path)
    }

    @Test
    fun `should find the shortest way in custom example`() = runBlocking {
        val inputs = readInput("testInput15_alt.txt").toRiskMap()
        val path = searchOptimalPath(inputs, 25_000)
        assertEquals(32, path)
    }

    @Test
    fun `should correctly reveal full map`() = runBlocking {
        val inputs = readInput("testInput15.txt").toRiskMap()
        val expected = readInput("testInput15_part2.txt").toRiskMap()

        val actual = revealActualCave(inputs)
        assertEquals(expected.sumOf { it.contentHashCode() }, actual.sumOf { it.contentHashCode() })
    }

    @Test
    fun `finds optimal path in revealed example`() = runBlocking {
        val inputs = readInput("testInput15.txt").toRiskMap()

        val revealed = revealActualCave(inputs)
        val best = searchOptimalPath(revealed, 30_000)

        assertEquals(315, best)
    }

    @Test
    fun `detects proximity as expected`() = runBlocking {
        assertFalse(CavePosition(0, 0, 0).inProximityTo(CavePosition(2, 0, 0)))
        assertTrue(CavePosition(0, 0, 0).inProximityTo(CavePosition(0, 1, 0)))
        assertFalse(CavePosition(0, 0, 0).inProximityTo(CavePosition(1, 1, 0)))
        assertFalse(CavePosition(0, 0, 0).inProximityTo(CavePosition(2, 0, 0)))
    }
}
