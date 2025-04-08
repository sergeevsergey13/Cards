package com.example.cards.common

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.snapTo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BindContainer(
    modifier: Modifier = Modifier,
    state: CardState,
    cardWidth: Dp,
    index: Int,
    onEvent: (CardsUiEvent) -> Unit
) {
    val offset = animateIntOffsetAsState(
        targetValue = with(LocalDensity.current) { IntOffset(x = getIndexOffset(index), y = 0) },
        label = "",
        animationSpec = tween(ANIM_DURATION)
    )

    val height = animateDpAsState(
        targetValue = (HEIGHT - getIndexOffsetInt(index)).dp,
        label = "",
        animationSpec = tween(ANIM_DURATION)
    )
    val cardWidthPx = with(LocalDensity.current) { cardWidth.toPx() }

    val stateAnchor = remember {
        AnchoredDraggableState(
            initialValue = DragAnchors.CENTER,
            positionalThreshold = { distance: Float -> distance * 0.3f },
            velocityThreshold = { cardWidthPx },
            animationSpec = tween(),
            anchors = DraggableAnchors {
                DragAnchors.CENTER at 0f
                DragAnchors.LEFT at -cardWidthPx - 100
            }
        )
    }

    Box(
        modifier = modifier
            .offset { offset.value }
            .height(height.value)
            .width(cardWidth)
            .addAnchorDraggableIfFirst(state = stateAnchor, index = index)
            .clip(RoundedCornerShape(24.dp)),
        contentAlignment = Alignment.Center
    ) {
        when (state) {
            is BlueCardState -> BlueCard(state)
            is BlackCardState -> BlackCard(state)
            is RedCardState -> RedCard(state)
            is PurpleCardState -> PurpleCard(state)
        }
    }

    LaunchedEffect(stateAnchor.currentValue) {
        if (stateAnchor.currentValue == DragAnchors.LEFT) {
            onEvent(CardsUiEvent.SwipedLeft)
            stateAnchor.snapTo(DragAnchors.CENTER)
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