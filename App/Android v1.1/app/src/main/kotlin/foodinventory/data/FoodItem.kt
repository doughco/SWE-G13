package com.example.foodinventory.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "food_items")
data class FoodItem(
    @PrimaryKey
    val id: String,
    val name: String,
    val foodType: FoodType,
    val quantity: String,
    val container: String,
    val creationDate: LocalDate,
    val expirationDate: LocalDate,
    val imageUrl: String? = null,
    val imagePath: String? = null
)

enum class FoodType(val displayName: String) {
    PRODUCE("Produce"),
    DAIRY("Dairy"),
    MEATS("Meats"),
    PANTRY("Pantry"),
    OTHER("Other")
}

fun FoodItem.daysUntilExpiration(): Int {
    return LocalDate.now().until(expirationDate).days
}

fun FoodItem.isExpiringSoon(): Boolean {
    return daysUntilExpiration() < 2 && daysUntilExpiration() >= 0
}
