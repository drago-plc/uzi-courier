package com.lomolo.uzicourier.compose.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lomolo.uzicourier.compose.signin.GetStarted

@Composable
internal fun NotAuthedScreen(
    modifier: Modifier = Modifier,
    onGetStartedClick: () -> Unit
) {
    Box(
        modifier = modifier
            .padding(bottom = 28.dp, start = 8.dp, end = 8.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        GetStarted(
            onGetStartedClick = onGetStartedClick
        )
    }
}

