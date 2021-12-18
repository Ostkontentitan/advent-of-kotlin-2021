import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals

internal class Day16KtTest {

    @Test
    fun `should find the shortest way in example`() = runBlocking {
        val input = "D2FE28"
        val actual = toBinary(input)
        assertEquals("110100101111111000101000", actual)
    }

    @Test
    fun `properly parses type 4 package`() = runBlocking {
        val actual = parseBinaryCode("110100101111111000101000")
        assertEquals(Package.LiteralValue(6, 2021), actual)
    }
}
