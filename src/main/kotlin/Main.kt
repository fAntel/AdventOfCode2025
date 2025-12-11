package com.keldzh

import com.keldzh.days.Day02
import kotlin.io.path.Path

fun main() {
    val day = Day02()

    println(day.partOne())
    println(day.partTwo())
}

abstract class Day {
    protected abstract val dayNumber: UByte

    open fun partOne(): String = ""
    open fun partTwo(): String = ""

    protected fun getInputPath(dayNumber: UByte) = Path("src", "main", "kotlin", "days", "input%02d".format(dayNumber.toInt()))
}