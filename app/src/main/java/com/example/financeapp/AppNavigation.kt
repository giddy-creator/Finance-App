package com.example.financeapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.example.financeapp.RegistrationScreen
import com.example.financeapp.MainActivityScreen
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "register") {

        // LOGIN SCREEN
        composable("register") {
            RegistrationScreen(
                onLoginSuccess = {
                    navController.navigate("main") {
                        popUpTo("register") { inclusive = true }   // remove login from backstack
                    }
                }
            )
        }

        // MAIN SCREEN (contains bottom navigation)
        composable("main") {
            MainActivityScreen()   // <--- bottom navigation is here
        }
    }
}
