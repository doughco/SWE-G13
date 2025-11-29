package com.example.foodinventory.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.foodinventory.data.FoodInventoryRepository

class FoodInventoryViewModelFactory(private val repository: FoodInventoryRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FoodInventoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FoodInventoryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
