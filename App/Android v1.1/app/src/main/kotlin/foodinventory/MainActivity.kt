package com.example.foodinventory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.foodinventory.data.FoodInventoryDatabase
import com.example.foodinventory.data.FoodInventoryRepository
import com.example.foodinventory.data.MIGRATION_1_2
import com.example.foodinventory.ui.screens.*
import com.example.foodinventory.ui.theme.XTheme
import com.example.foodinventory.viewmodel.FoodInventoryViewModel
import com.example.foodinventory.viewmodel.FoodInventoryViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            XTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    val navController = rememberNavController()
                    val db = Room.databaseBuilder(
                        applicationContext,
                        FoodInventoryDatabase::class.java, "food-inventory-database"
                    ).addMigrations(MIGRATION_1_2).build()
                    val repository = FoodInventoryRepository(db.foodItemDao())
                    val viewModel: FoodInventoryViewModel = ViewModelProvider(this, FoodInventoryViewModelFactory(repository)).get(FoodInventoryViewModel::class.java)
                    NavHost(
                        navController = navController,
                        startDestination = "overview",
                    ) {
                        composable("overview") {
                            OverviewScreen(navController, viewModel)
                        }
                        composable("all_items") {
                            AllItemsScreen(navController, viewModel)
                        }
                        composable("settings") {
                            SettingsScreen(navController)
                        }
                        composable("add_item") {
                            AddItemScreen(navController, viewModel)
                        }
                        composable("food_details/{itemId}") { backStackEntry ->
                            FoodDetailsScreen(
                                navController = navController,
                                viewModel = viewModel,
                                itemId = backStackEntry.arguments?.getString("itemId")
                            )
                        }
                        composable("edit_item/{itemId}") { backStackEntry ->
                            AddItemScreen(
                                navController = navController,
                                viewModel = viewModel,
                                itemId = backStackEntry.arguments?.getString("itemId")
                            )
                        }
                        composable("notifications") {
                            NotificationsScreen(navController)
                        }
                    }
                }
            }
        }
    }
}
