package com.example.cards.common

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
        val cardWidth = remember { maxWidth - SECOND_OFFSET.dp }

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
                            onEvent = cardEvent
                        )
                    },
                    onEvent = onEvent,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(HEIGHT.dp)
                        .zIndex(VISIBLE_CARDS.toFloat() - index),
                    contentAlignment = Alignment.CenterStart
                )
            }
        }
    }
}