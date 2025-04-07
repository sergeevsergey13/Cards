package com.example.cards.common.feautreblocks

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
public inline fun <reified T> FeatureBlockView(
    key: String,
    hostState: State<FeatureBlockHostModel<*>>,
    crossinline onEvent: (FeatureBlockUiEvent) -> Unit,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable BoxScope.(state: State<T>, onEvent: (Any) -> Unit) -> Unit,
) {
    val state = remember(key) {
        derivedStateOf {
            hostState.value
                .featureBlocksStates[key]
                .requireNotNull()
                .cast<T>()
        }
    }
    Box(modifier = modifier, contentAlignment) {
        content(
            state,
        ) { event -> onEvent(FeatureBlockUiEvent(key, event)) }
    }
}
