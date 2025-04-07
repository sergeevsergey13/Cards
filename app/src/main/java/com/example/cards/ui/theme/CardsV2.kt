package com.example.cards.ui.theme

import androidx.compose.runtime.Immutable
import kotlinx.coroutines.flow.MutableStateFlow

sealed class CardState {
    abstract val cardId: Int
}

data class RedCard(
    override val cardId: Int,
    val specificRedField: String
): CardState()

data class BlackCard(
    override val cardId: Int,
    val specificBlackField: List<String>
): CardState()

data class PurpleCard(
    override val cardId: Int,
    val specificPurpleField: Int
): CardState()

data class BlueCard(
    override val cardId: Int,
    val specificBlueField: List<Int>
): CardState()

@Immutable
data class CardContainerState(
    val containerState: List<CardState>
)

object CardContainerController {
    val state = MutableStateFlow(
        CardContainerState(
            containerState = listOf(
                BlackCard(0, listOf("a", "b", "c")),

            )
        )
    )
}

