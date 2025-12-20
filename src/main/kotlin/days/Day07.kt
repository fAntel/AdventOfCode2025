package com.keldzh.days

import com.keldzh.Day
import java.util.LinkedList
import kotlin.io.path.readLines

class Day07 : Day() {
    override val dayNumber: UByte
        get() = 7u

    override fun partOne(): String {
        val map = readMap()
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
                        pointsToGo.add(Point(p.x + 1, p.y + 1))
                    }

                    if (p.x > 0) {
                        p.x -= 1
                        p.y += 1
                    }
                } else {
                    p.y += 1
                }
            }
        }

        return counter.toString()
    }

    override fun partTwo(): String {
        val map = getInputPath(dayNumber).readLines()
        val memory = mutableMapOf<Point, Long>()
        return traverseMap(Point(map[0].indexOf('S'), 0), map, memory).toString()
    }

    private fun traverseMap(
        forkPoint: Point,
        map: List<String>,
        memory: MutableMap<Point, Long>,
        width: Int = map[0].length
    ): Long {
        val points = mutableSetOf<Point>()
        if (forkPoint.x > 0) {
            points.add(forkPoint.copy(x = forkPoint.x - 1, y = forkPoint.y + 1))
        }
        if (forkPoint.x + 1 < width) {
            points.add(forkPoint.copy(x = forkPoint.x + 1, y = forkPoint.y + 1))
        }
        var counter = 0L
        loop@for (p in points) {
            while (p.y < map.size) {
                if (map[p.y][p.x] == '^') {
                    var count = memory[p]
                    if (count == null) {
                        count = traverseMap(p, map, memory, width)
                        counter += count
                        memory[p.copy()] = count
                    } else {
                        counter += count
                    }
                    continue@loop
                } else {
                    p.y += 1
                }
            }
            counter += 1L
        }
        return counter
    }

    private fun readMap(): List<String> = getInputPath(dayNumber).readLines()
        .filterNot { line -> line.all { c -> c == '.' } }

    private data class Point(var x: Int, var y: Int)
}