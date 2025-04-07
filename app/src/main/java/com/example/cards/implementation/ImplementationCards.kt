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
data class RedCard(
    override val cardId: CardsId,
    val specificRedField: String
) : CardState()

@Immutable
data class BlackCard(
    override val cardId: CardsId,
    val specificBlackField: List<String>
) : CardState()

@Immutable
data class PurpleCard(
    override val cardId: CardsId,
    val specificPurpleField: Int
) : CardState()

@Immutable
data class BlueCard(
    override val cardId: CardsId,
    val specificBlueField: List<Int>
) : CardState()

object RedCardController : FeatureBlockController<RedCard, CardsUiEvent>() {
    override val state: StateFlow<RedCard>
        get() = MutableStateFlow(RedCard(CardsId.RED, ""))
}

object BlueCardController : FeatureBlockController<BlueCard, CardsUiEvent>() {
    override val state: StateFlow<BlueCard>
        get() = MutableStateFlow(BlueCard(CardsId.BLUE, listOf(1, 2, 3)))
}

object BlackCardController : FeatureBlockController<BlackCard, CardsUiEvent>() {
    override val state: StateFlow<BlackCard>
        get() = MutableStateFlow(BlackCard(CardsId.BLACK, listOf("a", "b")))
}

object PurpleCardController : FeatureBlockController<PurpleCard, CardsUiEvent>() {
    override val state: StateFlow<PurpleCard>
        get() = MutableStateFlow(PurpleCard(CardsId.PURPLE, 9))
}

@Composable
fun RedCard() {
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
fun BlackCard() {
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
fun BlueCard() {
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
fun PurpleCard() {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Magenta)
    )
    SideEffect {
        Log.d("RECOMPOSITION", "R!!! - PurpleCard")
    }
}