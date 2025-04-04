package com.example.cards.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun OverlappingThreeViewsLayout(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        val itemCount = measurables.size
        if (itemCount != 3) throw IllegalArgumentException("Exactly 3 children required")

        val totalWidth = constraints.maxWidth
        val totalHeight = constraints.maxHeight

        // Вычисляем ширину каждого элемента, перекрывая часть соседнего
        val itemWidth = (totalWidth * 0.7f).toInt() // 70% от общей ширины
        val overlap = (totalWidth * 0.2f).toInt() // 20% перекрытие

        val placeables = measurables.map { measurable ->
            measurable.measure(constraints.copy(minWidth = 0, maxWidth = itemWidth))
        }

        layout(totalWidth, totalHeight) {
            placeables.forEachIndexed { index, placeable ->
                val xPosition = index * (itemWidth - overlap) // Смещение с перекрытием
                placeable.placeRelative(x = xPosition, y = 0)
            }
        }
    }
}


@Composable
@Preview
fun Test() {
    OverlappingThreeViewsLayout(Modifier.fillMaxWidth().height(100.dp)) {
        Box(Modifier.fillMaxWidth().background(Color.Red))
        Box(Modifier.fillMaxWidth().background(Color.Green))
        Box(Modifier.fillMaxWidth().background(Color.Blue))
    }
}
