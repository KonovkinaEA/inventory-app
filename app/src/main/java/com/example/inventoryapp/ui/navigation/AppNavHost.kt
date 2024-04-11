package com.example.inventoryapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.inventoryapp.ui.screens.identification.IdentificationScreen
import com.example.inventoryapp.ui.screens.list.ListScreen
import com.example.inventoryapp.ui.screens.start.StartScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        modifier = Modifier,
        navController = navController,
        startDestination = Start.route
    ) {
        composable(Start.route) {
            StartScreen(
                toIdentification = { navController.navigate(Identification.route) },
                toList = {
                    if (it.isNotEmpty()) {
                        navController.navigate(ItemsList.navToOrderWithArgs(it))
                    } else {
                        navController.navigate(ItemsList.route)
                    }
                }
            )
        }
        composable(Identification.route) {
            IdentificationScreen(closeScreen = { returnToPrevScreen(navController) })
        }
        composable(ItemsList.route) {
            ListScreen(closeScreen = { returnToPrevScreen(navController) })
        }
        composable(ItemsList.routeWithArgs, arguments = ItemsList.arguments) {
            ListScreen(closeScreen = { returnToPrevScreen(navController) })
        }
    }
}

private fun returnToPrevScreen(navController: NavHostController) {
    navController.previousBackStackEntry
    navController.popBackStack()
}
