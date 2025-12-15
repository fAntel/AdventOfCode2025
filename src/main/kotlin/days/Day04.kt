package com.keldzh.days

import com.keldzh.Day
import kotlin.io.path.readLines

class Day04 : Day() {
    override val dayNumber: UByte
        get() = 4u

    override fun partOne(): String {
        val map = readMap()

        val calculation = mutableMapOf<Pair<Int, Int>, Int>()
        val rollsOfPaperCoords = mutableSetOf<Pair<Int, Int>>()
        analyzeMap(map, rollsOfPaperCoords, calculation)

        var counter = 0
        for (coord in rollsOfPaperCoords) {
            calculation[coord].let { count ->
                if (count == null || count < 4) {
                    counter += 1
                }
            }
        }

        return counter.toString()
    }

    override fun partTwo(): String {
        val map = readMap()

        val calculation = mutableMapOf<Pair<Int, Int>, Int>()
        val rollsOfPaperCoords = mutableSetOf<Pair<Int, Int>>()
        val mapWidth = map[0].size
        val mapHeight = map.size
        analyzeMap(map, rollsOfPaperCoords, calculation)

        var prevCounter: Int
        var counter = 0
        do {
            prevCounter = counter

            val iter = rollsOfPaperCoords.iterator()
            var coord: Pair<Int, Int>
            while (iter.hasNext()) {
                coord = iter.next()

                calculation[coord].let { count ->
                    if (count == null || count < 4) {
                        counter += 1
                        iter.remove()

                        calculation.updateForCell(mapWidth, mapHeight, coord.first, coord.second, ::decreaseCountForCell)
                    }
                }
            }
        } while (counter != prevCounter)

        return counter.toString()
    }

    private fun readMap(): List<List<CellType>> =
        getInputPath(dayNumber)
            .readLines()
            .map { s -> s.map(CellType::fromChar) }

    private fun analyzeMap(
        map: List<List<CellType>>,
        rollsOfPaperCoords: MutableSet<Pair<Int, Int>>,
        calculation: MutableMap<Pair<Int, Int>, Int>
    ) {
        val mapWidth = map[0].size
        val mapHeight = map.size
        for (i in 0..<mapHeight) {
            for (j in 0..<mapWidth) {
                if (map[i][j] != CellType.ROLL_OF_PAPER)
                    continue

                rollsOfPaperCoords += i to j

                calculation.updateForCell(mapWidth, mapHeight, i, j, ::increaseCountForCell)
            }
        }
    }

    private fun MutableMap<Pair<Int, Int>, Int>.updateForCell(
        mapWidth: Int, mapHeight: Int, i: Int, j: Int,
        updateFun: (MutableMap<Pair<Int, Int>, Int>, Int, Int) -> Unit
    ) {
        if (i > 0) {
            updateForRow(mapWidth, i - 1, j, updateFun)
        }
        if (j > 0) {
            updateFun(this, i, j - 1)
        }
        if (j + 1 < mapWidth) {
            updateFun(this, i, j + 1)
        }
        if (i + 1 < mapHeight) {
            updateForRow(mapWidth, i + 1, j, updateFun)
        }
    }

    private fun MutableMap<Pair<Int, Int>, Int>.updateForRow(
        mapWidth: Int, i: Int, j: Int,
        updateFun: (MutableMap<Pair<Int, Int>, Int>, Int, Int) -> Unit
    ) {
        if (j > 0) {
            updateFun(this, i, j - 1)
        }
        updateFun(this, i, j)
        if (j + 1 < mapWidth) {
            updateFun(this, i, j + 1)
        }
    }

    private fun increaseCountForCell(m: MutableMap<Pair<Int, Int>, Int>, i: Int, j: Int) {
        m.merge(i to j, 1, Int::plus)
    }

    private fun decreaseCountForCell(m: MutableMap<Pair<Int, Int>, Int>, i: Int, j: Int) {
        m.compute(i to j) { _, v ->
            if (v == null || v <= 1) null
            else v - 1
        }
    }

    private enum class CellType {
        EMPTY, ROLL_OF_PAPER;

        companion object {
            fun fromChar(c: Char): CellType = if (c == '@') ROLL_OF_PAPER else EMPTY
        }
    }
}