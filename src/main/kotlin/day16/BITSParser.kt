package day16

class BITSParser(private val binaryString: String) {

    private var currentPosition = 0

    fun parsePackage(): BITSPackage {
        val header = parseHeader()

        val bitsPackage = when (header.typeId) {
            0 -> BITSPackage.Sum(header, parseOperatorBody())
            1 -> BITSPackage.Product(header, parseOperatorBody())
            2 -> BITSPackage.Minimum(header, parseOperatorBody())
            3 -> BITSPackage.Maximum(header, parseOperatorBody())
            4 -> BITSPackage.LiteralValue(header, parseLiteralBody())
            5 -> BITSPackage.GreaterThan(header, parseOperatorBody())
            6 -> BITSPackage.LessThan(header, parseOperatorBody())
            7 -> BITSPackage.EqualTo(header, parseOperatorBody())
            else -> throw IllegalArgumentException("Unexpected type id ${header.typeId}")
        }

        return bitsPackage
    }

    private fun parseHeader(): BITSHeader {
        val typeIndex = currentPosition + 3

        val version = binaryString.substring(currentPosition, typeIndex).toInt(2)
        val typeId = binaryString.substring(typeIndex, typeIndex + 3).toInt(2)

        currentPosition += HEADER_SIZE

        return BITSHeader(version = version, typeId = typeId)
    }

    private fun parseLiteralBody(): Long {
        val combinedSegments = mutableListOf<String>()
        var exit = false

        do {
            if (binaryString[currentPosition] == ZERO_CHAR) {
                exit = true
            }
            combinedSegments.add(binaryString.substring(currentPosition, currentPosition + LITERAL_CHUNK_SIZE))
            currentPosition += LITERAL_CHUNK_SIZE

        } while (!exit)

        return combinedSegments.joinToString("") { it.takeLast(4) }.binaryToDecimal()
    }

    private fun parseOperatorBody(): List<BITSPackage> {
        val lengthTypeId = binaryString[currentPosition]

        currentPosition++

        return when (lengthTypeId) {
            ZERO_CHAR -> parseOperatorPackagesByLength()
            ONE_CHAR -> parseOperatorPackagesByCount()
            else -> throw IllegalStateException("Unexpected non binary char $lengthTypeId.")
        }
    }

    private fun parseOperatorPackagesByCount(): List<BITSPackage> {
        val subCount = binaryString.substring(currentPosition, currentPosition + OPERATOR_COUNT_SIZE).binaryToDecimal()
        currentPosition += OPERATOR_COUNT_SIZE
        val packages = mutableListOf<BITSPackage>()
        while (packages.size < subCount) {
            packages.add(parsePackage())
        }
        return packages
    }

    private fun parseOperatorPackagesByLength(): List<BITSPackage> {
        val subLength = binaryString.substring(currentPosition, currentPosition + OPERATOR_LENGTH_SIZE).binaryToDecimal()
        currentPosition += OPERATOR_LENGTH_SIZE
        val endPosition = currentPosition + subLength
        val packages = mutableListOf<BITSPackage>()
        while (currentPosition < endPosition) {
            packages.add(parsePackage())
        }
        assert(currentPosition.toLong() == endPosition) { "Expected current position ($currentPosition) to match end position ($endPosition)" }
        return packages
    }

    private fun String.binaryToDecimal() = this.toLong(2)

    companion object {
        private const val OPERATOR_LENGTH_SIZE = 15
        private const val OPERATOR_COUNT_SIZE = 11
        private const val LITERAL_CHUNK_SIZE = 5
        private const val HEADER_SIZE = 6
        private const val ZERO_CHAR = '0'
        private const val ONE_CHAR = '1'
    }
}