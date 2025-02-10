package com.yuvarajcode.food_harbor.presentation.profile.edit

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.yuvarajcode.food_harbor.R
import com.yuvarajcode.food_harbor.presentation.profile.ProfileViewmodel
import com.yuvarajcode.food_harbor.utilities.ResponseState
import com.yuvarajcode.food_harbor.utilities.ToastForResponseState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditOrgScreen(
    navController: NavController,
    profileViewmodel: ProfileViewmodel
) {
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
    val missionStatement = remember {
        mutableStateOf(profileViewmodel.realObj.missionStatement)
    }
    val launcher  = rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) {
        if (it != null) {
            imageUri.value = it
        }
    }
    var monthlyGoalState by remember { mutableStateOf(profileViewmodel.realObj.monthlyGoal.toString()) }
    var weeklyGoalState by remember { mutableStateOf(profileViewmodel.realObj.weeklyGoal.toString()) }
    var yearlyGoalState by remember { mutableStateOf(profileViewmodel.realObj.yearlyGoal.toString()) }
    var changedPassword by remember { mutableStateOf("") }
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp

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
            CenterAlignedTopAppBar(
                title = { Text(
                    "Edit Profile",
                    fontWeight = FontWeight.Bold,
                )
                        },
                navigationIcon = {
                    IconButton(
                       onClick = {
                           navController.navigateUp()
                       }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            profileViewmodel.setUserDetails(
                                username = userNameEdit.value,
                                email = emailEdit.value,
                                password = passwordEdit.value,
                                profilePicture = imageUri.value.toString(),
                                phone = phoneNumberEdit.value,
                                missionStatement = missionStatement.value,
                                weeklyGoal = weeklyGoalState.toInt(),
                                monthlyGoal = monthlyGoalState.toInt(),
                                yearlyGoal = yearlyGoalState.toInt(),
                                name = nameEdit.value,
                            )
                        },
                        enabled = userNameEdit.value.isNotEmpty() && emailEdit.value.isNotEmpty() && phoneNumberEdit.value.isNotEmpty() && missionStatement.value.isNotEmpty() && passwordEdit.value.isNotEmpty()  && nameEdit.value.isNotEmpty()
                    ) {
                        Text("Save", color = Color(0xFF4CAF50))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = (screenHeight * 0.02).dp)
                .padding(it)
        ) {
            // Header Image and Profile Picture
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height((screenHeight * 0.15).dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    // Profile Picture
                    AsyncImage(
                        model = imageUri.value,
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size((screenHeight * 0.09).dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.Gray, CircleShape)
                            .clickable {
                                launcher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                        contentScale = ContentScale.Crop
                    )
                    // Camera Icon
                    Text(
                        text = "Change your profile picture",
                        modifier = Modifier.padding(top = 8.dp),
                    )
                }
            }

            // Organization Details
            item {
                Column(modifier = Modifier.padding(vertical = (screenHeight * 0.02).dp)) {
                    EditOutlinedTextField(
                        value = nameEdit.value,
                        onValueChange = {
                            nameEdit.value = it
                        },
                        label = "Organization Name",
                        labelIcon = Icons.Default.Business
                    )

                    Spacer(modifier = Modifier.height((screenHeight * 0.02).dp))

                    EditOutlinedTextField(
                        value = emailEdit.value,
                        onValueChange = {
                            emailEdit.value = it
                        },
                        label = "Email Address",
                        labelIcon = Icons.Default.Email
                    )

                    Spacer(modifier = Modifier.height((screenHeight * 0.02).dp))

                    EditOutlinedTextField(
                        value = userNameEdit.value,
                        onValueChange = {
                            userNameEdit.value = it
                        },
                        label = "Username",
                        labelIcon = Icons.Default.Person
                    )

                    Spacer(modifier = Modifier.height((screenHeight * 0.02).dp))

                    EditOutlinedTextField(
                        value = phoneNumberEdit.value,
                        onValueChange = {
                            phoneNumberEdit.value = it
                        },
                        label = "Phone Number",
                        labelIcon = Icons.Default.Phone
                    )

                    Spacer(modifier = Modifier.height((screenHeight * 0.02).dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Mission Statement",
                            modifier = Modifier.padding(start = 8.dp),
                            color = Color(75, 85, 99),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    OutlinedTextField(
                        value = missionStatement.value,
                        onValueChange = {
                            missionStatement.value = it
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            unfocusedIndicatorColor = Color(156, 163, 175),
                            focusedIndicatorColor = Color(156, 163, 175),
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White
                        ),
                        textStyle = TextStyle(
                            fontSize = (screenHeight * 0.015).sp,
                            fontWeight = FontWeight.Bold
                        ),
                        maxLines = 3,
                    )
                }
            }

            // Impact Goals Section

            item {
                // Remember state for the selected period and its corresponding goal value.
                var selectedPeriod by remember { mutableStateOf("Weekly") }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = (screenHeight * 0.02).dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(0.5.dp, Color(156, 163, 175))
                ) {
                    Column(
                        modifier = Modifier.padding((screenHeight * 0.02).dp)
                    ) {
                        // Header row with icon and title.
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Default.BarChart,
                                contentDescription = null,
                                tint = Color(0xFF007AFF)
                            )
                            Text(
                                "Impact Goals",
                                fontSize = (screenHeight * 0.0225).sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    HorizontalDivider()

                    // Period Selector and the input field for the goals.
                    Column(
                        modifier = Modifier.padding((screenHeight * 0.02).dp)
                    ) {
                        // Period selection row.
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = (screenHeight * 0.02).dp)
                        ) {
                            listOf("Weekly", "Monthly", "Yearly").forEach { period ->
                                Button(
                                    onClick = { selectedPeriod = period },
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(horizontal = 4.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (selectedPeriod == period)
                                            Color(0xFF007AFF)
                                        else
                                            Color(243, 244, 246),
                                        contentColor = if (selectedPeriod == period)
                                            Color.White
                                        else
                                            Color(75, 85, 99)
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(period)
                                }
                            }
                        }

                        // Determine which goal value to display/edit based on the selected period.
                        val currentGoal = when (selectedPeriod) {
                            "Weekly" -> weeklyGoalState
                            "Monthly" -> monthlyGoalState
                            "Yearly" -> yearlyGoalState
                            else -> monthlyGoalState
                        }

                        // Text field that shows the goal value and updates the corresponding state.
                        EditOutlinedTextField(
                            value = currentGoal,
                            onValueChange = { newValue ->
                                when (selectedPeriod) {
                                    "Weekly" -> weeklyGoalState = newValue
                                    "Monthly" -> monthlyGoalState = newValue
                                    "Yearly" -> yearlyGoalState = newValue
                                }
                            },
                            label = "$selectedPeriod Food Quantity (kg)",
                            labelIcon = Icons.Default.FoodBank
                        )

                        Spacer(modifier = Modifier.height((screenHeight * 0.02).dp))
                    }
                }
            }

            item {
                Text(
                    "Security",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = (screenHeight * 0.02).dp, bottom = 8.dp)
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
private fun EditOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    labelIcon: ImageVector,
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = labelIcon,
                contentDescription = null,
                tint = Color(75, 85, 99)
            )
            Text(
                text = label,
                modifier = Modifier.padding(start = 8.dp),
                color = Color(75, 85, 99),
                fontWeight = FontWeight.Bold
            )
        }
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .height((screenHeight * 0.055).dp)
                .fillMaxWidth(),
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



//@Preview(showBackground = true)
//@Composable
//fun PreviewProfileEditOrgScreen() {
//    ProfileEditOrgScreen()
//}