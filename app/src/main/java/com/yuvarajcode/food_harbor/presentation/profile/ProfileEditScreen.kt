package com.yuvarajcode.food_harbor.presentation.profile

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.yuvarajcode.food_harbor.R
import com.yuvarajcode.food_harbor.utilities.ResponseState
import com.yuvarajcode.food_harbor.utilities.ToastForResponseState

@Composable
fun ProfileEditScreen(
    navController: NavController,
    profileViewmodel: ProfileViewmodel
) {
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
    Scaffold(
        topBar = {
            EditProfileTopBar(navController = navController)
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(50.dp))
            EditProfilePic(imageUri)
            Spacer(modifier = Modifier.height(30.dp))
            EditProfileDetails(
                userNameEdit,
                emailEdit,
                passwordEdit,
                phoneNumberEdit
            )
            Spacer(modifier = Modifier.height(30.dp))
            EditSaveButton(
                userNameEdit,
                emailEdit,
                passwordEdit,
                phoneNumberEdit,
                imageUri,
                profileViewmodel,
                navController
            )
        }
    }
}

@Composable
fun EditSaveButton(
    userNameEdit: MutableState<String>,
    emailEdit: MutableState<String>,
    passwordEdit: MutableState<String>,
    phoneNumberEdit: MutableState<String>,
    imageUri: MutableState<Uri>,
    profileViewmodel: ProfileViewmodel,
    navController: NavController
) {
    Button(
        onClick = {
            profileViewmodel.setUserDetails(
                userNameEdit.value,
                emailEdit.value,
                passwordEdit.value,
                imageUri.value.toString(),
                phoneNumberEdit.value
            )
            Log.d("EditSaveButton", " ${imageUri.value}")
        },
        colors = ButtonColors(
            containerColor = Color(red = 7, green = 31, blue = 27, alpha = 255),
            contentColor = Color.White,
            disabledContentColor = Color.Gray,
            disabledContainerColor = Color.Gray
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Text(
            text = "Save",
            style = MaterialTheme.typography.bodyLarge
        )
        when(val setResponse = profileViewmodel.setData.value){
            is ResponseState.Error -> {
                ToastForResponseState(message = setResponse.message)
                Log.d("edits", "EditSaveButton: ${setResponse.message}")
            }
            is ResponseState.Loading ->
            {
                    CircularProgressIndicator()
            }
            is ResponseState.Success ->
                if(setResponse.data == true){
                Log.d("edits", "EditSaveButton: ${setResponse.data}")
                navController.popBackStack()

            }
            else if (setResponse.data == false)
                {
                    ToastForResponseState(message = "An unexpected error occurred")
                    Log.d("edits", "EditSaveButton: ${setResponse.data}")
                }
        }
    }
}

@Composable
fun EditProfileDetails(
    userNameEdit : MutableState<String>,
    emailEdit : MutableState<String>,
    passwordEdit : MutableState<String>,
    phoneNumberEdit : MutableState<String>
) {

    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.Start
    ) {
        OutlinedTextField(
                value = userNameEdit.value,
                onValueChange ={
                    userNameEdit.value = it
                },
                label = { Text(text = "User Name") },
                modifier = Modifier.fillMaxWidth()
            )
        OutlinedTextField(
            value = emailEdit.value,
            onValueChange = {emailEdit.value = it},
            label = { Text(text = "Email") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = passwordEdit.value,
            onValueChange = {passwordEdit.value = it},
            label = { Text(text = "Password") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = phoneNumberEdit.value,
            onValueChange = {phoneNumberEdit.value = it},
            label = { Text(text = "Phone Number") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun EditProfilePic(
    imageUri: MutableState<Uri>
) {

    val launcher  = rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) {
        if (it != null) {
            imageUri.value = it
        }
    }
        Button(
            onClick = {
                launcher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            },
            colors = ButtonColors(
                containerColor = Color(red = 7, green = 31, blue = 27, alpha = 255),
                contentColor = Color.White,
                disabledContentColor = Color.Gray,
                disabledContainerColor = Color.Gray
            ),
            shape = CircleShape,
            modifier = Modifier
                .size(100.dp)
                .clip(shape = CircleShape)
                .border(width = 1.dp, color = Color.Gray, shape = CircleShape)
        ) {
            AsyncImage(
                model = imageUri.value,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(100.dp)
                    .clip(shape = CircleShape),
                contentScale = ContentScale.Fit
            )
        }
        Text(
            text ="Edit Profile Picture",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .padding(top=32.dp)
        )
    }


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileTopBar(
    navController: NavController
) {
    TopAppBar(
        title = {
            Text(
                text = "Edit Profile",
                modifier = Modifier.padding(8.dp)
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(red = 7, green = 31, blue = 27, alpha = 255),
            titleContentColor = Color.White
        ),
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
        navigationIcon = {
             Icon(
                 imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                 contentDescription = "Back",
                 tint = Color.White,
                 modifier = Modifier.clickable {
                     navController.popBackStack()
                 }
             )
        },
    )
}


//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    Scaffold(
//
//    ) {
//         Box (
//             modifier = Modifier
//                 .fillMaxSize()
//                 .padding(it)
//         ){
//            Column(
//                verticalArrangement = Arrangement.Center,
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Spacer(modifier = Modifier.height(50.dp))
//                EditProfilePic()
//            }
//        }
//    }
//}