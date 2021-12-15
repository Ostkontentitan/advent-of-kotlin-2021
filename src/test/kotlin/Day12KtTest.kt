import org.junit.Test
import kotlin.test.assertEquals

internal class Day12KtTest {

    @Test
    fun `can determine unique pathes for simple example`() {
        val inputs = readInput("testInput12_1.txt")
        val connections = inputsToConnections(inputs)
        val pathes = findUniquePaths(Cave.Start, connections, emptyMap())
        assertEquals(10, pathes)
    }

    @Test
    fun `can determine unique pathes for moderate example`() {
        val inputs = readInput("testInput12_2.txt")
        val connections = inputsToConnections(inputs)
        val pathes = findUniquePaths(Cave.Start, connections, emptyMap())
        assertEquals(19, pathes)
    }

    @Test
    fun `can determine unique pathes for big example`() {
        val inputs = readInput("testInput12_3.txt")
        val connections = inputsToConnections(inputs)
        val pathes = findUniquePaths(Cave.Start, connections, emptyMap())
        assertEquals(226, pathes)
    }

    @Test
    fun `can determine not quite unique pathes for simple example`() {
        val inputs = readInput("testInput12_1.txt")
        val connections = inputsToConnections(inputs)
        val pathes = findUniquePaths(Cave.Start, connections, emptyMap(), true)
        assertEquals(36, pathes)
    }

    @Test
    fun `can determine not quite pathes for moderate example`() {
        val inputs = readInput("testInput12_2.txt")
        val connections = inputsToConnections(inputs)
        val pathes = findUniquePaths(Cave.Start, connections, emptyMap(), true)
        assertEquals(103, pathes)
    }

    @Test
    fun `can determine not quite pathes for big example`() {
        val inputs = readInput("testInput12_3.txt")
        val connections = inputsToConnections(inputs)
        val pathes = findUniquePaths(Cave.Start, connections, emptyMap(), true)
        assertEquals(3509, pathes)
    }
}