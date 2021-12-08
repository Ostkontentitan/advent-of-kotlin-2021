import kotlin.test.Test
import kotlin.test.assertEquals

internal class Day7KtTest {

    @Test
    fun `parseCrabPositions returns correct list`() {
        val actual = parseCrabPositions("16,1,2,0,4,2,7,1,2,14")
        val expected = listOf(16,1,2,0,4,2,7,1,2,14)
        assertEquals(expected, actual)
    }
}