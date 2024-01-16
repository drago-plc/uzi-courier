package com.lomolo.uzicourier.compose.navigation.graphs

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.lomolo.uzicourier.MainViewModel
import com.lomolo.uzicourier.compose.TopBar
import com.lomolo.uzicourier.compose.navigation.Navigation
import com.lomolo.uzicourier.compose.signin.Name
import com.lomolo.uzicourier.compose.signin.Phone
import com.lomolo.uzicourier.compose.signin.SessionViewModel
import com.lomolo.uzicourier.compose.signin.UserNameDestination
import com.lomolo.uzicourier.compose.signin.UserPhoneDestination

object UserGraphDestination: Navigation {
    override val route = "graph/user"
    override val title = null
}

fun NavGraphBuilder.user(
    navController: NavHostController,
    sessionViewModel: SessionViewModel,
    mainViewModel: MainViewModel
) {
    navigation(
        startDestination = UserPhoneDestination.route,
        route = UserGraphDestination.route
    ) {
        composable(route = UserNameDestination.route) {
            NameScreen(
                navController = navController,
                sessionViewModel = sessionViewModel
            )
        }
        composable(route = UserPhoneDestination.route) {
            PhoneScreen(
                navController = navController,
                sessionViewModel = sessionViewModel,
                mainViewModel = mainViewModel
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NameScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    sessionViewModel: SessionViewModel
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        topBar = {
            TopBar(
                title = UserNameDestination.title,
                scrollBehavior = scrollBehavior,
            )
        }
    ) {innerPadding ->
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Name(
                sessionViewModel = sessionViewModel,
                onNavigateTo = {
                    navController.navigate(it) {
                        popUpTo(it) {
                            saveState = true
                        }
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    sessionViewModel: SessionViewModel,
    mainViewModel: MainViewModel
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        topBar = {
            TopBar(
                title = UserPhoneDestination.title,
                scrollBehavior = scrollBehavior,
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Phone(
                sessionViewModel = sessionViewModel,
                mainViewModel = mainViewModel,
                onNavigateTo = {
                    navController.navigate(it) {
                        popUpTo(it) {
                            saveState = true
                        }
                    }
                }
            )
        }
    }
}