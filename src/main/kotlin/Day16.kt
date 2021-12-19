fun puzzleDaySixteenPartOne() {
    val input = readInput(16).first()
}

fun toBinary(input: String): String = input.map { HEX_TO_BINARY[it]!! }.joinToString("")
fun parseNextPackage(packageStart: Int = 0, binaryCode: String): ParseResult<Package> {
    val header = parseHeader(packageStart, binaryCode)
    val bodyStart = packageStart + HEADER_LENGTH

    return if (header.typeId == 4) {
        val (value, nextIndex) = parseLiteralBody(bodyStart, binaryCode)
        ParseResult(Package.LiteralValue(header, value!!), nextIndex + HEADER_LENGTH)
    } else {
        val body = binaryCode.substring(bodyStart, binaryCode.length)
        val length = parseOperatorBody(body)
        ParseResult(Package.Operator(header, length), 0)
    }
}

fun parseHeader(packageStart: Int, binaryCode: String): Header {
    val typeIndex = packageStart + 3

    val version = TRIBIT_TO_INT[binaryCode.substring(packageStart, typeIndex)]!!.toInt()
    val typeId = TRIBIT_TO_INT[binaryCode.substring(typeIndex, typeIndex + 3)]!!.toInt()
    return Header(version = version, typeId = typeId)
}

data class ParseResult<T>(val pack: T?, val nextIndex: Int)

fun parseOperatorBody(body: String): Int {
    when (val lenghtTypeId = body.first()) {
        '0' -> {
            val packagesLength = body.substring(1, 16).triBitToDecimal()
            TODO()
        }
        '1' -> {
            val subPackages = body.substring(1, 16)
            TODO()
        }
        else -> throw IllegalStateException("Unexpected non binary char $lenghtTypeId.")
    }
}

fun parseLiteralBody(startIndex: Int, body: String): ParseResult<Int> {
    var value = ""
    var next = startIndex
    do {
        value += body.substring(next + 1, next + 5)

        if (body[next] == '0') {
            val diff = (next - startIndex)
            val continueIndex = diff + (diff % 4) + LITERAL_CHUNK_SIZE + 1
            return ParseResult(value.triBitToDecimal(), continueIndex)
        } else {
            next += LITERAL_CHUNK_SIZE
        }
    } while (true)
}

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

private const val HEADER_LENGTH = 6
private const val LITERAL_CHUNK_SIZE = 5

data class Header(val version: Int, val typeId: Int)

sealed class Package {
    abstract val header: Header

    data class LiteralValue(override val header: Header, val value: Int) : Package()
    data class Operator(override val header: Header, val length: Int) : Package()
}