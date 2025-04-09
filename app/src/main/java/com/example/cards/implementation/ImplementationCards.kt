package com.example.cards.implementation

import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    val specificBlackField: List<String>,
    val scrollState: ScrollState
) : CardState()

@Immutable
data class PurpleCardState(
    override val cardId: CardsId,
    val specificPurpleField: Int,
) : CardState()

@Immutable
data class BlueCardState(
    override val cardId: CardsId,
    val specificBlueField: List<Int>,
    val scrollState: ScrollState,
) : CardState()

object RedCardController : FeatureBlockController<RedCardState, CardsUiEvent>() {
    override val state: StateFlow<RedCardState>
        get() = MutableStateFlow(RedCardState(CardsId.RED, ""))
}

object BlueCardController : FeatureBlockController<BlueCardState, CardsUiEvent>() {
    override val state: StateFlow<BlueCardState>
        get() = MutableStateFlow(BlueCardState(CardsId.BLUE, listOf(1, 2, 3), ScrollState(0)))
}

object BlackCardController : FeatureBlockController<BlackCardState, CardsUiEvent>() {
    override val state: StateFlow<BlackCardState>
        get() = MutableStateFlow(
            BlackCardState(
                CardsId.BLACK,
                listOf("field1", "field2"),
                ScrollState(0)
            )
        )
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
    ) {
        Text(
            text = "Это просто карточка",
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            color = Color.White,
            fontSize = 18.sp
        )
    }
    SideEffect {
        Log.d("RECOMPOSITION", "R!!! - RedCard")
    }
}


@Composable
fun BlackCard(state: BlackCardState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
            .verticalScroll(state = state.scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "Это черная карточка, в которой есть вертикальный скролл",
            modifier = Modifier
                .fillMaxWidth(),
            color = Color.White,
            fontSize = 18.sp
        )

        Text(
            text = "Поля со стейта",
            modifier = Modifier
                .fillMaxWidth(),
            color = Color.White
        )

        state.specificBlackField.forEach {
            Text(it, color = Color.White)
        }

        Text(
            text = LoremIpsum(500).values.joinToString(" "),
            modifier = Modifier
                .fillMaxWidth(),
            color = Color.White
        )
    }
    SideEffect {
        Log.d("RECOMPOSITION", "R!!! - BlackCard")
    }
}

@Composable
fun BlueCard(state: BlueCardState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Blue),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Это синяя карточка, в которой есть горизонтальный скролл",
                modifier = Modifier
                    .fillMaxWidth(),
                color = Color.White,
                fontSize = 18.sp
            )

            Text(
                text = "Поля со стейта",
                modifier = Modifier
                    .fillMaxWidth(),
                color = Color.White
            )

            state.specificBlueField.forEach {
                Text(it.toString(), color = Color.White)
            }
        }

        Row(
            modifier = Modifier.horizontalScroll(state.scrollState),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(Modifier.padding(4.dp))
            repeat(26) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                ) {}
            }
            Spacer(Modifier.padding(4.dp))
        }
    }
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
    ) {
        Text(
            text = "Это просто карточка",
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            color = Color.White,
            fontSize = 18.sp
        )
    }
    SideEffect {
        Log.d("RECOMPOSITION", "R!!! - PurpleCard")
    }
}