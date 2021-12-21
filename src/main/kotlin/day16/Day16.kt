package day16

import readInput

fun puzzle() {
    val input = readInput(16).first()
    val bitsPackage = BITSParser(toBinary(input)).parsePackage()
    println("Package sum is: ${bitsPackage.versionSum}")
    println("Package value is: ${bitsPackage.value}")
}

fun toBinary(input: String): String = input.map { HEX_TO_BINARY[it]!! }.joinToString("")

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
