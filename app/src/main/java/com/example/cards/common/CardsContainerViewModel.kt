package com.example.cards.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cards.common.feautreblocks.FeatureBlockHost
import com.example.cards.common.feautreblocks.FeatureBlockUiEvent
import com.example.cards.common.feautreblocks.IFeatureBlockHost
import com.example.cards.common.feautreblocks.cast
import com.example.cards.implementation.BlackCardController
import com.example.cards.implementation.BlueCardController
import com.example.cards.implementation.PurpleCardController
import com.example.cards.implementation.RedCardController
import kotlinx.coroutines.flow.MutableStateFlow

class CardsContainerViewModel :
    ViewModel(),
    IFeatureBlockHost<CardContainerState> by FeatureBlockHost(
        featureBlockControllers = mapOf(
            CardsId.BLACK.toString() to BlackCardController,
            CardsId.BLUE.toString() to BlueCardController,
            CardsId.RED.toString() to RedCardController,
            CardsId.PURPLE.toString() to PurpleCardController,
        )
    ) {

    private val _hostCardState = MutableStateFlow(
        CardContainerState(CardsId.entries.toList())
    )

    val hostCardState = hostState(
        initialState = _hostCardState.value,
        state = _hostCardState,
        viewModelScope = viewModelScope
    )

    fun onUiEvent(cardsUiEvent: Any) {
        if (cardsUiEvent.cast<FeatureBlockUiEvent>().event is CardsUiEvent.SwipedLeft) {
            val prevState = _hostCardState.value.cardsOrder
            val firstElement = prevState.first()
            val newState = prevState.drop(1) + firstElement
            _hostCardState.value = CardContainerState(newState)
        }
    }
}