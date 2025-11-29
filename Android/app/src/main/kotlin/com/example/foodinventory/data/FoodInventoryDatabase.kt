package com.example.foodinventory.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [FoodItem::class], version = 2)
@TypeConverters(Converters::class)
abstract class FoodInventoryDatabase : RoomDatabase() {
    abstract fun foodItemDao(): FoodItemDao
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE food_items ADD COLUMN creationDate INTEGER NOT NULL DEFAULT 0")
    }
}
