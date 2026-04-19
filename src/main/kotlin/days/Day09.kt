package com.keldzh.days

import com.keldzh.Day
import kotlin.io.path.readLines
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

private typealias Point = Pair<Int, Int>
private val Point.x get() = this.first
private val Point.y get() = this.second

class Day09 : Day() {
    override val dayNumber: UByte
        get() = 9u

    override fun partOne(): String {
        val coords = readCoords()

        var maxArea = 0L
        var area: Long
        for (i in 0..<coords.lastIndex) {
            for (j in i + 1..<coords.size) {
                area = calculateArea(coords[i], coords[j])
                if (area > maxArea) {
                    maxArea = area
                }
            }
        }

        return maxArea.toString()
    }

    override fun partTwo(): String {
        val coords = readCoords()
        val lines = coords
            .windowed(2) { (a, b) -> Line.create(a, b) }
            .plusElement(Line.create(coords.first(), coords.last()))
            .groupBy(Line::orientation)
        val horizontalLines = lines[Orientation.HORIZONTAL] ?: emptyList()
        val verticalLines = lines[Orientation.VERTICAL] ?: emptyList()

        fun Point.isInsideFigure(): Boolean {
            if (horizontalLines.none { line -> line.isHigher(this) })
                return false
            if (horizontalLines.none { line -> line.isLower(this) })
                return false
            if (verticalLines.none { line -> line.isLefter(this) })
                return false
            if (verticalLines.none { line -> line.isRighter(this) })
                return false

            return true
        }

        var maxArea = 0L
        var area: Long
        for (i in 0..<coords.lastIndex) {
            for (j in i + 1..<coords.size) {
                val a = coords[i]
                val b = coords[j]
                val topLeft = Pair(min(a.x, b.x), min(a.y, b.y))
                val topRight = Pair(max(a.x, b.x), min(a.y, b.y))
                val bottomRight = Pair(max(a.x, b.x), max(a.y, b.y))
                val bottomLeft = Pair(min(a.x, b.x), max(a.y, b.y))

                fun Point.isOtherCornersInsideFigure(): Boolean = this == a || this == b || this.isInsideFigure()

                if (
                    !topLeft.isOtherCornersInsideFigure() || !topRight.isOtherCornersInsideFigure() ||
                    !bottomRight.isOtherCornersInsideFigure() || !bottomLeft.isOtherCornersInsideFigure()
                )
                    continue

                if (
                    verticalLines.any { line ->
                        line.intersects(topLeft, topRight, Orientation.HORIZONTAL) ||
                        line.intersects(bottomLeft, bottomRight, Orientation.HORIZONTAL)
                    } ||
                    horizontalLines.any { line ->
                        line.intersects(topRight, bottomRight, Orientation.VERTICAL) ||
                        line.intersects(topLeft, bottomLeft, Orientation.VERTICAL)
                    }
                )
                    continue

                area = calculateArea(a, b)
                if (area > maxArea) {
                    maxArea = area
                }
            }
        }

        return maxArea.toString()
    }

    private fun readCoords(): List<Point> = getInputPath(dayNumber)
        .readLines()
        .map { line ->
            val (x, y) = line.split(',')
            x.toInt() to y.toInt()
        }

    private fun calculateArea(a: Point, b: Point): Long =
        (abs(a.x - b.x) + 1L) * (abs(a.y - b.y) + 1L)

    private enum class Orientation { VERTICAL, HORIZONTAL }

    private data class Line(val a: Point, val b: Point, val orientation: Orientation) {
        fun intersects(otherA: Point, otherB: Point, otherOrientation: Orientation): Boolean {
            if (otherOrientation == orientation)
                return false

            return if (orientation == Orientation.VERTICAL) {
                otherA.x < a.x && a.x < otherB.x && a.y < otherA.y && otherA.y < b.y
            } else {
                otherA.y < a.y && a.y < otherB.y && a.x < otherA.x && otherA.x < b.x
            }
        }

        fun isHigher(point: Point): Boolean {
            assert(orientation == Orientation.HORIZONTAL)

            return point.y <= a.y && point.x in a.x..b.x
        }

        fun isLower(point: Point): Boolean {
            assert(orientation == Orientation.HORIZONTAL)

            return point.y >= a.y && point.x in a.x..b.x
        }

        fun isLefter(point: Point): Boolean {
            assert(orientation == Orientation.VERTICAL)

            return point.x >= a.x && point.y in a.y..b.y
        }

        fun isRighter(point: Point): Boolean {
            assert(orientation == Orientation.VERTICAL)

            return point.x <= a.x && point.y in a.y..b.y
        }

        companion object {
            fun create(a: Point, b: Point): Line {
                val orientation = if (a.x == b.x) Orientation.VERTICAL else Orientation.HORIZONTAL

                return if (orientation == Orientation.VERTICAL) {
                    val first = if (a.y <= b.y) a else b
                    val second = if (first == a) b else a
                    Line(first, second, orientation)
                } else {
                    val first = if (a.x <= b.x) a else b
                    val second = if (first == a) b else a
                     Line(first, second, orientation)
                }
            }
        }
    }
}