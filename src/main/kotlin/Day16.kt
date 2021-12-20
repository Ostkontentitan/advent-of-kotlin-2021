fun puzzleDaySixteenPartOne() {
    val input = readInput(16).first()
    val sum = BITSParser(toBinary(input)).parsePackages().sumOf { it.versionSum }
    println("Package sum is: $sum")
}

class BITSParser(private val binaryString: String) {

    private var currentPosition = 0

    fun parsePackages(): List<BITSPackage> = listOf(parseNextPackage())

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
        currentPosition += HEADER_SIZE
        return Header(version = version, typeId = typeId)
    }

    private fun parseLiteralBody(): Long {
        var combinedSegments = ""
        var exit = false
        do {
            if (binaryString[currentPosition] == ZERO_CHAR) {
                exit = true
            }
            combinedSegments += binaryString.substring(currentPosition + 1, currentPosition + LITERAL_CHUNK_SIZE)
            currentPosition += LITERAL_CHUNK_SIZE

        } while (!exit)

        return combinedSegments.binaryToDecimal()
    }

    private fun parseOperatorBody(): List<BITSPackage> {
        val lenghtTypeId = binaryString[currentPosition]
        currentPosition++
        return when (lenghtTypeId) {
            ZERO_CHAR -> parseOperatorPackagesByLength()
            ONE_CHAR -> parseOperatorPackagesByCount()
            else -> throw IllegalStateException("Unexpected non binary char $lenghtTypeId.")
        }
    }

    private fun parseOperatorPackagesByCount(): List<BITSPackage> {
        val subCount = +("0" + binaryString.substring(currentPosition, currentPosition + 11)).binaryToDecimal()
        currentPosition += 11
        val packages = mutableListOf<BITSPackage>()
        while (packages.size < subCount) {
            packages.add(parseNextPackage())
        }
        return packages
    }

    private fun parseOperatorPackagesByLength(): List<BITSPackage> {
        val subLength = binaryString.substring(currentPosition, currentPosition + 15).binaryToDecimal()
        currentPosition += 15
        val endPosition = currentPosition + subLength
        val packages = mutableListOf<BITSPackage>()
        while (currentPosition < endPosition) {
            packages.add(parseNextPackage())
        }
        return packages
    }

    companion object {
        private const val LITERAL_CHUNK_SIZE = 5
        private const val HEADER_SIZE = 6
        private const val ZERO_CHAR = '0'
        private const val ONE_CHAR = '1'
    }
}

fun toBinary(input: String): String = input.map { HEX_TO_BINARY[it]!! }.joinToString("")

fun String.binaryToDecimal() = this.toLong(2)

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

data class Header(val version: Int, val typeId: Int)

sealed class BITSPackage {
    abstract val header: Header
    abstract val versionSum: Int

    data class LiteralValue(override val header: Header, val value: Long) : BITSPackage() {
        override val versionSum: Int
            get() = header.version
    }

    data class Operator(override val header: Header, val packages: List<BITSPackage>) : BITSPackage() {
        override val versionSum: Int
            get() = header.version + packages.sumOf { it.versionSum }

    }
}