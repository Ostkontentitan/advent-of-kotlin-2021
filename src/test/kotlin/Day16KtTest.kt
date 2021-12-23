import day16.BITSPackage
import day16.BITSParser
import day16.BITSHeader
import day16.toBinary
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
        val first = BITSParser(binary).parsePackage()
        assertEquals(BITSPackage.LiteralValue(BITSHeader(6, 4), 2021), first)
    }

    @Test
    fun `properly parses example 1`() = runBlocking {
        val hex = "8A004A801A8002F478"
        val sum = BITSParser(toBinary(hex)).parsePackage().versionSum
        assertEquals(16, sum)
    }

    @Test
    fun `properly parses example 1b`() = runBlocking {
        val hex = "38006F45291200"
        val bitsPackage = BITSParser(toBinary(hex)).parsePackage()
        assertEquals(9, bitsPackage.versionSum)
        assertEquals(1, bitsPackage.value)
    }

    @Test
    fun `properly parses example 2`() = runBlocking {
        val hex = "620080001611562C8802118E34"
        val sum = BITSParser(toBinary(hex)).parsePackage().versionSum
        assertEquals(12, sum)
    }

    @Test
    fun `properly parses example 3`() = runBlocking {
        val hex = "C0015000016115A2E0802F182340"
        val sum = BITSParser(toBinary(hex)).parsePackage().versionSum
        assertEquals(23, sum)
    }

    @Test
    fun `properly parses example 4`() = runBlocking {
        val hex = "A0016C880162017C3686B18A3D4780"
        val sum = BITSParser(toBinary(hex)).parsePackage().versionSum
        assertEquals(31, sum)
    }

    @Test
    fun `properly parses example 5`() = runBlocking {
        val hex = "C200B40A82"
        val sum = BITSParser(toBinary(hex)).parsePackage().value
        assertEquals(3, sum)
    }

    @Test
    fun `properly parses example 6`() = runBlocking {
        val hex = "04005AC33890"
        val sum = BITSParser(toBinary(hex)).parsePackage().value
        assertEquals(54, sum)
    }

    @Test
    fun `properly parses example 7`() = runBlocking {
        val hex = "880086C3E88112"
        val sum = BITSParser(toBinary(hex)).parsePackage().value
        assertEquals(7, sum)
    }

    @Test
    fun `properly parses example 8`() = runBlocking {
        val hex = "CE00C43D881120"
        val sum = BITSParser(toBinary(hex)).parsePackage().value
        assertEquals(9, sum)
    }

    @Test
    fun `properly parses example 9`() = runBlocking {
        val hex = "D8005AC2A8F0"
        val sum = BITSParser(toBinary(hex)).parsePackage().value
        assertEquals(1, sum)
    }

    @Test
    fun `properly parses example 10`() = runBlocking {
        val hex = "F600BC2D8F"
        val sum = BITSParser(toBinary(hex)).parsePackage().value
        assertEquals(0, sum)
    }

    @Test
    fun `properly parses example 11`() = runBlocking {
        val hex = "9C005AC2F8F0"
        val sum = BITSParser(toBinary(hex)).parsePackage().value
        assertEquals(0, sum)
    }

    @Test
    fun `properly parses example 12`() = runBlocking {
        val hex = "9C0141080250320F1802104A08"
        val sum = BITSParser(toBinary(hex)).parsePackage().value
        assertEquals(1, sum)
    }

    @Test
    fun `properly parses example 13`() = runBlocking {
        val hex = "8A004A801A8002F478"
        val bitsPackage = BITSParser(toBinary(hex)).parsePackage()
        assertEquals(15, bitsPackage.value)
    }

    @Test
    fun `properly parses example 14`() = runBlocking {
        val hex = "620080001611562C8802118E34"
        val bitsPackage = BITSParser(toBinary(hex)).parsePackage()
        assertEquals(46, bitsPackage.value)
    }

    @Test
    fun `properly parses example 15`() = runBlocking {
        val hex = "C0015000016115A2E0802F182340"
        val bitsPackage = BITSParser(toBinary(hex)).parsePackage()
        assertEquals(46, bitsPackage.value)
    }

    @Test
    fun `properly parses example 16`() = runBlocking {
        val hex = "A0016C880162017C3686B18A3D4780"
        val bitsPackage = BITSParser(toBinary(hex)).parsePackage()
        assertEquals(54, bitsPackage.value)
    }

    @Test
    fun `long and int conversion return same result`() = runBlocking {
        val hex = "1010101100101110001101001111"
        assertEquals(hex.toInt(2).toLong(), hex.toLong(2))
    }

    @Test
    fun `properly parses subpackages of real data`() = runBlocking {
        val hex = readInput(16).first()
        val bitsPackage = BITSParser(toBinary(hex)).parsePackage()

        assertEquals(843, bitsPackage.packages[0].value)
        assertEquals(53943, bitsPackage.packages[1].value)
    }
}
