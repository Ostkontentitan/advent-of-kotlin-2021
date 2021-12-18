fun puzzleDaySixteenPartOne() {
    val input = readInput(16).first()
}

fun toBinary(input: String): String = input.map { HEX_TO_BINARY[it]!! }.joinToString("")
fun parseBinaryCode(binaryCode: String): Package {
    val version = TRIBIT_TO_INT[binaryCode.substring(0, 3)]!!.toInt()
    val typeId = TRIBIT_TO_INT[binaryCode.substring(3, 6)]!!.toInt()

    if (typeId == 4) {
        val body = binaryCode.substring(6, binaryCode.length)
        val value = parseLiteralBody(body)
        return Package.LiteralValue(version, value)
    } else {
        TODO("Unimplemented type")
    }
}

fun parseLiteralBody(body: String): Int {
    var value = ""
    var next = 0
    do {
        value += body.substring(next + 1, next + 5)

        if (body[next] == '0') {
            next = -1
        } else {
            next += 5
        }
    } while (next != -1)
    return value.windowed(3,3).joinToString("").toInt(2)
}

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

private val BINARY_TO_HEX = mapOf(
    "0000" to '0',
    "0001" to '1',
    "0010" to '2',
    "0011" to '3',
    "0100" to '4',
    "0101" to '5',
    "0110" to '6',
    "0111" to '7',
    "1000" to '8',
    "1001" to '9',
    "1010" to 'A',
    "1011" to 'B',
    "1100" to 'C',
    "1101" to 'D',
    "1110" to 'E',
    "1111" to 'F',
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

sealed class Package(val typeId: Int) {
    abstract val version: Int

    data class LiteralValue(override val version: Int, val value: Int) : Package(4)
}