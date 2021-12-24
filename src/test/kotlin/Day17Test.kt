import day17.ProbeLauncher
import day17.extractTargetArea
import org.junit.Test
import kotlin.test.assertEquals

class Day17Test {

    @Test
    fun `should find ideal velocity for example`() {
        val (targetX, targetY) = readInput("testInput17.txt").first().extractTargetArea()
        val launcher = ProbeLauncher()
        val result = launcher.calculateHittingShots(targetX, targetY)!!
        assertEquals(45, result.maxY)
        assertEquals(112, result.possibleShotsCount)
    }
}