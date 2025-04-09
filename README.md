Демо работы

https://github.com/user-attachments/assets/3cab74b7-f28a-4afc-8b1b-f674b3cd22fa

Карточки Механика работы

Карточки принимают вью модель(в демо автоматически создается вм) и модифаер, который регулирует размеры корневой вьюшки

```
                CardsContainer(
                    modifier = Modifier
                        .systemBarsPadding()
                        .padding(horizontal = 16.dp)
                )
```

Карточки используют фиче блоки, каждая карточка имеет свой стейт

Хост стейт - стейт контейнера, где только порядок карточек
```
private val _hostCardState = MutableStateFlow(
  CardContainerState(CardsId.entries.toList())
)

@Immutable
enum class CardsId {
    BLACK, BLUE, PURPLE, RED
}

@Immutable
data class CardContainerState(
    val cardsOrder: List<CardsId>
)
```
В дальнейшем поменяем айдишники на продуктовые

Вью модель является хостом, число возможных контейнеров фиксированно
```
class CardsContainerViewModel :
    ViewModel(),
    IFeatureBlockHost<CardContainerState> by FeatureBlockHost(
        featureBlockControllers = mapOf(
            CardsId.BLACK.toString() to BlackCardController,
            CardsId.BLUE.toString() to BlueCardController,
            CardsId.RED.toString() to RedCardController,
            CardsId.PURPLE.toString() to PurpleCardController,
        )
    )
```

При свайпе прилетает ивент свайпа и верхний контейнер ставится в конец
```
    fun onUiEvent(cardsUiEvent: Any) {
        if (cardsUiEvent.cast<FeatureBlockUiEvent>().event is CardsUiEvent.SwipedLeft) {
            val prevState = _hostCardState.value.cardsOrder
            val firstElement = prevState.first()
            val newState = prevState.drop(1) + firstElement
            _hostCardState.value = CardContainerState(newState)
        }
    }
```

Контейнер рассчитывает доступную ширину через BoxWithConstraints и его maxWidth
```
@Composable
fun CardsContainerView(
    modifier: Modifier = Modifier,
    hostState: State<FeatureBlockHostModel<CardContainerState>>,
    onEvent: (FeatureBlockUiEvent) -> Unit,
) {
    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.CenterStart
    ) {
        val cardWidth = remember { maxWidth - SECOND_OFFSET.dp }

        hostState.value.state.cardsOrder.take(VISIBLE_CARDS).forEachIndexed { index, cardsIds ->
            key(cardsIds) {
                FeatureBlockView<CardState>(
                    key = cardsIds.toString(),
                    hostState = hostState,
                    content = { cardState, cardEvent ->
                        BindContainer(
                            state = cardState.value,
                            index = index,
                            cardWidth = cardWidth,
                            onEvent = cardEvent
                        )
                    },
                    onEvent = onEvent,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(HEIGHT.dp)
                        .zIndex(VISIBLE_CARDS.toFloat() - index),
                    contentAlignment = Alignment.CenterStart
                )
            }
        }
    }
}
```
SECOND_OFFSET - фиксированный оффсет для последней карточки - 28.dp
Это значит что все карточки одинаковой ширины height(HEIGHT.dp), только смещены каждый на свой оффсет. С нулевым индексом на 0, с первым на 16, второй на 28
Фиче блок имеет фиксированный размер, но сам контент меняется в зависимости от индекса по формуле
.zIndex(VISIBLE_CARDS.toFloat() - index) ставит нужный Z индекс таким образом, чтобы карточка с индексом 0 была самая верхняя
Рисуются только верхние 3 карточки, даже если их больше

```
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BindContainer(
    modifier: Modifier = Modifier,
    state: CardState,
    cardWidth: Dp,
    index: Int,
    onEvent: (CardsUiEvent) -> Unit
) {
    val offset = animateIntOffsetAsState(
        targetValue = with(LocalDensity.current) { IntOffset(x = getIndexOffset(index), y = 0) },
        label = "",
        animationSpec = tween(ANIM_DURATION)
    )

    val height = animateDpAsState(
        targetValue = (HEIGHT - getIndexOffsetInt(index)).dp,
        label = "",
        animationSpec = tween(ANIM_DURATION)
    )
    val cardWidthPx = with(LocalDensity.current) { cardWidth.toPx() }

    val stateAnchor = remember {
        AnchoredDraggableState(
            initialValue = DragAnchors.CENTER,
            positionalThreshold = { distance: Float -> distance * 0.3f },
            velocityThreshold = { cardWidthPx },
            animationSpec = tween(),
            anchors = DraggableAnchors {
                DragAnchors.CENTER at 0f
                DragAnchors.LEFT at -cardWidthPx - 100
            }
        )
    }

    Box(
        modifier = modifier
            .offset { offset.value }
            .height(height.value)
            .width(cardWidth)
            .addAnchorDraggableIfFirst(state = stateAnchor, index = index)
            .clip(RoundedCornerShape(24.dp)),
        contentAlignment = Alignment.Center
    ) {
        when (state) {
            is BlueCardState -> BlueCard(state)
            is BlackCardState -> BlackCard(state)
            is RedCardState -> RedCard(state)
            is PurpleCardState -> PurpleCard(state)
        }
    }

    LaunchedEffect(stateAnchor.currentValue) {
        if (stateAnchor.currentValue == DragAnchors.LEFT) {
            onEvent(CardsUiEvent.SwipedLeft)
            stateAnchor.snapTo(DragAnchors.CENTER)
        }
    }
}
```
Считаются по формуле: 
```
fun getIndexOffsetInt(index: Int): Int {
    return when (index) {
        0 -> 0
        1 -> FIRST_OFFSET
        2 -> SECOND_OFFSET
        else -> throw RuntimeException()
    }
}

fun Density.getIndexOffset(index: Int): Int {
    return getIndexOffsetInt(index).dp.toPx().roundToInt()
}
```
val offset - анимированный оффсет в зависимости от индекса
val height - анимированная высота в зависимости от индекса
val stateAnchor - стейт для свайпов
При свайпе влево передается юай ивент, что произошел свайп и меняется стейт карточки (порядок карточек)
Свайп навешивается только на верхнюю карточку // TODO поправить, если дизайнер не окнет
```
@OptIn(ExperimentalFoundationApi::class)
fun Modifier.addAnchorDraggableIfFirst(
    state: AnchoredDraggableState<DragAnchors>,
    index: Int,
): Modifier {
    return if (index == 0) {
        this
            .offset {
                IntOffset(
                    x = state
                        .requireOffset()
                        .roundToInt(),
                    y = 0
                )
            }
            .anchoredDraggable(state, Orientation.Horizontal)
    } else {
        this
    }
}
```
В зависимости от стейта рисуется нужное содержимое карточки
```
when (state) {
            is BlueCardState -> BlueCard(state)
            is BlackCardState -> BlackCard(state)
            is RedCardState -> RedCard(state)
            is PurpleCardState -> PurpleCard(state)
        }
```
Сами контейнеры карточек рекомпоузятся довольно часто, так как все динамично анимировано (меняется высота, оффсет, меняется стейт anchor),
!Но!
Наполнение контейнеров рекомпоузятся только при появлении в конце стека.
Чекнуть можно по тегу package:mine RECOMPOSITION В каждой карточке есть сайд эффект с логами

Для снижения тревожности добавил скроллы в карточки

Имплементации продуктового содержимого карточек находится в пакете implementation



