package com.example.inventoryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.inventoryapp.ui.navigation.AppNavHost
import com.example.inventoryapp.ui.theme.ExtendedTheme
import com.example.inventoryapp.ui.theme.InventoryAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InventoryAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = ExtendedTheme.colors.backPrimary
                ) {
                    AppNavHost(navController = rememberNavController())
                }
            }
        }
    }
}
