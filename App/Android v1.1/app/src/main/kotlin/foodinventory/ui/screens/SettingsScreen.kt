package com.example.foodinventory.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.foodinventory.ui.common.NavigationTab

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Settings") },
            )
        },
        bottomBar = {
            BottomAppBar {
                Row(modifier = Modifier.fillMaxWidth()) {
                    NavigationTab(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Filled.Home,
                        label = "Overview",
                        selected = false,
                        onClick = { 
                            navController.navigate("overview") {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                    )
                    NavigationTab(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Filled.List,
                        label = "All Items",
                        selected = false,
                        onClick = { 
                            navController.navigate("all_items") {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                    )
                    NavigationTab(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Filled.Settings,
                        label = "Settings",
                        selected = true,
                        onClick = { }
                    )
                }
            }
        },
    ) {
        Box(modifier = Modifier.padding(it)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SettingItem(title = "Notifications", subtitle = "Manage notification preferences", onClick = { navController.navigate("notifications") })
                SettingItem(title = "Theme", subtitle = "Dark Mode (Currently Enabled)")
                SettingItem(title = "About", subtitle = "App version 1.0")
            }
        }
    }
}

@Composable
private fun SettingItem(title: String, subtitle: String, onClick: (() -> Unit)? = null) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.shapes.medium)
            .clickable(enabled = onClick != null) { onClick?.invoke() }
            .padding(16.dp)
    ) {
        Text(
            title,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
