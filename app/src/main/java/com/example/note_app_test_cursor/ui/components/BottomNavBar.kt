package com.example.note_app_test_cursor.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavBar(navController: NavController) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        NavigationBarItem(
            icon = { Icon(Icons.Default.List, contentDescription = "Tüm Notlar") },
            label = { Text("Tüm Notlar") },
            selected = currentRoute == "notes",
            onClick = {
                if (currentRoute != "notes") {
                    navController.navigate("notes") {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Favorite, contentDescription = "Favoriler") },
            label = { Text("Favoriler") },
            selected = currentRoute == "favorites",
            onClick = {
                if (currentRoute != "favorites") {
                    navController.navigate("favorites") {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            }
        )
    }
} 