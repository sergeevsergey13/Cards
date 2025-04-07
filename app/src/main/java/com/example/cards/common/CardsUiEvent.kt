package com.example.cards.common

sealed class CardsUiEvent {
    data object SwipedLeft: CardsUiEvent()
}