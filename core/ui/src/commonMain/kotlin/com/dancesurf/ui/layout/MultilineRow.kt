package com.dancesurf.ui.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Horizontal
import androidx.compose.foundation.layout.Arrangement.Vertical
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowOverflow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * A horizontal row with one or more items placed in a line. If items count exceeds the a parent width
 * then the rest of the items will be move to a new line
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
inline fun <T> MultilineRow(
    items: List<T>,
    modifier: Modifier = Modifier,
    horizontalArrangement: Horizontal = Arrangement.Center,
    verticalArrangement: Vertical = Arrangement.Center,
    maxLines: Int = 1,
    crossinline overflowContent: @Composable () -> Unit = {},
    crossinline content: @Composable (T) -> Unit
) {
    FlowRow(
        modifier = modifier,
        maxLines = maxLines,
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement,
        overflow = FlowRowOverflow.expandIndicator { overflowContent() }
    ) {
        items.forEach { item ->
            content(item)
        }
    }
}
