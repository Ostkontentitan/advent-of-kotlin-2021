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
        val first = BITSParser(binary).parsePackages().first()
        assertEquals(BITSPackage.LiteralValue(Header(6, 4), 2021), first)
    }

    @Test
    fun `properly parses example 1`() = runBlocking {
        val hex = "8A004A801A8002F478"
        val sum = BITSParser(toBinary(hex)).parsePackages().sumOf { it.versionSum }
        assertEquals(16, sum)
    }

    @Test
    fun `properly parses example 2`() = runBlocking {
        val hex = "620080001611562C8802118E34"
        val sum = BITSParser(toBinary(hex)).parsePackages().sumOf { it.versionSum }
        assertEquals(12, sum)
    }

    @Test
    fun `properly parses example 3`() = runBlocking {
        val hex = "C0015000016115A2E0802F182340"
        val sum = BITSParser(toBinary(hex)).parsePackages().sumOf { it.versionSum }
        assertEquals(23, sum)
    }

    @Test
    fun `properly parses example 4`() = runBlocking {
        val hex = "A0016C880162017C3686B18A3D4780"
        val sum = BITSParser(toBinary(hex)).parsePackages().sumOf { it.versionSum }
        assertEquals(31, sum)
    }
}
