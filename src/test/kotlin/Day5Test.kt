import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class Day5Test {

    @Test
    fun `parse a simple line`() {
        val parsed = parseLines(
            listOf(
                "1,1 -> 2,2"
            )
        )
        assertEquals(listOf(Line(Point(1, 1), Point(2, 2))), parsed)
    }

    @Test
    fun `parse the actual input giving correct size`() {
        val parsed = parseLines(
            readInput(5).map { it.trim() }
        )
        assertEquals(500, parsed.size)
    }

    @Test
    fun `make sure actual first 5 lines translate correctly`() {
        val parsed = parseLines(
            listOf(
                "348,742 -> 620,742",
                "494,864 -> 494,484",
                "193,136 -> 301,136",
                "342,692 -> 342,538",
                "234,525 -> 102,393"
            )
        )
        val expected = listOf(
            Line(Point(348, 742), Point(620, 742)),
            Line(Point(494, 864), Point(494, 484)),
            Line(Point(193, 136), Point(301, 136)),
            Line(Point(342, 692), Point(342, 538)),
            Line(Point(234, 525), Point(102, 393))
        )
        assertEquals(expected, parsed)
    }

    @Test
    fun `properly identifies vertical and horizontal lines`() {
        assertTrue(Line(Point(1, 1), Point(1, 9)).isVertical())
        assertTrue(Line(Point(1, 9), Point(1, 1)).isVertical())
        assertFalse(Line(Point(1, 1), Point(1, 9)).isHorizontal())

        assertTrue(Line(Point(1, 1), Point(9, 1)).isHorizontal())
        assertTrue(Line(Point(9, 1), Point(1, 1)).isHorizontal())
        assertFalse(Line(Point(1, 1), Point(9, 1)).isVertical())

        assertFalse(Line(Point(1, 1), Point(9, 2)).isHorizontal())
        assertFalse(Line(Point(1, 1), Point(9, 2)).isVertical())
    }

    @Test
    fun `sums up vertical points`() {
        val actual = Line(Point(1, 1), Point(1, 4)).points()
        val expected = listOf(
            Point(1, 1),
            Point(1, 2),
            Point(1, 3),
            Point(1, 4),
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `sums up horizontal points`() {
        val actual = Line(Point(1, 1), Point(4, 1)).points()
        val expected = listOf(
            Point(1, 1),
            Point(2, 1),
            Point(3, 1),
            Point(4, 1),
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `sums up vertical points within backwards line`() {
        val actual = Line(Point(1, 5), Point(1, 2)).points()
        val expected = listOf(
            Point(1, 5),
            Point(1, 4),
            Point(1, 3),
            Point(1, 2),
        )
        assertEquals(expected.toSet(), actual.toSet())
    }

    @Test
    fun `returns diagonal points`() {
        val actual = Line(Point(7, 5), Point(4, 2)).points()
        val expected = listOf(
            Point(7, 5),
            Point(6, 4),
            Point(5, 3),
            Point(4, 2),
        )
        assertEquals(expected.toSet(), actual.toSet())
    }

    @Test
    fun `returns diagonal points with x and y going different directions`() {
        val actual = Line(Point(7, 5), Point(4, 8)).points()
        val expected = listOf(
            Point(7, 5),
            Point(6, 6),
            Point(5, 7),
            Point(4, 8),
        )
        assertEquals(expected.toSet(), actual.toSet())
    }
}
