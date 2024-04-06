package com.example.inventoryapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.inventoryapp.ui.screens.identification.IdentificationScreen
import com.example.inventoryapp.ui.screens.start.StartScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        modifier = Modifier,
        navController = navController,
        startDestination = Start.route
    ) {
        composable(Start.route) {
            StartScreen(toIdentification = { navController.navigate(Identification.route) })
        }
        composable(Identification.route) {
            IdentificationScreen(onClose = {
                navController.previousBackStackEntry
                navController.popBackStack()
            })
        }
    }
}
