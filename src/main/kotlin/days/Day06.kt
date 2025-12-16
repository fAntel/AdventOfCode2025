package com.keldzh.days

import com.keldzh.Day
import java.util.LinkedList
import kotlin.io.path.readLines

class Day06 : Day() {
    override val dayNumber: UByte
        get() = 6u

    override fun partOne(): String {
        val lines = getInputPath(dayNumber).readLines()
        val numbers = lines
            .dropLast(1)
            .map { line ->
                line.trim().split(Regex("\\s+")).map { s -> s.toShort() }
            }
        val operations = lines.parseOperations()

        var counter = 0L
        for (j in 0..<operations.size) {
            var total = numbers[0][j].toLong()
            for (i in 1..<numbers.size) {
                total = operations[j].invoke(total, numbers[i][j].toLong())
            }
            counter += total
        }

        return counter.toString()
    }

    override fun partTwo(): String {
        val lines = getInputPath(dayNumber).readLines()
        val numbers = LinkedList<List<Short>>().apply {
            val width = lines[0].lastIndex
            val height = lines.size - 1
            var column = mutableListOf<Short>()
            for (j in width downTo 0) {
                var counter = 0
                for (i in 0..<height) {
                    if (lines[i][j].isDigit()) {
                        counter = counter * 10 + lines[i][j].digitToInt()
                    }
                }
                if (counter != 0) {
                    column += counter.toShort()
                } else {
                    add(0, column)
                    if (j != 0) {
                        column = mutableListOf()
                    }
                }
            }
            if (column.isNotEmpty()) {
                add(0, column)
            }
        }
        val operations = lines.parseOperations()

        return numbers.foldIndexed(0L) { i, acc, column ->
            val operation = operations[i]
            acc + column.fold(0L) { acc, n ->
                if (acc == 0L) n.toLong()
                else operation.invoke(acc, n.toLong())
            }
        }.toString()
    }

    private fun List<String>.parseOperations(): List<(Long, Long) -> Long> =
        last()
            .trim()
            .split(Regex("\\s+"))
            .map { c ->
                when (c) {
                    "+" -> Long::plus
                    "*" -> Long::times
                    else -> throw IllegalArgumentException("Unknown operation '%c'")
                }
            }
}