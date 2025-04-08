package com.example.cards.common

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.tween
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
    val offset = animateIntOffsetAsState(
        targetValue = with(LocalDensity.current) { IntOffset(x = getIndexOffset(index), y = 0) },
        label = "",
        animationSpec = tween(ANIM_DURATION)
    )

    val scaleState = animateFloatAsState(
        targetValue = when (index) {
            0 -> 1f
            1 -> 1f - (32.dp / cardWidth)
            2 -> 1f - (56.dp / cardWidth)
            else -> throw RuntimeException()
        },
        animationSpec = tween(ANIM_DURATION),
        label = ""
    )

    val roundedCorners = animateDpAsState(
        targetValue = when (index) {
            0 -> 24.dp
            1 -> 20.dp
            2 -> 18.dp
            else -> throw RuntimeException()
        },
        animationSpec = tween(ANIM_DURATION),
        label = ""
    )

    Box(
        modifier = modifier
            .width(cardWidth)
            .offset { offset.value }
            .graphicsLayer {
                scaleX = scaleState.value
                scaleY = scaleState.value
            }
            .clip(RoundedCornerShape(roundedCorners.value)),
        contentAlignment = Alignment.Center
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