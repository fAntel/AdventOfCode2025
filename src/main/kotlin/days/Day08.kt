package com.keldzh.days

import com.keldzh.Day
import java.util.LinkedList
import java.util.TreeMap
import kotlin.io.path.readLines
import kotlin.math.pow
import kotlin.math.sqrt

private typealias Coord = Triple<Int, Int, Int>
private typealias BoxesPair = Pair<Coord, Coord>

class Day08 : Day() {
    override val dayNumber: UByte
        get() = 8u

    override fun partOne(): String {
        val boxes = readBoxes()
        val distances = boxes.calculateDistances()

        val circuits = LinkedList<HashSet<Coord>>()
        repeat(1000) {
            connectPairs(distances, circuits)
        }

        return circuits
            .map { set -> set.size.toLong() }
            .sortedDescending()
            .take(3)
            .reduce { a, b -> a * b }
            .toString()
    }

    override fun partTwo(): String {
        val boxes = readBoxes()
        val distances = boxes.calculateDistances()

        val circuits = LinkedList<HashSet<Coord>>()
        var lastConnectedBoxesPair: BoxesPair
        do {
            lastConnectedBoxesPair = connectPairs(distances, circuits)
        } while (circuits[0].size != boxes.size)
        return (lastConnectedBoxesPair.first.x.toLong() * lastConnectedBoxesPair.second.x.toLong()).toString()
    }

    private fun readBoxes(): List<Triple<Int, Int, Int>> = getInputPath(dayNumber)
        .readLines()
        .map { line ->
            line.split(',')
                .map { it.toInt() }
                .let { (a, b, c) -> Triple(a, b, c) }
        }

    private fun List<Coord>.calculateDistances(): TreeMap<Double, LinkedList<BoxesPair>> {
        val distances = TreeMap<Double, LinkedList<BoxesPair>>()
        for (i in 0..<lastIndex) {
            for (j in i + 1..<size) {
                val distance = calculateDistance(this[i], this[j])
                distances.getOrPut(distance) { LinkedList() }.add(this[i] to this[j])
            }
        }
        return distances
    }

    private fun calculateDistance(a: Coord, b: Coord): Double =
        sqrt((a.x - b.x).toDouble().pow(2) + (a.y - b.y).toDouble().pow(2) + (a.z - b.z).toDouble().pow(2))

    private fun connectPairs(
        distances: TreeMap<Double, LinkedList<BoxesPair>>,
        circuits: LinkedList<HashSet<Coord>>
    ) : BoxesPair {
        val (k, v) = distances.firstEntry()
        val boxesPair = v.first()
        if (v.size == 1) {
            distances.remove(k)
        } else {
            v.remove(boxesPair)
        }

        val circuitsWithBoxes = LinkedList<HashSet<Coord>>()
        for (circuit in circuits) {
            if (circuit.contains(boxesPair.first)) {
                circuitsWithBoxes.add(circuit)
                if (circuitsWithBoxes.size > 1)
                    break
                else
                    continue
            } else if (circuit.contains(boxesPair.second)) {
                circuitsWithBoxes.add(circuit)
                if (circuitsWithBoxes.size > 1)
                    break
                else
                    continue
            }
        }

        when (circuitsWithBoxes.size) {
            0 -> circuits.add(HashSet<Coord>().apply { add(boxesPair) })
            1 -> circuitsWithBoxes[0].add(boxesPair)
            2 -> {
                circuitsWithBoxes[0].addAll(circuitsWithBoxes[1])
                circuits.remove(circuitsWithBoxes[1])
            }
        }

        return boxesPair
    }

    private val Coord.x by Coord::first
    private val Coord.y by Coord::second
    private val Coord.z by Coord::third

    private fun HashSet<Coord>.add(pair: BoxesPair) {
        add(pair.first)
        add(pair.second)
    }
}