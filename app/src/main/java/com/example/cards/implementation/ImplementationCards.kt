package com.example.cards.implementation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.cards.common.CardState
import com.example.cards.common.CardsId
import com.example.cards.common.CardsUiEvent
import com.example.cards.common.feautreblocks.FeatureBlockController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Immutable
data class RedCardState(
    override val cardId: CardsId,
    val specificRedField: String
) : CardState()

@Immutable
data class BlackCardState(
    override val cardId: CardsId,
    val specificBlackField: List<String>
) : CardState()

@Immutable
data class PurpleCardState(
    override val cardId: CardsId,
    val specificPurpleField: Int
) : CardState()

@Immutable
data class BlueCardState(
    override val cardId: CardsId,
    val specificBlueField: List<Int>
) : CardState()

object RedCardController : FeatureBlockController<RedCardState, CardsUiEvent>() {
    override val state: StateFlow<RedCardState>
        get() = MutableStateFlow(RedCardState(CardsId.RED, ""))
}

object BlueCardController : FeatureBlockController<BlueCardState, CardsUiEvent>() {
    override val state: StateFlow<BlueCardState>
        get() = MutableStateFlow(BlueCardState(CardsId.BLUE, listOf(1, 2, 3)))
}

object BlackCardController : FeatureBlockController<BlackCardState, CardsUiEvent>() {
    override val state: StateFlow<BlackCardState>
        get() = MutableStateFlow(BlackCardState(CardsId.BLACK, listOf("a", "b")))
}

object PurpleCardController : FeatureBlockController<PurpleCardState, CardsUiEvent>() {
    override val state: StateFlow<PurpleCardState>
        get() = MutableStateFlow(PurpleCardState(CardsId.PURPLE, 9))
}

@Composable
fun RedCard(state: RedCardState) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Red)
    )
    SideEffect {
        Log.d("RECOMPOSITION", "R!!! - RedCard")
    }
}


@Composable
fun BlackCard(state: BlackCardState) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
    )
    SideEffect {
        Log.d("RECOMPOSITION", "R!!! - BlackCard")
    }
}

@Composable
fun BlueCard(state: BlueCardState) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Blue)
    )
    SideEffect {
        Log.d("RECOMPOSITION", "R!!! - BlueCard")
    }
}

@Composable
fun PurpleCard(state: PurpleCardState) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Magenta)
    )
    SideEffect {
        Log.d("RECOMPOSITION", "R!!! - PurpleCard")
    }
}