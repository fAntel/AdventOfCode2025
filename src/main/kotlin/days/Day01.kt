package com.keldzh.days

import com.keldzh.Day
import com.keldzh.days.Day01.Direction.Companion.toDirection
import kotlin.io.path.readLines

class Day01 : Day() {
    override val dayNumber: UByte = 1u

    override fun partOne(): String {
        var count = 0
        var arrowAt = 50
        for (rotation in getRotations()) {
            arrowAt += rotation.distance.toInt() * rotation.direction.sign % (MAX_ARROW_VALUE + 1)
            arrowAt %= (MAX_ARROW_VALUE + 1) // +1 because we count from 0
            if (arrowAt < 0) arrowAt += MAX_ARROW_VALUE + 1

            if (arrowAt == 0) count += 1
        }
        return count.toString()
    }

    override fun partTwo(): String {
        var count = 0
        var arrowAt = 50
        for (rotation in getRotations()) {
            if (rotation.direction == Direction.LEFT) {
                if (arrowAt == 0) arrowAt += MAX_ARROW_VALUE + 1
                arrowAt -= rotation.distance.toInt()
                while (arrowAt < 0) {
                    arrowAt += MAX_ARROW_VALUE + 1
                    count += 1
                }
                if (arrowAt == 0) count += 1
            } else {
                arrowAt += rotation.distance.toInt()
                count += arrowAt / (MAX_ARROW_VALUE + 1)
                arrowAt %= (MAX_ARROW_VALUE + 1)
            }
        }
        return count.toString()
    }

    private fun getRotations(): List<Rotation> = getInputPath(dayNumber)
        .readLines()
        .map { line -> Rotation(line[0].toDirection(), line.drop(1).toUShort()) }

    private enum class Direction(val sign: Int) {
        LEFT(-1), RIGHT(1);

        companion object {
            fun Char.toDirection(): Direction = when (this) {
                'L' -> LEFT
                'R' -> RIGHT
                else -> throw IllegalArgumentException("Unknown direction '$this'.")
            }
        }
    }

    private data class Rotation(val direction: Direction, val distance: UShort)

    private companion object {
        private const val MAX_ARROW_VALUE = 99
    }
}