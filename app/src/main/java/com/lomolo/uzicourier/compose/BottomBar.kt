package com.lomolo.uzicourier.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    BottomBar(
        modifier = modifier
    ) {
        content()
    }
}