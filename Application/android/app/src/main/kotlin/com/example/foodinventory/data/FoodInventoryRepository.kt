package com.example.foodinventory.data

import kotlinx.coroutines.flow.Flow

class FoodInventoryRepository(private val foodItemDao: FoodItemDao) {

    fun getAllFoodItems(): Flow<List<FoodItem>> = foodItemDao.getAll()

    suspend fun insertFoodItem(item: FoodItem) {
        foodItemDao.insert(item)
    }

    suspend fun updateFoodItem(item: FoodItem) {
        foodItemDao.update(item)
    }

    suspend fun deleteFoodItem(item: FoodItem) {
        foodItemDao.delete(item)
    }
}
