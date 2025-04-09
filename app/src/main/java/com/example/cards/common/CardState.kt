package com.example.cards.common

import androidx.compose.runtime.Immutable

@Immutable
enum class CardsId {
    BLACK, BLUE, PURPLE, RED
}

@Immutable
data class CardContainerState(
    val cardsOrder: List<CardsId>,
    val currentPageIndex: Int,
)

@Immutable
abstract class CardState {
    abstract val cardId: CardsId
}

