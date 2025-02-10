package com.yuvarajcode.food_harbor.presentation.main.donation.form

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.yuvarajcode.food_harbor.utilities.Screens
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Calendar
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun DonateFormFoodDetails(
    navController: NavController,
    donationFormViewModel: DonationFormViewModel
) {
    var foodName by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var showDatePicker1 by remember {
        mutableStateOf(false)
    }
    var showDatePicker2 by remember {
        mutableStateOf(false)
    }
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    val datetime = LocalDateTime.now()
    val dateState1 = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return isSameOrAfterToday(utcTimeMillis)
            }
        })
    val pickupDate = dateState1.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""
    var dPickupDate by remember {
        mutableStateOf("mm/dd/yyyy")
    }
    val dateState2 = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return isSameOrAfterToday(utcTimeMillis)
            }
        })
    val expiryDate = dateState2.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""
    var dExpiryDate by remember {
        mutableStateOf("mm/dd/yyyy")
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Food details") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {
            StepsIndicator(
                screenHeight = screenHeight,
                currentStep = 1
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding((screenHeight * 0.02).dp)
            ) {
                Text(
                    text = "Food Details",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = (screenHeight * 0.02).dp),
                    fontWeight = FontWeight.Bold
                )

                // Food Item Name
                OutlinedTextField(
                    value = foodName,
                    onValueChange = { foodName = it },
                    label = { Text("Food Item Name") },
                    placeholder = { Text("E.g. Rice, Canned Goods, etc.") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Quantity and Category
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = quantity,
                        onValueChange = { quantity = it },
                        label = { Text("Quantity") },
                        modifier = Modifier.weight(1f),
                        trailingIcon = {
                            Text("Kg")
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    )
                }

                // Date and Condition
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = dPickupDate,
                        onValueChange = {},
                        label = { Text("Pickup Date") },
                        placeholder = { Text("mm/dd/yyyy") },
                        modifier = Modifier.weight(1f),
                        trailingIcon = {
                            IconButton(
                                onClick = { showDatePicker1 = true }
                            ) {
                                Icon(
                                    Icons.Default.DateRange,
                                    "Select date",
                                )
                            }
                        }
                    )
                    if (showDatePicker1) {
                        DatePickerDialog(
                            onDismissRequest = { showDatePicker1 = false },
                            confirmButton = {
                                Button(onClick = {
                                    dPickupDate = pickupDate
                                    showDatePicker1 = false
                                }
                                ) {
                                    Text(text = "OK")
                                }
                            },
                            dismissButton = {
                                Button(onClick = { showDatePicker1 = false }) {
                                    Text(text = "Cancel")
                                }
                            }
                        ) {
                            DatePicker(
                                state = dateState1,
                                title = { Text(text = "Pickup Date", fontSize = (screenHeight * 0.02).sp , modifier = Modifier.padding(8.dp))})
                        }
                    }
                    OutlinedTextField(
                        value = dExpiryDate,
                        onValueChange = {},
                        label = { Text("Expiry Date") },
                        placeholder = { Text("mm/dd/yyyy") },
                        modifier = Modifier.weight(1f),
                        trailingIcon = {
                            IconButton(
                                onClick = { showDatePicker2 = true }
                            ) {
                                Icon(
                                    Icons.Default.DateRange,
                                    "Select date",
                                )
                            }
                        }
                    )

                    if (showDatePicker2) {
                        DatePickerDialog(
                            onDismissRequest = { showDatePicker2 = false },
                            confirmButton = {
                                Button(onClick = {
                                    dExpiryDate = expiryDate
                                    showDatePicker2 = false
                                }
                                ) {
                                    Text(text = "OK")
                                }
                            },
                            dismissButton = {
                                Button(onClick = { showDatePicker2 = false }) {
                                    Text(text = "Cancel")
                                }
                            }
                        ) {
                            DatePicker(
                                state = dateState2,
                                title = { Text(text = "Expiry Date") })
                        }
                    }
                }


                // Description
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    placeholder = { Text("Describe the food items you're donating (e.g. packaging, storage requirements, etc.)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height((screenHeight * 0.17).dp),
                    maxLines = 5,
                    supportingText = {
                        Text(
                            text = "0/500 characters",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.sp,
                            modifier = Modifier.align(Alignment.End)
                        )
                    }
                )
                var images by remember {
                    mutableStateOf<List<Uri?>>(listOf())
                }

                val launcher =
                    rememberLauncherForActivityResult(contract = ActivityResultContracts.PickMultipleVisualMedia()) {
                        images = it
                    }

                val pagerState = rememberPagerState(pageCount = {
                    images.size
                })

                Text(
                    text = "Add Photos",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = (screenHeight * 0.02).dp, bottom = 8.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height((screenHeight * 0.17).dp)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (images.isEmpty()) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                Icons.Default.CameraAlt,
                                contentDescription = "Add photos",
                                modifier = Modifier.size((screenHeight * 0.0325).dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                "Add up to 4 photos",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 12.sp
                            )
                            Text(
                                "(PNG, JPG up to 5MB)",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 12.sp
                            )
                            TextButton(onClick = {
                                launcher.launch(
                                    PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageAndVideo)
                                )
                            }) {
                                Text("Upload Photos")
                            }
                        }
                    } else {
                        HorizontalPager(
                            state = pagerState
                        ) { page ->
                            AsyncImage(
                                model = images[page],
                                contentDescription = "Food photo",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }
                }
                if (images.isNotEmpty()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextButton(onClick = {
                            launcher.launch(
                                PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }) {
                            Text("Upload Photos")
                        }
                        TextButton(onClick = {
                            images = emptyList()
                        }) {
                            Text("Remove Photos")
                        }
                    }
                }

                Button(
                    onClick = {
                        navController.navigate(Screens.DonationFormPickup.route)
                        donationFormViewModel.addFoodDetailsToDonation(
                            foodName = foodName,
                            quantity = quantity,
                            description = description,
                            photos = images,
                            pickupDate = dPickupDate,
                            expiryDate = dExpiryDate
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = (screenHeight * 0.02).dp)
                        .height((screenHeight * 0.06).dp),
                    shape = RoundedCornerShape(8.dp),
                    enabled = foodName.isNotEmpty() && quantity.isNotEmpty() && description.isNotEmpty() && images.isNotEmpty() && dPickupDate != "mm/dd/yyyy" && dExpiryDate != "mm/dd/yyyy"
                ) {
                    Text("Next Step")
                }
            }
        }
    }
}


@SuppressLint("SimpleDateFormat")
private fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy")
    return formatter.format(Date(millis))
}

fun isSameOrAfterToday(utcTimeMillis: Long): Boolean {
    val calendar = Calendar.getInstance()
    // Get the current time in milliseconds and set to the start of the day
    val currentDayStart = calendar.apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis

    // Set the given date to the start of the day
    val givenDayStart = Calendar.getInstance().apply {
        timeInMillis = utcTimeMillis
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis

    return givenDayStart >= currentDayStart
}