package com.example.inventoryapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.inventoryapp.data.navigation.Scanner
import com.example.inventoryapp.ui.screens.ScannerScreen

@Composable
fun AppNavHost(
    modifier: Modifier,
    navController: NavHostController
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Scanner.route
    ) {
        composable(Scanner.route) {
            ScannerScreen()
        }
    }
}
