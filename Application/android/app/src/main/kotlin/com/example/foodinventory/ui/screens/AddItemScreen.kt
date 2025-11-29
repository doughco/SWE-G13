package com.example.foodinventory.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.foodinventory.data.FoodItem
import com.example.foodinventory.data.FoodType
import com.example.foodinventory.viewmodel.FoodInventoryViewModel
import java.io.File
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemScreen(
    navController: NavController,
    viewModel: FoodInventoryViewModel = viewModel(),
    itemId: String? = null
) {
    val context = LocalContext.current
    val foodItems by viewModel.foodItems.collectAsState()
    val item = foodItems.find { it.id == itemId }

    var itemName by remember { mutableStateOf(item?.name ?: "") }
    var quantity by remember { mutableStateOf(item?.quantity ?: "") }
    var container by remember { mutableStateOf(item?.container ?: "") }
    var selectedFoodType by remember { mutableStateOf(item?.foodType ?: FoodType.PRODUCE) }
    var showFoodTypeMenu by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(item?.expirationDate ?: LocalDate.now()) }
    var capturedImageUri by remember { mutableStateOf<Uri?>(null) }
    var cameraPermissionGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && capturedImageUri != null) {
            // viewModel.updateSelectedImage(capturedImageUri.toString()) // This function does not exist in the new ViewModel
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        cameraPermissionGranted = isGranted
        if (isGranted) {
            launchCamera(context, cameraLauncher) { uri ->
                capturedImageUri = uri
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (item == null) "Add Item" else "Edit Item") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Image Preview
            if (item?.imagePath != null || capturedImageUri != null) {
                AsyncImage(
                    model = capturedImageUri?.toString() ?: item?.imagePath,
                    contentDescription = "Captured photo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(16.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Form Fields
            OutlinedTextField(
                value = itemName,
                onValueChange = { itemName = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(16.dp))

            ExposedDropdownMenuBox(expanded = showFoodTypeMenu, onExpandedChange = { showFoodTypeMenu = !showFoodTypeMenu }) {
                OutlinedTextField(
                    value = selectedFoodType.displayName,
                    onValueChange = { },
                    label = { Text("Food Type") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showFoodTypeMenu) }
                )
                ExposedDropdownMenu(expanded = showFoodTypeMenu, onDismissRequest = { showFoodTypeMenu = false }) {
                    FoodType.entries.forEach { type ->
                        DropdownMenuItem(text = { Text(type.displayName) }, onClick = {
                            selectedFoodType = type
                            showFoodTypeMenu = false
                        })
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = quantity,
                onValueChange = { quantity = it },
                label = { Text("Quantity") },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = container,
                onValueChange = { container = it },
                label = { Text("Container Name") },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = selectedDate.format(DateTimeFormatter.ISO_LOCAL_DATE),
                onValueChange = { },
                label = { Text("Expiration Date") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Calendar
            SimpleCalendarPicker(selectedDate = selectedDate, onDateSelected = { date ->
                selectedDate = date
            })

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        if (!cameraPermissionGranted) {
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        } else {
                            launchCamera(context, cameraLauncher) { uri ->
                                capturedImageUri = uri
                            }
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                ) {
                    Text("Take Photo")
                }

                Button(
                    onClick = {
                        if (itemName.isNotEmpty()) {
                            val updatedItem = FoodItem(
                                id = item?.id ?: UUID.randomUUID().toString(),
                                name = itemName,
                                foodType = selectedFoodType,
                                quantity = quantity,
                                container = container,
                                creationDate = item?.creationDate ?: LocalDate.now(),
                                expirationDate = selectedDate,
                                imagePath = capturedImageUri?.toString() ?: item?.imagePath
                            )
                            if (item == null) {
                                viewModel.addFoodItem(updatedItem)
                            } else {
                                viewModel.updateFoodItem(updatedItem)
                            }
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                ) {
                    Text(if (item == null) "Add Item" else "Save Changes")
                }
            }
        }
    }
}

@Composable
private fun SimpleCalendarPicker(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Month Navigation
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { currentMonth = currentMonth.minusMonths(1) }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Previous Month")
            }

            Text(
                "${currentMonth.month} ${currentMonth.year}",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )

            IconButton(onClick = { currentMonth = currentMonth.plusMonths(1) }) {
                 Icon(Icons.Filled.ArrowForward, contentDescription = "Next Month")
            }
        }

        // Day labels
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("S", "M", "T", "W", "T", "F", "S").forEach { day ->
                Text(
                    day,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }

        // Calendar grid
        val firstDay = currentMonth.atDay(1)
        val dayOfWeek = firstDay.dayOfWeek.value % 7
        val daysInMonth = currentMonth.lengthOfMonth()

        var dayCounter = 1
        for (week in 0..5) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (day in 0..6) {
                    if (week == 0 && day < dayOfWeek || dayCounter > daysInMonth) {
                        Box(modifier = Modifier.weight(1f).height(48.dp))
                    } else {
                        val date = currentMonth.atDay(dayCounter)
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .clip(MaterialTheme.shapes.small)
                                .clickable { onDateSelected(date) },
                            color = if (date == selectedDate) Color.DarkGray else Color.Transparent,
                            contentColor = if (date == selectedDate) Color.White else Color.Black
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = dayCounter.toString(),
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                            }
                        }
                        dayCounter++
                    }
                }
            }
        }
    }
}

private fun launchCamera(
    context: android.content.Context,
    launcher: androidx.activity.result.ActivityResultLauncher<Uri>,
    onUriCreated: (Uri) -> Unit
) {
    val file = File(context.cacheDir, "camera_${System.currentTimeMillis()}.jpg")
    val uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file
    )
    onUriCreated(uri)
    launcher.launch(uri)
}
