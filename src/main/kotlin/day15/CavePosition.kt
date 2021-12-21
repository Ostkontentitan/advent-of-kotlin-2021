package day15

import kotlin.math.abs

data class CavePosition(val y: Int, val x: Int, val risk: Int) {
    fun inProximityTo(other: CavePosition): Boolean {
        val yDiff = abs(y - other.y)
        val xDiff = abs(x - other.x)
        return yDiff <= 1 && xDiff <= 1 && (xDiff + yDiff) < 2
    }
}