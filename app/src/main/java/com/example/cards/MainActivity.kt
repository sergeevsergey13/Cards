package com.example.cards

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.cards.ui.theme.CardsContainer
import com.example.cards.ui.theme.CardsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CardsTheme {
                CardsContainer()
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    CardsTheme {
//        CardRootView()
//    }
//}

//@OptIn(ExperimentalFoundationApi::class)
//@Composable
//fun CardRootView(
//    modifier: Modifier = Modifier
//) {
//    val density = LocalDensity.current
//    val pxValue = with(density) { DIFF.dp.toPx() }
//
//    Layout(
//        content = {
//
//            val state = remember {
//                AnchoredDraggableState(
//                    // 2
//                    initialValue = DragAnchors.CENTER,
//                    // 3
//                    positionalThreshold = { distance: Float -> distance * 0.5f },
//                    // 4
//                    velocityThreshold = { with(density) { 100.dp.toPx() } },
//                    // 5
//                    animationSpec = tween(),
//                ).apply {
//                    // 6
//                    updateAnchors(
//                        // 7
//                        DraggableAnchors {
//                            DragAnchors.RIGHT at 100f
//                            DragAnchors.CENTER at 0f
//                            DragAnchors.LEFT at -100f
//                        }
//                    )
//                }
//            }
//
//
//            LaunchedEffect(state.progress) {
//                Log.d("SSV", state.progress.toString())
//            }
//
//            CardView(
//                Card.BLACK,
//                modifier = Modifier
//                    .offset {
//                        IntOffset(
//                            x = state
//                                .requireOffset()
//                                .roundToInt(),
//                            y = 0,
//                        )
//                    }
//                    .anchoredDraggable(state, Orientation.Horizontal),
//                progressState = state.progress
//            )
////            CardView(Card.GRAY, 1, progressState = state.progress)
////            CardView(Card.BLUE, 2, progressState = state.progress)
//        },
//        modifier = modifier.fillMaxWidth()
//    ) { measurables, constraints ->
//
//        val placeables = measurables.take(SIZE).map { measurable ->
//            measurable.measure(constraints)
//        }
//        layout(constraints.maxWidth, constraints.maxHeight) {
//            var x = constraints.maxWidth - placeables.first().width
//            var y = (SIZE - 1) * pxValue
//            var z = 0f
//
//            placeables.fastForEachReversed { item ->
//                item.place(x = x, y = y.roundToInt(), zIndex = z)
//                x -= pxValue.roundToInt()
//                y -= pxValue / 2
//                z += 1
//            }
//        }
//    }
//}


//@Preview
//@Composable
//fun CardViewPreview() {
//    val progressState = remember { mutableFloatStateOf(1f) }
//    Log.d("SSV", progressState.floatValue.toString())
//    CardView(
//        card = Card.PURPLE,
////        index = 1,
//        progressState = progressState.floatValue,
//        modifier = Modifier.padding(20.dp)
//    )
//
//    LaunchedEffect(Unit) {
//        CoroutineScope(Dispatchers.IO).launch {
//            while (progressState.floatValue > 0f) {
//                delay(50)
//                progressState.floatValue -= 0.01f
//            }
//            while (progressState.floatValue <= 1f) {
//                delay(50)
//                progressState.floatValue += 0.01f
//            }
//        }
//    }
//}

