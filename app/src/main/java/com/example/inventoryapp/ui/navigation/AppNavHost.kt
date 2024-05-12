package com.example.inventoryapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.inventoryapp.ui.screens.identification.IdentificationScreen
import com.example.inventoryapp.ui.screens.inventory.InventoryScreen
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
                },
                toInventory = {
                    if (it.isNotEmpty()) {
                        navController.navigate(Inventory.navToOrderWithArgs(it))
                    } else {
                        navController.navigate(Inventory.route)
                    }
                }
            )
        }
        composable(Identification.route) {
            IdentificationScreen(closeScreen = { returnToPrevScreen(navController) })
        }
        composable(Identification.routeWithArgs, arguments = Identification.arguments) {
            IdentificationScreen(closeScreen = { returnToPrevScreen(navController) })
        }
        composable(ItemsList.route) { NavListScreen(navController, it) }
        composable(ItemsList.routeWithArgs, arguments = ItemsList.arguments) {
            NavListScreen(navController, it)
        }
        composable(Inventory.route) {
            InventoryScreen(closeScreen = { returnToPrevScreen(navController) })
        }
        composable(Inventory.routeWithArgs, arguments = Inventory.arguments) {
            InventoryScreen(closeScreen = { returnToPrevScreen(navController) })
        }
    }
}

@Composable
private fun NavListScreen(navController: NavHostController, entry: NavBackStackEntry) {
    val reload = entry.savedStateHandle.get<Boolean>("reload") ?: false
    ListScreen(
        reload = reload,
        closeScreen = { returnToPrevScreen(navController) },
        openItem = { navController.navigate(Identification.navToOrderWithArgs(it)) })
}

private fun returnToPrevScreen(navController: NavHostController) {
    navController.previousBackStackEntry
        ?.savedStateHandle
        ?.set("reload", true)
    navController.popBackStack()
}
