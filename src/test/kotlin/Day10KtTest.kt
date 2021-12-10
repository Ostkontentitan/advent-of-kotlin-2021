import org.junit.Test
import kotlin.test.assertEquals

internal class Day10KtTest {

    @Test
    fun `calculates points correctly for single line`() {
        val inputs = listOf("[({(<(())[]>[[{[]{<()<>>")
        val sortedCompleteScores = calculateCompletionScoresAndSort(inputs)
        assertEquals(288957, sortedCompleteScores.first())
    }

    @Test
    fun `test middle score determination against example`() {
        val inputs = readInput("testInput10.txt")
        val sortedCompleteScores = calculateCompletionScoresAndSort(inputs)
        val middleIndex = sortedCompleteScores.size / 2
        val middleScore = sortedCompleteScores[middleIndex]
        assertEquals(288957, middleScore)
    }
}