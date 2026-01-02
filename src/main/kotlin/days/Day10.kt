package com.keldzh.days

import com.keldzh.Day
import kotlin.io.path.readLines
import kotlin.math.max

class Day10 : Day() {
    override val dayNumber: UByte
        get() = 10u

    override fun partOne(): String {
        val machines = readMachines()

        var counter = 0
        loop@for ((diagram, buttons) in machines) {
            for (t in 1..buttons.size) {
                val seq = buttons.combinations(t)
                    .filter { set -> diagram.doesEnableDiagram(set) }
                if (seq.any { set -> diagram.doesMatchDiagram(set) }) {
                    counter += t
                    continue@loop
                }
            }
        }

        return counter.toString()
    }

    private fun LightDiagram.doesMatchDiagram(buttons: Collection<ButtonWiringSchematics>): Boolean =
        pattern == buttons.fold(0) { acc, schematics -> acc xor schematics.pattern }

    private fun LightDiagram.doesEnableDiagram(buttons: Collection<ButtonWiringSchematics>): Boolean =
        pattern and buttons.fold(0) { acc, schematics -> acc xor schematics.pattern } == pattern

    private fun List<ButtonWiringSchematics>.combinations(t: Int): Sequence<Set<ButtonWiringSchematics>> =
        sequence {
            if (t < 1)
                return@sequence
            if (t == 1) {
                forEach { button ->
                    yield(setOf(button))
                }
                return@sequence
            }
            if (t == size) {
                yield(toSet())
                return@sequence
            }

            val a = takeLast(t).toMutableSet()
            val w = IntArray(size + 1) { 1 }
            var r = size - t
            var j = r
            do {
                fun c4() {
                    a.add(get(j - 1))
                    a.remove(get(j))

                    if (r == j && j > 1) {
                        r = j - 1
                    } else if (r == j - 1) {
                        r = j
                    }
                }
                fun c5() {
                    if (get(j - 2) in a) {
                        c4()
                    } else {
                        a.add(get(j - 2))
                        a.remove(get(j))

                        if (r == j) {
                            r = max(j - 2, 1)
                        } else if (r == j - 2) {
                            r = j - 1
                        }
                    }
                }
                fun c6() {
                    a.add(get(j))
                    a.remove(get(j - 1))

                    if (r == j && j > 1) {
                        r = j - 1
                    } else if (r == j - 1) {
                        r = j
                    }
                }
                fun c7() {
                    if (get(j - 1) in a) {
                        c6()
                    } else {
                        a.add(get(j))
                        a.remove(get(j - 2))

                        if (r == j - 2) {
                            r = j
                        } else if (r == j - 1) {
                            r = j - 2
                        }
                    }
                }

                yield(a.toSet())

                j = r
                while (w[j] == 0) {
                    w[j++] = 1
                }
                if (j == size)
                    break

                w[j] = 0

                if (j % 2 != 0 && get(j) in a) {
                    c4()
                } else if (j % 2 == 0 && get(j) in a) {
                    c5()
                } else if (j % 2 == 0 && get(j) !in a) {
                    c6()
                } else if (j % 2 != 0 && get(j) !in a) {
                    c7()
                }
            } while (true)
        }

    private fun readMachines(): List<Machine> =
        getInputPath(dayNumber)
            .readLines()
            .map { line -> Machine.fromString(line) }

    private class LightDiagram(val pattern: Int, val length: Int) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as LightDiagram

            if (pattern != other.pattern) return false
            if (length != other.length) return false

            return true
        }

        override fun hashCode(): Int {
            var result = pattern
            result = 31 * result + length
            return result
        }

        override fun toString(): String = buildString(length + 2) {
            append('[')

            var counter = this@LightDiagram.length
            var i = 1
            while (counter > 0) {
                append(if (pattern and i == 0) '.' else '#')
                i = i shl 1
                counter -= 1
            }

            append(']')
        }

        companion object {
            fun fromString(str: String): LightDiagram {
                val len = str.length - 2
                if (len <= 0)
                    throw IllegalArgumentException("String is too short")
                if (str.first() != '[' || str.last() != ']')
                    throw IllegalArgumentException("Wrong format: doesn't enclosed in square brackets")

                var pattern = 0
                var j = 1
                for (i in 1..<str.lastIndex) {
                    if (str[i] == '#') {
                        pattern = pattern or j
                    }
                    j = j shl 1
                }

                return LightDiagram(pattern, len)
            }
        }
    }

    private class ButtonWiringSchematics(val pattern: Int, val size: Int) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as ButtonWiringSchematics

            if (pattern != other.pattern) return false
            if (size != other.size) return false

            return true
        }

        override fun hashCode(): Int {
            var result = pattern
            result = 31 * result + size
            return result
        }

        override fun toString(): String = buildList {
            var counter = 0
            var j = 1
            while (this.size < this@ButtonWiringSchematics.size) {
                if (pattern and j != 0) {
                    add(counter)
                }
                counter += 1
                j = j shl 1
            }
        }
            .joinToString(prefix = "(", postfix = ")")

        companion object {
            fun fromString(str: String): ButtonWiringSchematics {
                if (str.first() != '(' || str.last() != ')')
                    throw IllegalArgumentException("Wrong format: doesn't enclosed in brackets")

                val indices = str
                    .substring(1, str.lastIndex)
                    .split(',')
                    .map { it.trim().toInt() }

                if (indices.isEmpty())
                    throw IllegalArgumentException("String is too short")

                var pattern = 0
                var j = 1
                var counter = 0
                for (i in indices) {
                    while (counter < i) {
                        counter += 1
                        j = j shl 1
                    }

                    pattern = pattern or j
                }

                return ButtonWiringSchematics(pattern, indices.size)
            }
        }
    }

    private class Machine(val lightDiagram: LightDiagram, val buttons: List<ButtonWiringSchematics>) {
        operator fun component1() = lightDiagram
        operator fun component2() = buttons

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Machine

            if (lightDiagram != other.lightDiagram) return false
            if (buttons != other.buttons) return false

            return true
        }

        override fun hashCode(): Int {
            var result = lightDiagram.hashCode()
            result = 31 * result + buttons.hashCode()
            return result
        }

        override fun toString(): String =
            "Machine($lightDiagram ${buttons.joinToString(separator = " ")})"

        companion object {
            fun fromString(str: String): Machine {
                val data = str.split(' ')
                val diagram = LightDiagram.fromString(data.first())
                val buttons = data.subList(1, data.lastIndex).map { str -> ButtonWiringSchematics.fromString(str) }

                return Machine(diagram, buttons)
            }
        }
    }
}