package com.example.foodinventory.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.foodinventory.data.FoodItem
import com.example.foodinventory.data.daysUntilExpiration
import com.example.foodinventory.ui.common.NavigationTab
import com.example.foodinventory.viewmodel.FoodInventoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllItemsScreen(
    navController: NavController,
    viewModel: FoodInventoryViewModel
) {
    val foodItems by viewModel.foodItems.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Containers") },
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
                        selected = true,
                        onClick = { }
                    )
                    NavigationTab(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Filled.Settings,
                        label = "Settings",
                        selected = false,
                        onClick = { 
                            navController.navigate("settings") {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }
        },
    ) {
        if (foodItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "No items yet",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(foodItems) { item ->
                    val expirationColor = getExpirationColor(item)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                if (expirationColor == Color.Transparent) MaterialTheme.colorScheme.surfaceVariant else expirationColor.copy(alpha = 0.2f),
                                MaterialTheme.shapes.medium
                            )
                            .clickable { navController.navigate("food_details/${item.id}") }
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (item.imagePath.isNullOrEmpty()) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.small),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Restaurant,
                                    contentDescription = "No image",
                                    modifier = Modifier.size(24.dp),
                                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                                )
                            }
                        } else {
                            AsyncImage(
                                model = item.imagePath,
                                contentDescription = item.name,
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(MaterialTheme.shapes.small)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                item.name,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                "${item.foodType.displayName} • ${item.quantity}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                "Expires: ${item.expirationDate}",
                                style = MaterialTheme.typography.bodySmall,
                                color = if(expirationColor == Color.Transparent) Color.White else expirationColor
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun getExpirationColor(item: FoodItem): Color {
    val daysUntilExpiration = item.daysUntilExpiration()
    return when {
        daysUntilExpiration < 2 -> Color(0xFFE57373) // Red
        daysUntilExpiration < 10 -> Color(0xFFFFB74D) // Amber
        else -> Color.Transparent
    }
}
