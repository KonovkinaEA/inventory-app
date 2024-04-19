package com.example.inventoryapp.ui.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

object Start : Destination {

    override val route = "start"
}

object Identification : Destination {
    const val id = "id"
    override val route = "identification"

    const val routeWithArgs = "identification/{id}"

    val arguments = listOf(
        navArgument(id) {
            type = NavType.StringType
        }
    )

    fun navToOrderWithArgs(id: String = "") = "${route}/$id"
}

object ItemsList : Destination {
    const val id = "id"
    override val route = "list"

    const val routeWithArgs = "list/{id}"

    val arguments = listOf(
        navArgument(id) {
            type = NavType.StringType
        }
    )

    fun navToOrderWithArgs(id: String = "") = "$route/$id"
}
