package com.keldzh.days

import com.keldzh.Day
import kotlin.io.path.readText
import kotlin.math.pow

class Day02 : Day() {
    override val dayNumber: UByte
        get() = 2u

    override fun partOne(): String {
        val ranges = readRanges()

        var counter = 0L
        for (range in ranges) {
            var digitsCount = countDigits(range.first)
            var l = range.first

            while (l <= range.last) {
                var firstPart: Long
                var secondPart: Long
                if (digitsCount % 2 != 0) {
                    digitsCount += 1
                    firstPart = 10.0.pow(digitsCount / 2 - 1).toLong()
                    secondPart = firstPart
                    l = firstPart * 10.0.pow(digitsCount / 2).toLong() + secondPart
                } else {
                    firstPart = l
                    repeat(digitsCount / 2) {
                        firstPart /= 10L
                    }
                    secondPart = l - (firstPart * 10.0.pow(digitsCount / 2)).toLong()

                    if (firstPart > secondPart) {
                        secondPart = firstPart
                    } else if (secondPart > firstPart) {
                        firstPart += 1
                        secondPart = firstPart
                    }
                    l = firstPart * (10.0.pow(digitsCount / 2)).toLong() + secondPart
                }
                if (l > range.last)
                    break

                val nextDigitCount = (10.0.pow(digitsCount / 2)).toLong()
                do {
                    counter += l

                    firstPart += 1
                    l = firstPart * (10.0.pow(digitsCount / 2)).toLong() + firstPart
                } while (l <= range.last && firstPart < nextDigitCount)

                digitsCount += 1
            }
        }

        return counter.toString()
    }

    override fun partTwo(): String {
        val ranges = readRanges()

        var counter = 0L
        for (range in ranges) {
            loop@ for (l in range) {
                val digitsCount = countDigits(l)
                var multiplier = 10L
                var multiplierDigitsCount = 1
                while (multiplierDigitsCount * 2 <= digitsCount) {
                    if (digitsCount % multiplierDigitsCount != 0) {
                        multiplier *= 10L
                        multiplierDigitsCount += 1
                        continue
                    }

                    val b = l % multiplier
                    var t = l / multiplier
                    do {
                        if (t % multiplier == b) t /= multiplier
                        else break
                    } while (t > 0)
                    if (t == 0L) {
                        counter += l
                        continue@loop
                    }

                    multiplier *= 10L
                    multiplierDigitsCount += 1
                }
            }
        }

        return counter.toString()
    }

    private fun readRanges(): Sequence<LongRange> = getInputPath(dayNumber)
        .readText()
        .splitToSequence(',', ignoreCase = true)
        .map { range ->
            val (beginning, end) = range.split('-').map { i -> i.toLong() }
            LongRange(beginning, end)
        }

    private fun countDigits(l: Long): Int {
        var result = 1
        var counter = l
        while (counter >= 10) {
            counter /= 10L
            result += 1
        }
        return result
    }
}