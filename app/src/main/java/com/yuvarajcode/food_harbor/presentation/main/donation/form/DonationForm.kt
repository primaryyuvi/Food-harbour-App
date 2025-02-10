package com.yuvarajcode.food_harbor.presentation.main.donation.form

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.maryamrzdh.stepper.Stepper
import com.yuvarajcode.food_harbor.domain.model.DonationStatus
import com.yuvarajcode.food_harbor.utilities.ResponseState
import com.yuvarajcode.food_harbor.utilities.ToastForResponseState
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Calendar
import java.util.Date

//@RequiresApi(Build.VERSION_CODES.O)
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun DonationForm(
//    navController: NavController,
//    donationFormViewModel: DonationFormViewModel = hiltViewModel()
//) {
//    val dName = remember {
//        mutableStateOf("")
//    }
//    val dDescription = remember {
//        mutableStateOf("")
//    }
//    val dQuantity = remember {
//        mutableStateOf("")
//    }
//    var dImages by remember {
//        mutableStateOf<List<Uri?>>(listOf())
//    }
//    val datetime = LocalDateTime.now()
//    val dateState1 = rememberDatePickerState(
//        selectableDates = object : SelectableDates {
//            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
//                return utcTimeMillis <= System.currentTimeMillis()
//            }
//        })
//    val pickupDate =  dateState1.selectedDateMillis?.let {
//        convertMillisToDate(it)
//    } ?: ""
//    var dPickupDate by remember {
//        mutableStateOf("Open date picker dialog")
//    }
//    val timeState = rememberTimePickerState(
//        initialHour = datetime.hour,
//        initialMinute = datetime.minute,
//        is24Hour = false
//    )
//    val pTime = remember {
//        mutableStateOf("${timeState.hour}:${timeState.minute}")
//    }
//    val dateState2 = rememberDatePickerState(
//        selectableDates = object : SelectableDates {
//            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
//                return isSameOrAfterToday(utcTimeMillis)
//            }
//        })
//    val expiryDate =  dateState2.selectedDateMillis?.let {
//        convertMillisToDate(it)
//    } ?: ""
//    var dExpiryDate by remember {
//        mutableStateOf("Open date picker dialog")
//    }
//    val dLocation = remember {
//        mutableStateOf("")
//    }
//    val dContact = remember {
//        mutableStateOf("")
//    }
//    Scaffold(
//        topBar = {
//            DonationFormTopBar(
//               navController = navController,
//            )
//        }
//    ) {
//        Box (
//            modifier = Modifier
//                .padding(it)
//                .fillMaxSize()
//        ){
//            Column(
//                modifier = Modifier
//                    .padding(4.dp)
//                    .verticalScroll(rememberScrollState()),
//                verticalArrangement = Arrangement.spacedBy(8.dp),
//                horizontalAlignment = Alignment.Start
//            ) {
//                Column (
//                    modifier = Modifier.padding(4.dp)
//                ){
//                    DonationAttribute(title = "Name:" )
//                    OutlinedTextField(
//                        value = dName.value,
//                        onValueChange = {
//                            dName.value = it
//                        },
//                        label = { Text("Name") },
//                        singleLine = true,
//                        modifier = Modifier.fillMaxWidth()
//                    )
//                }
//                Column (
//                    modifier = Modifier.padding(4.dp)
//                ){
//                    DonationAttribute(title = "Description:" )
//                    OutlinedTextField(
//                        value = dDescription.value,
//                        onValueChange = {
//                            dDescription.value = it
//                        },
//                        label = { Text("Description") },
//                        maxLines = 4,
//                        modifier = Modifier.fillMaxWidth()
//                    )
//                }
//                Column (
//                    modifier = Modifier.padding(4.dp)
//                ){
//
//                    DonationAttribute(title = "Approximate quantity in Kgs:" )
//                    OutlinedTextField(
//                        value = dQuantity.value,
//                        onValueChange = {
//                            dQuantity.value = it
//                        },
//                        label = { Text("Quantity") },
//                        modifier = Modifier.fillMaxWidth(),
//                        keyboardOptions = KeyboardOptions(
//                            keyboardType = KeyboardType.Number,
//                            imeAction = ImeAction.Done
//                        ),
//                    )
//                }
//                Column {
//                    Row(
//                        modifier = Modifier.padding(4.dp),
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        DonationAttribute(
//                            title = "Upload the photos"
//                        )
//                        val launcher =
//                            rememberLauncherForActivityResult(contract = ActivityResultContracts.PickMultipleVisualMedia()) {
//                                dImages = it
//                            }
//                        IconButton(onClick = {
//                            launcher.launch(
//                                PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageAndVideo)
//                            )
//                        }) {
//                            Icon(
//                                imageVector = Icons.Default.Add,
//                                contentDescription = "Add Photo",
//                                modifier = Modifier.padding(8.dp)
//                            )
//                        }
//                    }
//                    LazyRow {
//                        items(dImages.size) { index ->
//                            dImages[index]?.let {
//                                AsyncImage(
//                                    model = it,
//                                    contentDescription = "Donation Image",
//                                    modifier = Modifier.size(70.dp),
//                                )
//                            }
//                        }
//                    }
//                }
//                Column {
//                    Row(
//                        modifier = Modifier.padding(4.dp),
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.spacedBy(8.dp)
//                    ) {
//                        var showDatePicker by remember {
//                            mutableStateOf(false)
//                        }
//                        DonationAttribute(title = "Pickup Date:")
//                        Button(onClick = { showDatePicker = true }) {
//                            Text(text = "Pickup Date")
//                        }
//                        if (showDatePicker) {
//                            DatePickerDialog(
//                                onDismissRequest = { showDatePicker = false },
//                                confirmButton = {
//                                    Button(onClick = {
//                                        dPickupDate = pickupDate
//                                        showDatePicker = false
//                                    }
//                                    ) {
//                                        Text(text = "OK")
//                                    }
//                                },
//                                dismissButton = {
//                                    Button(onClick = { showDatePicker = false }) {
//                                        Text(text = "Cancel")
//                                    }
//                                }
//                            ) {
//                                DatePicker(
//                                    state = dateState1,
//                                    title = { Text(text = "Pickup Date") })
//                            }
//                        }
//
//                    }
//                    Text(
//                        text = dPickupDate,
//                        modifier = Modifier.padding(start=8.dp),
//                        fontWeight = FontWeight.Bold,
//                        style = MaterialTheme.typography.titleMedium
//                    )
//                }
//                Column {
//                    Row(
//                        modifier = Modifier.padding(4.dp),
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.spacedBy(8.dp)
//                    ) {
//                        var showTimePicker by remember {
//                            mutableStateOf(false)
//                        }
//                        DonationAttribute(title = "Pickup Time:")
//                        Button(onClick = { showTimePicker = true }) {
//                            Text(text = "Pickup Time")
//                        }
//                        pTime.value = "${timeState.hour}:${timeState.minute}"
//                        if (showTimePicker) {
//                            Dialog(onDismissRequest = {
//                                showTimePicker = false
//                            }
//                            ) {
//                                Column(
//                                    modifier = Modifier.padding(8.dp),
//                                    horizontalAlignment = Alignment.End,
//                                    verticalArrangement = Arrangement.spacedBy(8.dp)
//                                ) {
//                                    TimePicker(
//                                        state = timeState
//                                    )
//                                    Row(
//                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
//                                        verticalAlignment = Alignment.CenterVertically
//                                    ) {
//                                        Button(
//                                            onClick = {
//                                                pTime.value =
//                                                    "${timeState.hour}:${timeState.minute}"
//                                                showTimePicker = false
//                                            },
//                                        ) {
//                                            Text(text = "OK")
//                                        }
//                                        Button(onClick = {
//                                            showTimePicker = false
//                                        }) {
//                                            Text(text = "Cancel")
//                                        }
//                                    }
//                                }
//                            }
//                        }
//
//                    }
//                    Text(
//                        text = pTime.value,
//                        modifier = Modifier.padding(start=8.dp),
//                        fontWeight = FontWeight.Bold,
//                        style = MaterialTheme.typography.titleMedium
//                    )
//                }
//                Column {
//                    Row(
//                        modifier = Modifier.padding(4.dp),
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.spacedBy(8.dp)
//                    ) {
//                        var showDatePicker by remember {
//                            mutableStateOf(false)
//                        }
//                        DonationAttribute(title = "Expiry Date:")
//                        Button(onClick = { showDatePicker = true }) {
//                            Text(text = "Expiry Date")
//                        }
//                        if (showDatePicker) {
//                            DatePickerDialog(
//                                onDismissRequest = { showDatePicker = false },
//                                confirmButton = {
//                                    Button(onClick = {
//                                        dExpiryDate = expiryDate
//                                        showDatePicker = false
//                                    }
//                                    ) {
//                                        Text(text = "OK")
//                                    }
//                                },
//                                dismissButton = {
//                                    Button(onClick = { showDatePicker = false }) {
//                                        Text(text = "Cancel")
//                                    }
//                                }
//                            ) {
//                                DatePicker(
//                                    state = dateState2,
//                                    title = { Text(text = "Expiry Date") })
//                            }
//                        }
//                    }
//                    Text(
//                        text = dExpiryDate,
//                        modifier = Modifier.padding(start=8.dp),
//                        fontWeight = FontWeight.Bold,
//                        style = MaterialTheme.typography.titleMedium
//                    )
//                }
//                Column(
//                    modifier = Modifier.padding(8.dp)
//                ){
//                        DonationAttribute(title = "Location:" )
//                        OutlinedTextField(
//                            value = dLocation.value,
//                            onValueChange = {
//                                dLocation.value = it
//                            },
//                            label = { Text("Location") },
//                            maxLines = 4,
//                            modifier = Modifier.fillMaxWidth()
//                        )
//                }
//                Column(
//                    modifier = Modifier.padding(8.dp)
//                ){
//                    DonationAttribute(title = "Contact:" )
//                    OutlinedTextField(
//                        value = dContact.value,
//                        onValueChange = {
//                            dContact.value = it
//                        },
//                        label = { Text("Contact") },
//                        singleLine = true,
//                        modifier = Modifier.fillMaxWidth()
//                    )
//                }
//                DonationButton(
//                    navController = navController,
//                    donationFormViewModel,
//                    dName = dName.value,
//                    dDescription = dDescription.value,
//                    dQuantity = dQuantity.value,
//                    dImages = dImages,
//                    dLocation = dLocation.value,
//                    dContact = dContact.value,
//                    dPickupDate = dPickupDate,
//                    dExpiryDate = dExpiryDate,
//                    pTime = pTime.value,
//                    status = DonationStatus.PENDING.status
//                )
//            }
//        }
//    }
//}
//

//
//@Composable
//fun DonationButton(
//    navController: NavController,
//    donationViewModel: DonationFormViewModel,
//    dName: String,
//    dDescription: String,
//    dQuantity: String,
//    dImages: List<Uri?>,
//    dLocation: String,
//    dContact: String,
//    dPickupDate: String,
//    dExpiryDate: String,
//    pTime: String,
//    status: String
//) {
//    LaunchedEffect(Unit) {
//        donationViewModel.resetState()
//    }
//    val images : List<String> =  dImages.map { it.toString() }
//    val showButton = dName.isNotEmpty() && dDescription.isNotEmpty() && dQuantity.isNotEmpty() &&  dLocation.isNotEmpty() && dContact.isNotEmpty() && dPickupDate.isNotEmpty() && dExpiryDate.isNotEmpty() && pTime.isNotEmpty()
//    Button(
//        onClick = {
//           donationViewModel.addDonation(
//                dName,
//                dDescription,
//                dQuantity.toDouble(),
//                images,
//                dLocation,
//                dContact,
//                dPickupDate,
//                dExpiryDate,
//                pTime,
//                status
//            )
//        },
//        modifier = Modifier.padding(8.dp),
//        enabled = showButton
//    ) {
//        when(val response = donationViewModel.addData.value){
//            is ResponseState.Error -> {
//                ToastForResponseState(response.message)
//            }
//            ResponseState.Loading -> {
//                CircularProgressIndicator()
//            }
//            is ResponseState.Success -> {
//                if(response.data != null){
//                    navController.navigateUp()
//                }
//                else{
//                    Text(text = "Donate")
//                }
//            }
//
//            ResponseState.Initial -> {}
//        }
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun DonationFormTopBar(
//    navController: NavController
// ) {
//    TopAppBar(
//        title = {
//            Text(
//                text = "Donation Form",
//                modifier = Modifier.padding(8.dp),
//                fontWeight = FontWeight.Bold,
//            )
//        },
//        navigationIcon = {
//            IconButton(onClick = {
//               navController.popBackStack()
//            }) {
//                Icon(
//                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                    contentDescription = "Back",
//                    modifier = Modifier.padding(8.dp)
//                )
//            }
//        },
//        modifier = Modifier,
//        colors = TopAppBarDefaults.topAppBarColors(
//            containerColor = Color(red = 7, green = 31, blue = 27, alpha = 255),
//            scrolledContainerColor = Color(red = 7, green = 31, blue = 27, alpha = 255),
//            navigationIconContentColor = Color.White,
//            actionIconContentColor = Color.White,
//            titleContentColor = Color.White
//        )
//    )
//}
//
//@Composable
//fun DonationAttribute(
//    title : String
//){
//    Text(
//        text =title,
//        fontWeight = FontWeight.Bold,
//        modifier = Modifier.padding(4.dp),
//        style = MaterialTheme.typography.titleLarge
//    )
//}


//@RequiresApi(Build.VERSION_CODES.O)
//@Preview(showBackground = true)
//@Composable
//fun DonationFormPreview() {
//    DonationForm()
//}


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DonationFormScreen(
    navController: NavController,
    donationFormViewModel: DonationFormViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        DonateFormFoodDetails(
            donationFormViewModel = donationFormViewModel,
            navController = navController
        )
    }
}


enum class DonationStep(val value: String) {
    FOOD_DETAILS("Food Details"),
    PICKUP_LOCATION("Location"),
    CONTACT_INFO("Contact Info"),
    REVIEW("Review")
}

@Composable
fun StepsIndicator(
    screenHeight: Int,
    currentStep : Int,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = (screenHeight * 0.02).dp).padding(bottom = (screenHeight * 0.02).dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Stepper(
            numberOfSteps = 4,
            currentStep = currentStep,
            stepDescriptionList = listOf(
                DonationStep.FOOD_DETAILS.value,
                DonationStep.PICKUP_LOCATION.value,
                DonationStep.CONTACT_INFO.value,
                DonationStep.REVIEW.value
            ),
            selectedColor = Color(0xFF4CAF50)
        )
    }
}

//@Preview
//@Composable
//fun DonationFormScreenPreview() {
//    DonationFormScreen()
//}


