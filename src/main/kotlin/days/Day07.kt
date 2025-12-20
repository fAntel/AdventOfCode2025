package com.keldzh.days

import com.keldzh.Day
import java.util.LinkedList
import kotlin.io.path.readLines

class Day07 : Day() {
    override val dayNumber: UByte
        get() = 7u

    override fun partOne(): String {
        val map = getInputPath(dayNumber).readLines()
        val traversedPoints = hashSetOf<Point>()
        val pointsToGo = LinkedList<Point>().apply {
            add(Point(map[0].indexOf('S'), 0))
        }

        var counter = 0L
        var p: Point
        val height = map.size
        val width = map[0].length
        while (pointsToGo.isNotEmpty()) {
            p = pointsToGo.pop()

            while (p.y < height && p !in traversedPoints) {
                traversedPoints.add(p.copy())

                if (map[p.y][p.x] == '^') {
                    counter += 1

                    if (p.x + 1 < width) {
                        pointsToGo.add(Point(p.x + 1, p.y))
                    }

                    if (p.x > 0) {
                        p.x -= 1
                    }
                } else {
                    p.y += 1
                }
            }
        }

        return counter.toString()
    }

    private data class Point(var x: Int, var y: Int)
}