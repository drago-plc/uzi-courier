package com.lomolo.uzicourier

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.lomolo.uzicourier.compose.navigation.UziCourierNavHost
import com.lomolo.uzicourier.ui.theme.UziCourierTheme

@Composable
fun UziCourierApplication(navController: NavHostController) {
    UziCourierNavHost(navController = navController)
}

@Preview(showBackground = true)
@Composable
fun UziCourierApplicationPreview() {
    UziCourierTheme {
        UziCourierApplication(rememberNavController())
    }
}