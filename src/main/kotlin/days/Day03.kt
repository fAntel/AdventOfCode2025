package com.keldzh.days

import com.keldzh.Day
import kotlin.io.path.readLines

class Day03 : Day() {
    override val dayNumber: UByte
        get() = 3u

    override fun partOne(): String = solution(2).toString()

    override fun partTwo(): String = solution(12).toString()

    private fun readBanks(): List<List<UByte>> =
        getInputPath(dayNumber)
            .readLines()
            .map { s -> s.map { c -> c.digitToInt().toUByte() } }

    @OptIn(ExperimentalUnsignedTypes::class)
    private fun solution(maxBatteriesCount: Int): Long {
        val banks = readBanks()

        var counter = 0L
        for (bank in banks) {
            var joltage = 0L//UByteArray(2)
            var currentJoltageIndex = 0
            var currentBankIndex = 0
            var nextJoltageRaging: UByte
            do {
                nextJoltageRaging = 0u
                for (i in currentBankIndex..bank.size - (maxBatteriesCount - currentJoltageIndex)) {
                    if (bank[i] > nextJoltageRaging) {
                        nextJoltageRaging = bank[i]
                        currentBankIndex = i
                    }
                }
                joltage = joltage * 10L + nextJoltageRaging.toLong()
                currentBankIndex += 1
                currentJoltageIndex += 1
            } while (currentBankIndex < bank.size && currentJoltageIndex < maxBatteriesCount)
            counter += joltage
        }

        return counter
    }
}