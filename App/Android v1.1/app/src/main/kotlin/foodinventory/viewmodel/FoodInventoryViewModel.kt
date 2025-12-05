package com.example.foodinventory.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodinventory.data.FoodInventoryRepository
import com.example.foodinventory.data.FoodItem
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FoodInventoryViewModel(private val repository: FoodInventoryRepository) : ViewModel() {

    val foodItems: StateFlow<List<FoodItem>> = repository.getAllFoodItems().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )

    fun addFoodItem(foodItem: FoodItem) {
        viewModelScope.launch {
            repository.insertFoodItem(foodItem)
        }
    }

    fun updateFoodItem(foodItem: FoodItem) {
        viewModelScope.launch {
            repository.updateFoodItem(foodItem)
        }
    }

    fun deleteFoodItem(foodItem: FoodItem) {
        viewModelScope.launch {
            repository.deleteFoodItem(foodItem)
        }
    }
}
