package com.example.cards.common

import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.snapTo
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cards.common.feautreblocks.FeatureBlockHostModel
import com.example.cards.common.feautreblocks.FeatureBlockUiEvent
import com.example.cards.common.feautreblocks.FeatureBlockView

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
        contentAlignment = Alignment.CenterStart
    ) {
        val cardWidth = remember { maxWidth - 28.dp }
        val cardWidthPx = with(LocalDensity.current) { cardWidth.toPx() }
        val customProgress = remember { mutableFloatStateOf(0f) }

        val stateAnchor = remember {
            AnchoredDraggableState(
                initialValue = DragAnchors.CENTER,
                positionalThreshold = { distance: Float -> distance * 0.15f },
                velocityThreshold = { cardWidthPx },
                animationSpec = tween(ANIM_DURATION),
                anchors = DraggableAnchors {
                    DragAnchors.CENTER at 0f
                    DragAnchors.LEFT at -cardWidthPx - 100
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

        LaunchedEffect(stateAnchor.currentValue) {
            if (stateAnchor.currentValue == DragAnchors.LEFT) {
                stateAnchor.snapTo(DragAnchors.CENTER)
                onEvent(FeatureBlockUiEvent("", CardsUiEvent.SwipedLeft))
            }
        }

        LaunchedEffect(stateAnchor.progress) {
            Log.d("SSV", "${stateAnchor.progress}")
        }
    }
}