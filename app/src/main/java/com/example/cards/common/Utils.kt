package com.example.cards.common

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

fun getIndexOffsetInt(index: Int): Int {
    return when (index) {
        0 -> 0
        1 -> FIRST_OFFSET
        2 -> SECOND_OFFSET
        else -> throw RuntimeException()
    }
}

fun Density.getIndexOffset(index: Int): Int {
    return getIndexOffsetInt(index).dp.toPx().roundToInt()
}