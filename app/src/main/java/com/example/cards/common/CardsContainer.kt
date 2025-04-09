package com.example.cards.common

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.snapTo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cards.common.feautreblocks.FeatureBlockHostModel
import com.example.cards.common.feautreblocks.FeatureBlockUiEvent
import com.example.cards.common.feautreblocks.FeatureBlockView
import kotlin.math.absoluteValue

@Composable
fun CardsContainer(
    modifier: Modifier = Modifier,
    vm: CardsContainerViewModel = viewModel()
) {
    val hostState = vm.hostCardState.collectAsState()

    CardsContainerView(
        modifier = modifier,
        hostState = hostState,
        onEvent = vm::onUiEvent
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CardsContainerView(
    modifier: Modifier = Modifier,
    hostState: State<FeatureBlockHostModel<CardContainerState>>,
    onEvent: (FeatureBlockUiEvent) -> Unit,
) {
    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val cardWidth = remember { maxWidth - 28.dp }
        val cardWidthPx = with(LocalDensity.current) { cardWidth.toPx() }
        val customProgress = remember { mutableFloatStateOf(0f) }

        val stateAnchor = remember {
            AnchoredDraggableState(
                initialValue = DragAnchors.CENTER,
                positionalThreshold = { distance: Float -> distance * 0.15f },
                velocityThreshold = { cardWidthPx },
                animationSpec = tween(),
                anchors = DraggableAnchors {
                    DragAnchors.CENTER at 0f
                    DragAnchors.LEFT at -cardWidthPx - 100f
                }
            )
        }

        hostState.value.state.cardsOrder.take(VISIBLE_CARDS).forEachIndexed { index, cardsIds ->
            key(cardsIds) {
                FeatureBlockView<CardState>(
                    key = cardsIds.toString(),
                    hostState = hostState,
                    content = { cardState, cardEvent ->
                        BindContainer(
                            state = cardState.value,
                            index = index,
                            cardWidth = cardWidth,
                            progress = customProgress.floatValue
                        )
                    },
                    onEvent = onEvent,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(HEIGHT.dp)
                        .zIndex(VISIBLE_CARDS.toFloat() - index)
                        .addAnchorDraggableIfFirst(stateAnchor, index),
                    contentAlignment = Alignment.CenterStart
                )
            }
        }

        Box(
            modifier = Modifier
                .height(HEIGHT.dp)
                .width(maxWidth)
                .zIndex(5f),
            contentAlignment = Alignment.BottomCenter
        ) {
            PageIndicator(
                currentIndex = hostState.value.state.currentPageIndex,
                totalElements = 4,
                progress = customProgress.floatValue,
                modifier = Modifier
                    .padding(bottom = 12.dp, end = 14.dp)
            )
        }

        LaunchedEffect(stateAnchor.currentValue) {
            if (stateAnchor.currentValue == DragAnchors.LEFT) {
                stateAnchor.snapTo(DragAnchors.CENTER)
                onEvent(FeatureBlockUiEvent("", CardsUiEvent.SwipedLeft))
                customProgress.floatValue = 0f
            }
        }

        LaunchedEffect(stateAnchor.progress) {
            if (stateAnchor.progress == 1f) return@LaunchedEffect
            //уменьшаем немного эмиты прогресса свайпа
            val diff = (customProgress.floatValue - stateAnchor.progress).absoluteValue
            if (diff >= 0.01f) {
                customProgress.floatValue = stateAnchor.progress
            }
        }
    }
}

@Composable
fun PageIndicator(
    currentIndex: Int,
    totalElements: Int,
    progress: Float,
    modifier: Modifier
) {
    val activeColor = remember { Color(0xFF7E8894) }
    val inactiveColor = remember { Color.White.copy(0.2f) }

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        repeat(totalElements) { index ->
            val isCurrent = index == currentIndex
            val isNext = index == (currentIndex + 1) % totalElements
            val isWrapAround = currentIndex == totalElements - 1 && index == 0

            val color = when {
                isCurrent && progress < 1f -> lerpColor(activeColor, inactiveColor, progress)
                isNext && currentIndex != totalElements - 1 -> lerpColor(
                    inactiveColor,
                    activeColor,
                    progress
                )

                isWrapAround -> lerpColor(inactiveColor, activeColor, progress)
                else -> inactiveColor
            }

            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}

fun lerpColor(startColor: Color, endColor: Color, fraction: Float): Color {
    return Color(
        red = lerp(startColor.red, endColor.red, fraction),
        green = lerp(startColor.green, endColor.green, fraction),
        blue = lerp(startColor.blue, endColor.blue, fraction),
        alpha = lerp(startColor.alpha, endColor.alpha, fraction),
    )
}

