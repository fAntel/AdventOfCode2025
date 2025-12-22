package com.keldzh.days

import com.keldzh.Day
import kotlin.io.path.readLines
import kotlin.math.abs

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

    private fun readCoords(): List<Pair<Int, Int>> = getInputPath(dayNumber)
        .readLines()
        .map { line ->
            val (x, y) = line.split(',')
            x.toInt() to y.toInt()
        }

    private fun calculateArea(a: Pair<Int, Int>, b: Pair<Int, Int>): Long =
        (abs(a.first - b.first) + 1L) * (abs(a.second - b.second) + 1L)
}