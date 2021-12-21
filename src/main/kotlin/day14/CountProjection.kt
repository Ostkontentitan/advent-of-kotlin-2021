package day14

class CountProjection {
    fun perform(steps: Int, inputs: List<String>): Long {
        val instructions = extractInstructionMap(inputs)
        val polyCount = initialPolyPairCount(inputs.first())
        val polyTail = inputs.first().last().toString()
        val countForChars = growPolyCountBySteps(steps, polyCount, instructions, polyTail)
        val counts = countForChars.values.sorted()
        return counts.maxOf { it } - counts.minOf { it }
    }

    private tailrec fun growPolyCountBySteps(
        steps: Int,
        polyCount: Map<String, Long>,
        instructions: Map<String, String>,
        tail: String
    ): Map<String, Long> = if (steps == 0) {
        polyCount.keys.fold(mapOf(tail to 1)) { acc, item ->
            val first = item.first().toString()
            val count = polyCount[item]!!
            acc + (first to (acc[first] ?: 0L) + count)
        }
    } else {
        val new = growPolyCountByStep(polyCount, instructions)
        growPolyCountBySteps(steps - 1, new, instructions, tail)
    }

    private fun growPolyCountByStep(
        polyCount: Map<String, Long>,
        instructions: Map<String, String>
    ): Map<String, Long> {
        val updated = mutableMapOf<String, Long>()
        polyCount.forEach { item ->
            val insertion = instructions[item.key]
            if (insertion != null) {
                val keyOne = "${item.key[0]}${insertion}"
                val keyTwo = "${insertion}${item.key[1]}"
                val itemCount = item.value
                updated.addToValue(keyOne, itemCount)
                updated.addToValue(keyTwo, itemCount)
            }
        }
        return updated.filterValues { it > 0L }
    }

    private fun MutableMap<String, Long>.addToValue(key: String, itemCount: Long) {
        this += (key to ((this[key] ?: 0) + itemCount))
    }

    private fun initialPolyPairCount(input: String): Map<String, Long> =
        input.windowed(size = 2, partialWindows = true).fold(emptyMap()) { acc, item ->
            acc + (item to (acc[item] ?: 0L) + 1L)
        }

    private fun extractInstructionMap(inputs: List<String>): Map<String, String> =
        inputs.subList(2, inputs.size).associate {
            val (target, replace) = it.split(" -> ")
            target to replace
        }
}
