package com.example.inventoryapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.inventoryapp.ui.screens.scanner.ScannerScreen
import com.example.inventoryapp.ui.screens.start.StartScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        modifier = Modifier,
        navController = navController,
        startDestination = Start.route
    ) {
        composable(Start.route) {
            StartScreen(toDetailsScreen = { navController.navigate(Scanner.route) })
        }
        composable(Scanner.route) {
            ScannerScreen()
        }
    }
}
