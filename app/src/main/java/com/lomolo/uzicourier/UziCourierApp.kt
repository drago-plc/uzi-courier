package com.lomolo.uzicourier

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.lomolo.uzicourier.compose.navigation.UziNavHost
import com.lomolo.uzicourier.ui.theme.UziTheme

@Composable
fun UziCourierApplication() {
    UziNavHost(navController = rememberNavController())
}

@Preview(showBackground = true)
@Composable
fun UziCourierApplicationPreview() {
    UziTheme {
        UziCourierApplication()
    }
}