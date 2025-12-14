package com.example.financeapp

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.navigationBarsPadding

@Composable
fun BottomNavScreen() {
    var selectedItem by remember { mutableStateOf("home") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFFFD5930),
                contentColor = Color.White,
                modifier = Modifier.navigationBarsPadding() // avoids system nav bar overlap
            ) {
                NavigationBarItem(
                    selected = selectedItem == "home",
                    onClick = { selectedItem = "home" },
                    label = { Text("Home", fontSize = 10.sp) },
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Home", modifier = Modifier.size(24.dp)) },
                    alwaysShowLabel = true
                )
                NavigationBarItem(
                    selected = selectedItem == "reports",
                    onClick = { selectedItem = "reports" },
                    label = { Text("Reports", fontSize = 10.sp) },
                    icon = { Icon(Icons.Filled.BarChart, contentDescription = "Reports", modifier = Modifier.size(24.dp)) },
                    alwaysShowLabel = true
                )
                NavigationBarItem(
                    selected = selectedItem == "budgets",
                    onClick = { selectedItem = "budgets" },
                    label = { Text("Budgets", fontSize = 10.sp) },
                    icon = { Icon(Icons.Filled.AccountBalance, contentDescription = "Budgets", modifier = Modifier.size(24.dp)) },
                    alwaysShowLabel = true
                )
                NavigationBarItem(
                    selected = selectedItem == "settings",
                    onClick = { selectedItem = "settings" },
                    label = { Text("Settings", fontSize = 10.sp) },
                    icon = { Icon(Icons.Filled.Settings, contentDescription = "Settings", modifier = Modifier.size(24.dp)) },
                    alwaysShowLabel = true
                )
            }
        }
    ) { innerPadding ->
        val contentModifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        when (selectedItem) {
            "home" -> Home(modifier = contentModifier)
            "reports" -> ReportsScreen(modifier = contentModifier)
            "budgets" -> BudgetsScreen(modifier = contentModifier)
            "settings" -> SettingsScreen(modifier = contentModifier)
        }
    }
}

