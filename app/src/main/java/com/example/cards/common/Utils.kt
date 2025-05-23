package com.example.cards.common

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

fun getIndexOffsetInt(index: Int): Int {
    return when (index) {
        0 -> 0
        1 -> 16
        2 -> 28
        else -> throw RuntimeException()
    }
}

fun Density.getIndexOffset(index: Int): Int {
    return getIndexOffsetInt(index).dp.toPx().roundToInt()
}