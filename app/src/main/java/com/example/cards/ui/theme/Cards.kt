//package com.example.cards.ui.theme
//
//import android.util.Log
//import androidx.compose.animation.core.animateDpAsState
//import androidx.compose.animation.core.animateIntOffsetAsState
//import androidx.compose.animation.core.tween
//import androidx.compose.foundation.ExperimentalFoundationApi
//import androidx.compose.foundation.background
//import androidx.compose.foundation.gestures.AnchoredDraggableState
//import androidx.compose.foundation.gestures.DraggableAnchors
//import androidx.compose.foundation.gestures.Orientation
//import androidx.compose.foundation.gestures.anchoredDraggable
//import androidx.compose.foundation.gestures.snapTo
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.BoxWithConstraints
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.offset
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.systemBarsPadding
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.SideEffect
//import androidx.compose.runtime.Stable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.key
//import androidx.compose.runtime.mutableIntStateOf
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalDensity
//import androidx.compose.ui.unit.Density
//import androidx.compose.ui.unit.Dp
//import androidx.compose.ui.unit.IntOffset
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.zIndex
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlin.math.roundToInt
//
//data class Card(
//    val id: Int,
//    val color: Enum<CardColor>
//) {
//
//    enum class CardColor {
//        BLACK, PURPLE, BLUE, GRAY
//    }
//}
//
//
//enum class DragAnchors {
//    LEFT,
//    CENTER
//}
//
//@Composable
//fun CardsView(
//    listCards: List<Card>,
//    modifier: Modifier = Modifier
//) {
//    BoxWithConstraints(
//        modifier = modifier.fillMaxWidth(),
//        contentAlignment = Alignment.CenterStart
//    ) {
//        CardsBoxView(
//            maxWidth = maxWidth,
//            listCards = listCards
//        )
//    }
//
//    SideEffect {
//        Log.d("RECOMPOSITION", "CardsView")
//    }
//}
//
//@Composable
//fun CardsBoxView(
//    maxWidth: Dp,
//    listCards: List<Card>
//) {
//    val cardsWidth = remember { maxWidth - 28.dp }
//
//    listCards.take(3).forEachIndexed { index, card ->
//        key(card.id) {
//            Box(
//                modifier = Modifier
//                    .height(HEIGHT.dp)
//                    .zIndex(3f - index),
//                contentAlignment = Alignment.CenterStart
//            ) {
//                CardView(
//                    card = card,
//                    cardWidth = cardsWidth,
//                    index = index,
//                    modifier = Modifier
//                )
//            }
//        }
//    }
//
//    SideEffect {
//        Log.d("RECOMPOSITION", "CardsBoxView")
//    }
//}
//
//private val HEIGHT = 326
//private val ANIM_DURATION = 250
//
//@Composable
//fun CardsContainer() {
//    val state = CardsController.cardsState.collectAsState().value
//
//    CardsView(
//        state.listCards, modifier = Modifier
//            .systemBarsPadding()
//            .padding(horizontal = 16.dp, vertical = 16.dp)
//    )
//
//    SideEffect {
//        Log.d("RECOMPOSITION", "CardsContainer")
//    }
//}
//
//@OptIn(ExperimentalFoundationApi::class)
//@Stable
//@Composable
//fun CardView(
//    card: Card,
//    cardWidth: Dp,
//    index: Int,
//    modifier: Modifier = Modifier
//) {
//    val density = LocalDensity.current
//    val color =  remember { when (card.color) {
//        Card.CardColor.BLACK -> Color.Black
//        Card.CardColor.PURPLE -> Color.Magenta
//        Card.CardColor.BLUE -> Color.Blue
//        Card.CardColor.GRAY -> Color.Gray
//        else -> throw RuntimeException()
//    } }
//    val cardWidthPx = remember { with(density) { cardWidth.toPx() } }
//
//    val diffHeight = remember { mutableIntStateOf(0) }
//
//    LaunchedEffect(index) {
//        diffHeight.intValue = getIndexOffsetInt(index)
//    }
//
//    val height = animateDpAsState(
//        targetValue = (HEIGHT - diffHeight.value).dp,
//        label = "",
//        animationSpec = tween(ANIM_DURATION)
//    )
//    val offset = animateIntOffsetAsState(
//        with(density) {
//            IntOffset(x = getIndexOffset(index), y = 0)
//        }, label = "",
//        animationSpec = tween(ANIM_DURATION)
//    )
//
//    val state = remember {
//        AnchoredDraggableState(
//            initialValue = DragAnchors.CENTER,
//            positionalThreshold = { distance: Float -> distance * 0.3f },
//            velocityThreshold = { cardWidthPx },
//            animationSpec = tween(),
//        ).apply {
//            updateAnchors(
//                DraggableAnchors {
//                    DragAnchors.CENTER at 0f
//                    DragAnchors.LEFT at -cardWidthPx - 100
//                }
//            )
//        }
//    }
//
//    Box(
//        modifier = modifier
//            .width(cardWidth)
//            .height(height.value)
//            .offset { offset.value }
//            .addAnchorDraggableIfFirst(state, index)
//            .clip(RoundedCornerShape(24.dp))
//            .background(color = color)
//    )
//
//    LaunchedEffect(state.currentValue) {
//        if (state.currentValue == DragAnchors.LEFT) {
//            state.snapTo(DragAnchors.CENTER)
//            CardsController.swipedLeft()
//        }
//    }
//
//    SideEffect {
//        Log.d("RECOMPOSITION", "CardView $card")
//    }
//}
//
//data class CardsState(
//    val listCards: List<Card>
//)
//
//object CardsController {
//    val cardsState = MutableStateFlow(
//        CardsState(
//            listCards = listOf(
//                Card(id = 0, color = Card.CardColor.BLUE),
//                Card(id = 1, color = Card.CardColor.BLACK),
//                Card(id = 2, color = Card.CardColor.GRAY),
//                Card(id = 3, color = Card.CardColor.PURPLE)
//            )
//        )
//    )
//
//    fun swipedLeft() {
//        val prevList = cardsState.value.listCards
//        val firstItem = prevList.first()
//        val newList = prevList.drop(1).plus(firstItem)
//        cardsState.value = CardsState(listCards = newList)
//    }
//}
//
//@OptIn(ExperimentalFoundationApi::class)
//fun Modifier.addAnchorDraggableIfFirst(
//    state: AnchoredDraggableState<DragAnchors>,
//    index: Int,
//): Modifier {
//    return if (index == 0) {
//        this
//            .offset {
//                IntOffset(
//                    x = state
//                        .requireOffset()
//                        .roundToInt(),
//                    y = 0
//                )
//            }
//            .anchoredDraggable(state, Orientation.Horizontal)
//    } else {
//        this
//    }
//}
//
//fun Density.getIndexOffset(index: Int): Int {
//    return getIndexOffsetInt(index).dp.toPx().roundToInt()
//}
//
//fun getIndexOffsetInt(index: Int): Int {
//    return when (index) {
//        0 -> 0
//        1 -> 16
//        2 -> 28
//        else -> throw RuntimeException()
//    }
//}
//
////ROTATE
////.rotate((state.offset / cardWidthPx) * 8f)