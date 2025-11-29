# Food Inventory Android App

A modern Android application built with Kotlin and Jetpack Compose for managing food inventory with expiration tracking and photo capture.

## Features

- **Overview Screen**: Dashboard showing expiring items and inventory summary
- **All Items List**: View all food items in your inventory
- **Add/Edit Item Screen**: Create and edit food items with:
  - Item name and food type
  - Quantity and container information
  - Expiration date with calendar picker
  - Photo capture with native camera
- **Food Details Screen**: View detailed information about a food item
- **Settings Screen**: App configuration and preferences
- **Smart Expiring Detection**: Items expiring within 2 days appear in "Expiring Soon" section
- **Multiple Themes**: Choose between a stark, black-and-white "X-style" theme and a vibrant, professional "LG-style" theme.
- **Modern Theming**: The app uses Material Design 3 for a modern look and feel.
- **Data Persistence**: The app uses Room to store all food items, so your data is saved even when you close the app.

## Technology Stack

- **Kotlin**: Programming language
- **Jetpack Compose**: Modern UI framework
- **Material Design 3**: Design system
- **Room**: Local database for persistent storage
- **CameraX**: Camera integration
- **Coil**: Image loading library
- **Navigation Compose**: In-app navigation

## Project Structure

```
android/
├── app/
│   ├── src/main/
│   │   ├── kotlin/com/example/foodinventory/
│   │   │   ├── MainActivity.kt
│   │   │   ├── data/
│   │   │   │   ├── FoodItem.kt
│   │   │   │   ├── FoodInventoryDatabase.kt
│   │   │   │   ├── FoodItemDao.kt
│   │   │   │   └── Converters.kt
│   │   │   ├── viewmodel/
│   │   │   │   ├── FoodInventoryViewModel.kt
│   │   │   │   └── FoodInventoryViewModelFactory.kt
│   │   │   └── ui/
│   │   │       ├── theme/
│   │   │       ├── screens/
│   │   │       │   ├── OverviewScreen.kt
│   │   │       │   ├── AllItemsScreen.kt
│   │   │       │   ├── SettingsScreen.kt
│   │   │       │   ├── AddItemScreen.kt
│   │   │       │   └── FoodDetailsScreen.kt
│   │   │       └── common/
│   │   │           └── NavigationTab.kt
│   │   └── res/
│   │       ├── values/
│   │       └── xml/
│   └── build.gradle.kts
├── build.gradle.kts
└── settings.gradle.kts
```

## Customization

### Changing Theme Colors

Edit `ui/theme/Theme.kt`:
```kotlin
private val XLightColorScheme = lightColorScheme(
    primary = Color.Black,
    onPrimary = Color.White,
    // ... other colors
)

private val LGLightColorScheme = lightColorScheme(
    primary = Color(0xFFC62828), // LG Red
    onPrimary = Color.White,
    // ... other colors
)
```

### Adding New Food Types

Edit `data/FoodItem.kt`:
```kotlin
enum class FoodType(val displayName: String) {
    PRODUCE("Produce"),
    DAIRY("Dairy"),
    MEATS("Meats"),
    PANTRY("Pantry"),
    // Add new types here
}
```

### Modifying Expiration Threshold

Edit `data/FoodItem.kt`:
```kotlin
fun FoodItem.isExpiringSoon(): Boolean {
    return daysUntilExpiration() < 2  // Change 2 to desired number of days
}
```

## Future Enhancements

- [ ] Cloud sync (Firebase Firestore or AWS)
- [ ] Barcode scanning for quick item addition
- [ ] Push notifications for expiring items
- [ ] Multi-user support
- [ ] Item search and filtering
- [ ] Export/import functionality

## License

This project is provided as-is for educational and development purposes.
