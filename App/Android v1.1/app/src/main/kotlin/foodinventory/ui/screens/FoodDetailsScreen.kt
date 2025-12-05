package com.example.foodinventory.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.foodinventory.data.FoodItem
import com.example.foodinventory.data.daysUntilExpiration
import com.example.foodinventory.viewmodel.FoodInventoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodDetailsScreen(
    navController: NavController,
    viewModel: FoodInventoryViewModel,
    itemId: String?,
) {
    val foodItems by viewModel.foodItems.collectAsState()
    val item = foodItems.find { it.id == itemId }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Food Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
    ) {
        if (item != null) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                if (item.imagePath.isNullOrEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        contentAlignment = androidx.compose.ui.Alignment.Center
                    ) {
                        Icon(
                            Icons.Filled.Restaurant,
                            contentDescription = "No Image",
                            modifier = Modifier.size(100.dp),
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                        )
                    }
                } else {
                    AsyncImage(
                        model = item.imagePath,
                        contentDescription = item.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Expires in ${item.daysUntilExpiration()} days",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Food Type", style = MaterialTheme.typography.labelMedium)
                        Text(item.foodType.displayName, style = MaterialTheme.typography.bodyLarge)
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Container", style = MaterialTheme.typography.labelMedium)
                        Text(item.container, style = MaterialTheme.typography.bodyLarge)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Quantity", style = MaterialTheme.typography.labelMedium)
                        Text(item.quantity, style = MaterialTheme.typography.bodyLarge)
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Expires On", style = MaterialTheme.typography.labelMedium)
                        Text(item.expirationDate.toString(), style = MaterialTheme.typography.bodyLarge)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                 Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Added On", style = MaterialTheme.typography.labelMedium)
                        Text(item.creationDate.toString(), style = MaterialTheme.typography.bodyLarge)
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = { navController.navigate("edit_item/${item.id}") },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Edit Item")
                }
                OutlinedButton(
                    onClick = { 
                        viewModel.deleteFoodItem(item)
                        navController.popBackStack()
                     },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Delete Item")
                }
            }
        }
    }
}
