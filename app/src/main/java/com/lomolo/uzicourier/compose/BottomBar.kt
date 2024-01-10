package com.lomolo.uzicourier.compose

import androidx.compose.material3.BottomAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    BottomAppBar(
        modifier = modifier
    ) {
        content()
    }
}