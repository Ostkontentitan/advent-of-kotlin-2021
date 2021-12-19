import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals

internal class Day16KtTest {

    @Test
    fun `properly converts to binary`() = runBlocking {
        val input = "D2FE28"
        val actual = toBinary(input)
        assertEquals("110100101111111000101000", actual)
    }

    @Test
    fun `properly parses type 4 package`() = runBlocking {
        val binary = "110100101111111000101000"
        val first = parseNextPackage(0, binary)
        assertEquals(Package.LiteralValue(Header(6, 4), 2021), first.pack!!)
    }

    @Test
    fun `properly parses 2 type 4 package in series`() = runBlocking {
        val binary = "110100101111111000101000110100101111111000101000"
        val first = parseNextPackage(0, binary)
        val second = parseNextPackage(first.nextIndex, binary)
        assertEquals(Package.LiteralValue(Header(6, 4), 2021), first.pack!!)
        assertEquals(Package.LiteralValue(Header(6, 4), 2021), second.pack!!)
    }
}
