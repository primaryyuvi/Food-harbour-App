package com.yuvarajcode.food_harbor.presentation.profile.edit

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.AlternateEmail
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.yuvarajcode.food_harbor.R
import com.yuvarajcode.food_harbor.presentation.profile.ProfileViewmodel
import com.yuvarajcode.food_harbor.utilities.ResponseState
import com.yuvarajcode.food_harbor.utilities.ToastForResponseState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditUserScreen(
    navController: NavController,
    profileViewmodel: ProfileViewmodel
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    var showDonations by remember { mutableStateOf(true) }
    var nameEdit = remember {
        mutableStateOf(profileViewmodel.realObj.name)
    }
    var userNameEdit = remember {
        mutableStateOf(profileViewmodel.realObj.userName)
    }
    var emailEdit = remember {
        mutableStateOf(profileViewmodel.realObj.email)
    }
    var passwordEdit = remember {
        mutableStateOf(profileViewmodel.realObj.password)
    }
    var phoneNumberEdit = remember {
        mutableStateOf(profileViewmodel.realObj.phoneNumber)
    }
    var imageUri  = remember {
        mutableStateOf<Uri>(Uri.parse(profileViewmodel.realObj.profilePictureUrl))
    }

    val launcher  = rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) {
        if (it != null) {
            imageUri.value = it
        }
    }
    val missionStatement = remember {
        mutableStateOf(profileViewmodel.realObj.missionStatement)
    }
    var monthlyGoalState by remember { mutableStateOf(profileViewmodel.realObj.monthlyGoal.toString()) }
    var weeklyGoalState by remember { mutableStateOf(profileViewmodel.realObj.weeklyGoal.toString()) }
    var yearlyGoalState by remember { mutableStateOf(profileViewmodel.realObj.yearlyGoal.toString()) }
    var changedPassword by remember { mutableStateOf("") }


    val setUserDetails by profileViewmodel.setData


    when(val response = setUserDetails){
        is ResponseState.Error -> {
            ToastForResponseState(response.message)
        }
        ResponseState.Initial -> {

        }
        ResponseState.Loading -> {
            Dialog(
                onDismissRequest = {

                }
            ) {
                CircularProgressIndicator()
            }
        }
        is ResponseState.Success -> {
            ToastForResponseState("Profile Updated Successfully")
            profileViewmodel.resetSetData()
            navController.navigateUp()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Profile") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigateUp()
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(onClick = {
                        profileViewmodel.setUserDetails(
                            username = userNameEdit.value,
                            email = emailEdit.value,
                            password = passwordEdit.value,
                            profilePicture = imageUri.value.toString(),
                            name = profileViewmodel.realObj.name,
                            missionStatement = missionStatement.value,
                            weeklyGoal = weeklyGoalState.toInt(),
                            monthlyGoal = monthlyGoalState.toInt(),
                            yearlyGoal = yearlyGoalState.toInt(),
                            phone = phoneNumberEdit.value
                        )

                    },
                        enabled = userNameEdit.value.isNotEmpty() && emailEdit.value.isNotEmpty() && phoneNumberEdit.value.isNotEmpty()  && passwordEdit.value.isNotEmpty()  && nameEdit.value.isNotEmpty()
                    ) {
                        Text("Save", color = Color(0xFF4CAF50))
                    }
                }
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding((screenHeight * 0.02).dp)
                .padding(it)
        ) {
            // Profile Photo
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = (screenHeight * 0.02).dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    AsyncImage(
                        model = imageUri.value,
                        contentDescription = "Profile Photo",
                        modifier = Modifier
                            .size((screenHeight * 0.09).dp)
                            .clickable {
                                launcher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            }
                            .border(1.dp, Color(0xFF4CAF50), CircleShape)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        "Change Photo",
                        color = Color(0xFF4CAF50),
                        modifier = Modifier.padding(top = (screenHeight * 0.02).dp)
                    )
                }
            }

            // Basic Details
            item {
                Text(
                    "Basic Details",
                    fontSize = (screenHeight * 0.0225).sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = (screenHeight * 0.02).dp)
                )
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy((screenHeight * 0.02).dp)
                ) {
                    EditOutfieldTextField(
                        value = userNameEdit.value,
                        onValueChange = {
                            userNameEdit.value = it
                        },
                        leadingIcon = Icons.Outlined.AlternateEmail
                    )
                    EditOutfieldTextField(
                        value = nameEdit.value,
                        onValueChange = {
                            nameEdit.value = it
                        },
                        leadingIcon = Icons.Outlined.Person
                    )
                    EditOutfieldTextField(
                        value = emailEdit.value,
                        onValueChange = {
                            emailEdit.value = it
                        },
                        leadingIcon = Icons.Outlined.Email
                    )
                    EditOutfieldTextField(
                        value = phoneNumberEdit.value,
                        onValueChange = {
                            phoneNumberEdit.value = it
                        },
                        leadingIcon = Icons.Outlined.Phone
                    )
                }
            }


            // Impact Goals
            item {
                Text(
                    "Impact Goals",
                    fontSize = (screenHeight * 0.0225).sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = (screenHeight * 0.02).dp)
                )
                Text(
                    text = "Weekly donation goal",
                    fontSize = (screenHeight * 0.015).sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = weeklyGoalState,
                        onValueChange = {
                            weeklyGoalState = it
                        },
                        modifier = Modifier
                            .height((screenHeight * 0.055).dp)
                            .weight(0.5f),
                        colors = TextFieldDefaults.colors(
                            unfocusedIndicatorColor = Color(156, 163, 175),
                            focusedIndicatorColor = Color(156, 163, 175),
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White
                        ),
                        textStyle = TextStyle(
                            fontSize = (screenHeight * 0.015).sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    OutlinedTextField(
                        value = "kg/Week",
                        onValueChange = { },
                        modifier = Modifier
                            .height((screenHeight * 0.055).dp)
                            .weight(0.3f),
                        colors = TextFieldDefaults.colors(
                            unfocusedIndicatorColor = Color(156, 163, 175),
                            focusedIndicatorColor = Color(156, 163, 175),
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White
                        ),
                        textStyle = TextStyle(
                            fontSize = (screenHeight * 0.015).sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Text(
                    text = "Monthly donation goal",
                    fontSize = (screenHeight * 0.015).sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = monthlyGoalState,
                        onValueChange = {
                            monthlyGoalState = it
                        },
                        modifier = Modifier
                            .height((screenHeight * 0.055).dp)
                            .weight(0.5f),
                        colors = TextFieldDefaults.colors(
                            unfocusedIndicatorColor = Color(156, 163, 175),
                            focusedIndicatorColor = Color(156, 163, 175),
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White
                        ),
                        textStyle = TextStyle(
                            fontSize = (screenHeight * 0.015).sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    OutlinedTextField(
                        value = "kg/Month",
                        onValueChange = { },
                        modifier = Modifier
                            .height((screenHeight * 0.055).dp)
                            .weight(0.3f),
                        colors = TextFieldDefaults.colors(
                            unfocusedIndicatorColor = Color(156, 163, 175),
                            focusedIndicatorColor = Color(156, 163, 175),
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White
                        ),
                        textStyle = TextStyle(
                            fontSize = (screenHeight * 0.015).sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Text(
                    text = "Yearly donation goal",
                    fontSize = (screenHeight * 0.015).sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = yearlyGoalState,
                        onValueChange = {
                            yearlyGoalState = it
                        },
                        modifier = Modifier
                            .height((screenHeight * 0.055).dp)
                            .weight(0.5f),
                        colors = TextFieldDefaults.colors(
                            unfocusedIndicatorColor = Color(156, 163, 175),
                            focusedIndicatorColor = Color(156, 163, 175),
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White
                        ),
                        textStyle = TextStyle(
                            fontSize = (screenHeight * 0.015).sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    OutlinedTextField(
                        value = "kg/Year",
                        onValueChange = { },
                        modifier = Modifier
                            .height((screenHeight * 0.055).dp)
                            .weight(0.3f),
                        colors = TextFieldDefaults.colors(
                            unfocusedIndicatorColor = Color(156, 163, 175),
                            focusedIndicatorColor = Color(156, 163, 175),
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White
                        ),
                        textStyle = TextStyle(
                            fontSize = (screenHeight * 0.015).sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            // Statistics
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = (screenHeight * 0.02).dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StatisticItem(
                        icon = Icons.Default.Eco,
                        title = "Total Donations",
                        value = profileViewmodel.realObj.totalDonation.toString()
                    )
                    StatisticItem(
                        icon = Icons.Default.BarChart,
                        title = "NGOs Supported",
                        value = profileViewmodel.realObj.ngosDonated.toString()
                    )
                    StatisticItem(
                        icon = Icons.Default.CloudQueue,
                        title = "CO2 Saved",
                        value = (profileViewmodel.realObj.totalDonation * 2.5).toString()
                    )
                }
            }

            // Security
            item {
                Text(
                    "Security",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = (screenHeight * 0.02).dp)
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy((screenHeight * 0.02).dp),
                    modifier = Modifier.padding(bottom = (screenHeight * 0.02).dp)
                ) {

                    OutlinedTextField(
                        value = passwordEdit.value,
                        onValueChange = {
                            passwordEdit.value = it
                        },
                        modifier = Modifier
                            .height((screenHeight * 0.065).dp)
                            .fillMaxWidth(),
                        label = {
                            Text(
                                "Current Password",
                                fontSize = (screenHeight * 0.015).sp,
                            )
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                        colors = TextFieldDefaults.colors(
                            unfocusedIndicatorColor = Color(156, 163, 175),
                            focusedIndicatorColor = Color(156, 163, 175),
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White
                        ),
                        textStyle = TextStyle(
                            fontSize = (screenHeight * 0.015).sp,
                            fontWeight = FontWeight.Bold
                        )

                    )
                    OutlinedTextField(
                        value = changedPassword,
                        onValueChange = {
                            changedPassword = it
                        },
                        modifier = Modifier
                            .height((screenHeight * 0.065).dp)
                            .fillMaxWidth(),
                        label = {
                            Text(
                                "New Password",
                                fontSize = (screenHeight * 0.015).sp,
                            )
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                        colors = TextFieldDefaults.colors(
                            unfocusedIndicatorColor = Color(156, 163, 175),
                            focusedIndicatorColor = Color(156, 163, 175),
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White
                        ),
                        textStyle = TextStyle(
                            fontSize = (screenHeight * 0.015).sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun StatisticItem(icon: ImageVector, title: String, value: String) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    Card(
        modifier = Modifier
            .width((screenHeight * 0.125).dp)
            .padding(4.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding((screenHeight * 0.02).dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF4CAF50)
            )
            Text(
                title,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                value,
                style = MaterialTheme.typography.titleMedium
            )

        }
    }
}

@Composable
fun EditOutfieldTextField(
    value: String,
    onValueChange: (String) -> Unit,
    leadingIcon: ImageVector,
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .height((screenHeight * 0.055).dp)
            .fillMaxWidth(),
        leadingIcon = {
            Icon(
                leadingIcon,
                contentDescription = null,
                tint = Color(156, 163, 175),
                modifier = Modifier.size((screenHeight * 0.02).dp)
            )
        },
        colors = TextFieldDefaults.colors(
            unfocusedIndicatorColor = Color(156, 163, 175),
            focusedIndicatorColor = Color(156, 163, 175),
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White
        ),
        textStyle = TextStyle(
            fontSize = (screenHeight * 0.015).sp,
            fontWeight = FontWeight.Bold
        )
    )
}

//
//@Preview(showSystemUi = false, showBackground = true)
//@Composable
//fun ProfileEditUserScreenPreview() {
//    ProfileEditUserScreen()
//}