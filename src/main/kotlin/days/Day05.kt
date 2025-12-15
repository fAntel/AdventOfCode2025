package com.keldzh.days

import com.keldzh.Day
import java.math.BigInteger
import kotlin.collections.fold
import kotlin.collections.mutableListOf
import kotlin.io.path.useLines
import kotlin.sequences.fold

class Day05 : Day() {
    override val dayNumber: UByte
        get() = 5u

    override fun partOne(): String {
        val ranges = mutableListOf<ClosedRange<BigInteger>>()
        val ids = mutableListOf<BigInteger>()
        getInputPath(dayNumber).useLines { seq ->
            var rangesPassed = false
            for (line in seq) {
                if (rangesPassed) {
                    ids.add(line.toBigInteger())
                } else if (line.isEmpty()) {
                    rangesPassed = true
                } else {
                    ranges.add(line.lineToRange())
                }

            }
        }

        ranges.sortWith(Comparator { a, b -> a.start.compareTo(b.start) })
        ids.sort()

        var counter = 0
        val rangesIter = ranges.iterator()
        var range: ClosedRange<BigInteger> = rangesIter.next()
        val idsIter = ids.listIterator()
        var id: BigInteger
        while (idsIter.hasNext()) {
            id = idsIter.next()

            if (id < range.start)
                continue

            if (id in range) {
                counter += 1
                continue
            }

            if (!rangesIter.hasNext())
                break
            range = rangesIter.next()
            idsIter.previous()
        }

        return counter.toString()
    }

    override fun partTwo(): String = getInputPath(dayNumber).useLines { lines ->
        lines
            .takeWhile { line -> line.isNotEmpty() }
            .map { line -> line.lineToRange() }
            .sortedWith { a, b -> a.start.compareTo(b.start) }
            .fold(mutableListOf<ClosedRange<BigInteger>>()) { acc, range ->
                if (acc.isEmpty()) {
                    acc += range
                } else {
                    val prevRange = acc.last()
                    if (range.start > prevRange.endInclusive) {
                        acc += range
                    } else {
                        if (range.endInclusive > prevRange.endInclusive) {
                            acc[acc.lastIndex] = prevRange.start..range.endInclusive
                        }
                    }
                }
                acc
            }
            .fold(BigInteger.ZERO) { counter, range ->
                counter + range.endInclusive - range.start + BigInteger.ONE
            }
            .toString()
    }

    private fun String.lineToRange(): ClosedRange<BigInteger> {
        val (a, b) = split('-', limit = 2)
        return a.toBigInteger()..b.toBigInteger()
    }
}