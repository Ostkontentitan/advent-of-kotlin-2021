import org.junit.Test
import kotlin.test.assertEquals

internal class Day13KtTest {

    @Test
    fun `should properly perform first fold`() {
        val inputs = readInput("testInput13.txt")
        val actual: Set<Point> = extractCoordinates(inputs)
        val firstFold = foldUp(actual, 7)
        assertEquals(17, firstFold.size)
    }

    @Test
    fun `should have correct final count`() {
        val inputs = readInput("testInput13.txt")
        val coords = extractCoordinates(inputs)
        val instructions = extractInstructions(inputs)
        val folded = foldCompletely(coords, instructions)
        assertEquals(16, folded.size)
    }

    @Test
    fun `visualizes exact initial exmaple`() {
        val inputs = readInput("testInput13.txt")
        val final = readInput("testInput13_initial.txt").joinToString("\n")
        val coords = visualizePaperDots(extractCoordinates(inputs)).joinToString("\n")
        assertEquals(final, coords)
    }

    @Test
    fun `visualizes exact intermediate exmaple`() {
        val inputs = readInput("testInput13.txt")
        val final = readInput("testInput13_intermediate.txt").joinToString("\n")
        val coords = extractCoordinates(inputs)
        val instructions = extractInstructions(inputs)
        val folded = foldCompletely(coords, instructions.dropLast(1))
        val printable = visualizePaperDots(folded).joinToString("\n")
        assertEquals(final, printable)
    }

    @Test
    fun `visualizes exact final exmaple`() {
        val inputs = readInput("testInput13.txt")
        val final = readInput("testInput13_final.txt").joinToString("\n")
        val coords = extractCoordinates(inputs)
        val instructions = extractInstructions(inputs)
        val folded = foldCompletely(coords, instructions)
        val printable = visualizePaperDots(folded).joinToString("\n")
        assertEquals(final, printable)
    }
}
