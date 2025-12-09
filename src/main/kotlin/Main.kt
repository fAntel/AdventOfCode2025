package com.keldzh

import kotlin.io.path.Path

fun main() {

}

abstract class Day {
    protected abstract val dayNumber: UByte

    open fun partOne(): String = ""
    open fun partTwo(): String = ""

    protected fun getInputPath(dayNumber: UByte) = Path("src", "main", "kotlin", "days", "input%02d".format(dayNumber.toInt()))
}