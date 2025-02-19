package com.dancesurf.ui.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Horizontal
import androidx.compose.foundation.layout.Arrangement.Vertical
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.SelectableChipColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
inline fun <T> ChipsLayout(
    items: List<T>,
    modifier: Modifier = Modifier,
    maxLines: Int = 1,
    horizontalArrangement: Horizontal = Arrangement.Center,
    verticalArrangement: Vertical = Arrangement.Center,
    colors: SelectableChipColors = FilterChipDefaults.filterChipColors(),
    crossinline overflowContent: @Composable () -> Unit = {},
    crossinline content: @Composable (T) -> Unit,
    crossinline onSelected: (T, Boolean) -> Unit,
    noinline onOverflowClick: () -> Unit = {},
) {
    MultilineRow(
        modifier = modifier,
        items = items,
        maxLines = maxLines,
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement,
        overflowContent = {
            OverflowFilterChipItem(
                modifier = Modifier.wrapContentSize()
                    .padding(
                        horizontal = 4.dp,
                        vertical = 2.dp
                    ),
                colors = colors,
                content = { overflowContent() },
                onClick = onOverflowClick
            )
        },
        content = { item ->
            FilterChipItem(
                item = item,
                modifier = Modifier.wrapContentSize()
                    .padding(
                        horizontal = 4.dp,
                        vertical = 2.dp
                    ),
                colors = colors,
                content = content,
                onSelected = onSelected
            )
        }
    )
}

@PublishedApi
@Composable
internal inline fun <T> FilterChipItem(
    item: T,
    modifier: Modifier = Modifier,
    colors: SelectableChipColors,
    crossinline onSelected: (T, Boolean) -> Unit,
    crossinline content: @Composable (T) -> Unit,
) {
    var selected by remember { mutableStateOf(false) }

    FilterChip(
        modifier = modifier.padding(horizontal = 2.dp, vertical = 2.dp),
        selected = selected,
        colors = colors,
        onClick = {
            selected = !selected
            onSelected(item, selected)
        },
        label = { content(item) }
    )
}

@PublishedApi
@Composable
internal inline fun OverflowFilterChipItem(
    modifier: Modifier = Modifier,
    colors: SelectableChipColors,
    noinline onClick: () -> Unit,
    noinline content: @Composable () -> Unit,
) {
    FilterChip(
        modifier = modifier,
        colors = colors,
        selected = false,
        onClick = onClick,
        label = content
    )
}
