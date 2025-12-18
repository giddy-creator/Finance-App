package com.example.financeapp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation(
    isDarkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "register") {
        composable("register") {
            RegistrationScreen(
                onLoginSuccess = {
                    navController.navigate("main") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            )
        }

        composable("main") {
            BottomNavScreen(isDarkMode = isDarkMode,
                onDarkModeChange = onDarkModeChange)
        }

        composable("settings") {
            SettingsScreen(
                modifier = Modifier.fillMaxSize(),
                isDarkMode = isDarkMode,
                onDarkModeChange = onDarkModeChange
            )
        }
    }
}
