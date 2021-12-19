fun puzzleDaySixteenPartOne() {
    val input = readInput(16).first()
}

class BITSParser(private val binaryString: String) {

    private var currentPosition = 0
    private val packages = mutableListOf<BITSPackage>()

    fun parsePackages(): List<BITSPackage> {
        if (currentPosition != binaryString.length) {
            throw IllegalStateException("Unexpected end index $currentPosition instead of ${binaryString.length}")
        }
        return packages
    }

    private fun parseNextPackage(): BITSPackage {
        val header = parseHeader()

        return if (header.typeId == 4) {
            val value = parseLiteralBody()
            BITSPackage.LiteralValue(header, value)
        } else {
            val subPackages = parseOperatorBody()
            BITSPackage.Operator(header, subPackages)
        }
    }

    private fun parseHeader(): Header {
        val typeIndex = currentPosition + 3

        val version = TRIBIT_TO_INT[binaryString.substring(currentPosition, typeIndex)]!!.toInt()
        val typeId = TRIBIT_TO_INT[binaryString.substring(typeIndex, typeIndex + 3)]!!.toInt()
        currentPosition += 6
        return Header(version = version, typeId = typeId)
    }

    private fun parseLiteralBody(): Int {
        var value = ""
        do {
            value += binaryString.substring(currentPosition + 1, currentPosition + LITERAL_CHUNK_SIZE)

            if (binaryString[currentPosition] == '0') {
                currentPosition += LITERAL_CHUNK_SIZE
                    // TODO offset padding bits
                return value.triBitToDecimal()
            } else {
                currentPosition += LITERAL_CHUNK_SIZE
            }
        } while (true)
    }

    private fun parseOperatorBody(): List<BITSPackage> {
        val lenghtTypeId = binaryString[currentPosition]
        currentPosition++
        return when (lenghtTypeId) {
            '0' -> parseOperatorPackagesByLength()
            '1' -> parseOperatorPackagesByCount()
            else -> throw IllegalStateException("Unexpected non binary char $lenghtTypeId.")
        }
    }

    private fun parseOperatorPackagesByCount(): List<BITSPackage> {
        val subCount = binaryString.substring(currentPosition, currentPosition + 11).triBitToDecimal()
        currentPosition += 11
        val packages = mutableListOf<BITSPackage>()
        while (packages.size < subCount) {
            packages.add(parseNextPackage())
        }
        return packages
    }

    private fun parseOperatorPackagesByLength(): List<BITSPackage> {
        val subLength = binaryString.substring(currentPosition, currentPosition + 15).triBitToDecimal()
        currentPosition += 15
        val endPosition = currentPosition + subLength
        val packages = mutableListOf<BITSPackage>()
        while (currentPosition < endPosition) {
            packages.add(parseNextPackage())
        }
        return packages
    }
}

fun toBinary(input: String): String = input.map { HEX_TO_BINARY[it]!! }.joinToString("")

fun String.triBitToDecimal() = this.windowed(3, 3).joinToString("").toInt(2)

private val HEX_TO_BINARY = mapOf(
    '0' to "0000",
    '1' to "0001",
    '2' to "0010",
    '3' to "0011",
    '4' to "0100",
    '5' to "0101",
    '6' to "0110",
    '7' to "0111",
    '8' to "1000",
    '9' to "1001",
    'A' to "1010",
    'B' to "1011",
    'C' to "1100",
    'D' to "1101",
    'E' to "1110",
    'F' to "1111",
)

private val TRIBIT_TO_INT = mapOf(
    "000" to 0,
    "001" to 1,
    "010" to 2,
    "011" to 3,
    "100" to 4,
    "101" to 5,
    "110" to 6,
    "111" to 7,
)

private const val LITERAL_CHUNK_SIZE = 5

data class Header(val version: Int, val typeId: Int)

sealed class BITSPackage {
    abstract val header: Header
    abstract val versionSum: Int

    data class LiteralValue(override val header: Header, val value: Int) : BITSPackage() {
        override val versionSum: Int
            get() = header.version
    }

    data class Operator(override val header: Header, val packages: List<BITSPackage>) : BITSPackage() {
        override val versionSum: Int
            get() = header.version + packages.sumOf { it.versionSum }

    }
}