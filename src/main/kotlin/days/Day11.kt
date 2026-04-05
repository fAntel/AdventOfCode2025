package com.keldzh.days

import com.keldzh.Day
import kotlin.io.path.readLines

class Day11 : Day() {
    override val dayNumber: UByte
        get() = 11u

    override fun partOne(): String {
        val m = getInputPath(dayNumber)
            .readLines()
            .associateBy(
                { line -> line.take(3) },
                {line -> line.substringAfter(": ").split(' ').toSet() }
            )

        val memory = mutableSetOf<String>()
        var count = 0
        for (node in m["you"]!!) {
            count += traversePath(node, m, mutableSetOf(), memory)
        }
        return count.toString()
    }

    private fun traversePath(node: String, map: Map<String, Set<String>>, path: MutableSet<String>, memory: MutableSet<String>): Int {
        when (node) {
            "out" -> {
                memory.addAll(path)
                return 1
            }

            "you" -> return 0

            else -> {
                path.add(node)
                var count = 0
                for (n in map[node]!!) {
                    count += traversePath(n, map, path.toMutableSet(), memory)
                }
                return count
            }
        }
    }
}