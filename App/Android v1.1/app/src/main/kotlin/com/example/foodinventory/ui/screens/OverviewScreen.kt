package com.example.foodinventory.ui.screens

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.foodinventory.R
import com.example.foodinventory.data.FoodItem
import com.example.foodinventory.data.FoodType
import com.example.foodinventory.data.daysUntilExpiration
import com.example.foodinventory.viewmodel.FoodInventoryViewModel
import com.example.foodinventory.ui.common.NavigationTab

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OverviewScreen(
    navController: NavController,
    viewModel: FoodInventoryViewModel
) {
    val foodItems by viewModel.foodItems.collectAsState()
    val expiringItems = foodItems.filter { it.daysUntilExpiration() < 2 }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Overview") },
                actions = {
                    IconButton(onClick = { navController.navigate("add_item") }) {
                        Icon(
                            Icons.Filled.Add,
                            contentDescription = "Add"
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar {
                Row(modifier = Modifier.fillMaxWidth()) {
                    NavigationTab(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Filled.Home,
                        label = "Overview",
                        selected = true,
                        onClick = { }
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
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            // Expiring Soon Section
            if (expiringItems.isNotEmpty()) {
                Text(
                    "Expiring Soon",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(top = 20.dp, bottom = 12.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    expiringItems.forEach { item ->
                        ExpiringItemCard(item, navController)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            // Inventory Summary Section
            Text(
                "Inventory Summary",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(24.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Total Items",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        foodItems.size.toString(),
                        style = MaterialTheme.typography.displayLarge,
                        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                    )
                    Text(
                        "This Month",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        FoodType.entries.forEach { type ->
                            InventoryBar(
                                label = type.displayName,
                                count = foodItems.count { it.foodType == type },
                                totalCount = foodItems.size
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun ExpiringItemCard(item: FoodItem, navController: NavController) {
    Card(
        modifier = Modifier
            .width(160.dp),
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(2.dp, Color.Black)
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickable { navController.navigate("food_details/${item.id}") }
                .padding(0.dp)
        ) {
            if (item.imagePath.isNullOrEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .background(MaterialTheme.colorScheme.surface),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Restaurant,
                        contentDescription = "No image",
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                    )
                }
            } else {
                AsyncImage(
                    model = item.imagePath,
                    contentDescription = item.name,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                )
            }

            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    item.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    "Expires in ${item.daysUntilExpiration()} days",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun InventoryBar(label: String, count: Int, totalCount: Int) {
    val height = if (totalCount > 0) {
        137.dp * (count.toFloat() / totalCount.toFloat())
    } else {
        0.dp
    }
    val animatedHeight by animateDpAsState(targetValue = height)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(animatedHeight)
                .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
