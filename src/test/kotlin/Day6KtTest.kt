import org.junit.Test
import kotlin.test.assertEquals

internal class Day6KtTest {

    @Test
    fun inputToPopulation() {
        val input = "1,2,3"
        val actual = inputToPopulation(input)
        val expected = listOf(
            Lanternfish(1),
            Lanternfish(2),
            Lanternfish(3)
        )
        assertEquals(expected, actual)
    }

    @Test
    fun inputToPopulationModel() {
        val input = "1,2,3,2,2,3,1,1,1,1"
        val actual = inputToPopulationModel(input)
        val expected = mapOf(
            1 to 5L,
            2 to 3L,
            3 to 2L
        )
        assertEquals(expected, actual)
    }

    @Test
    fun predictPopulation() {
        val actual = predictPopulation(inputToPopulation("3,4,3,1,2"), 18)
        val expected = inputToPopulation("6,0,6,4,5,6,0,1,1,2,6,0,1,1,1,2,2,3,3,4,6,7,8,8,8,8")
        assertEquals(expected.size, actual.size)
    }

    @Test
    fun predictPopulationWithModel() {
        val actual = predictPopulationWithModel(inputToPopulationModel("3,4,3,1,2"), 256)
        assertEquals(26984457539L, actual.values.sum())
    }
}
