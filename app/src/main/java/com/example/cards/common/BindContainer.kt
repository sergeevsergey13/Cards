package com.example.cards.common

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.example.cards.implementation.BlackCard
import com.example.cards.implementation.BlackCardState
import com.example.cards.implementation.BlueCard
import com.example.cards.implementation.BlueCardState
import com.example.cards.implementation.PurpleCard
import com.example.cards.implementation.PurpleCardState
import com.example.cards.implementation.RedCard
import com.example.cards.implementation.RedCardState
import kotlin.math.roundToInt

@Composable
fun BindContainer(
    modifier: Modifier = Modifier,
    state: CardState,
    cardWidth: Dp,
    index: Int,
    progress: Float
) {
    val density = LocalDensity.current

    val offset = with(density) {
        IntOffset(
            x = when (index) {
                0 -> 0.dp.toPx().roundToInt()
                1 -> lerp(32.dp.toPx(), 0f, progress).roundToInt()
                2 -> lerp(56.dp.toPx(), 32.dp.toPx(), progress).roundToInt()
                3 -> 56.dp.toPx().roundToInt()
                else -> throw RuntimeException()
            },
            y = 0
        )
    }

    // Вычисляем стартовые скейлы для карточек 1 и 2
    val startScale1 = (cardWidth - 32.dp) / cardWidth
    val startScale2 = (cardWidth - 56.dp) / cardWidth

    val scaleState = when (index) {
        0 -> 1f
        1 -> lerp(startScale1, 1f, progress)
        2 -> lerp(startScale2, startScale1, progress)
        3 -> startScale2
        else -> throw RuntimeException()
    }

    Box(
        modifier = modifier
            .width(cardWidth)
            .offset { offset }
            .graphicsLayer {
                scaleX = scaleState
                scaleY = scaleState
            }
            .clip(RoundedCornerShape(24.dp)),
        contentAlignment = Alignment.CenterStart
    ) {
        when (state) {
            is BlueCardState -> BlueCard(state)
            is BlackCardState -> BlackCard(state)
            is RedCardState -> RedCard(state)
            is PurpleCardState -> PurpleCard(state)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
fun Modifier.addAnchorDraggableIfFirst(
    state: AnchoredDraggableState<DragAnchors>,
    index: Int,
): Modifier {
    return if (index == 0) {
        this
            .offset {
                IntOffset(
                    x = state
                        .requireOffset()
                        .roundToInt(),
                    y = 0
                )
            }
            .anchoredDraggable(state, Orientation.Horizontal)
    } else {
        this
    }
}

enum class DragAnchors {
    LEFT,
    CENTER
}