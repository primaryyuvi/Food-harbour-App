package com.yuvarajcode.food_harbor.presentation.profile.edit

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.yuvarajcode.food_harbor.presentation.profile.ProfileViewmodel
import com.yuvarajcode.food_harbor.utilities.ResponseState
import com.yuvarajcode.food_harbor.utilities.ToastForResponseState

@Composable
fun ProfileEditScreen(
    navController: NavController,
    profileViewmodel: ProfileViewmodel = hiltViewModel()
) {
    Scaffold(
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
//            Spacer(modifier = Modifier.height(50.dp))
//            EditProfilePic(imageUri)
//            Spacer(modifier = Modifier.height(30.dp))
//            EditProfileDetails(
//                userNameEdit,
//                emailEdit,
//                passwordEdit,
//                phoneNumberEdit
//            )
//            Spacer(modifier = Modifier.height(30.dp))
//            EditSaveButton(
//                userNameEdit,
//                emailEdit,
//                passwordEdit,
//                phoneNumberEdit,
//                imageUri,
//                profileViewmodel,
//                navController
//            )
                if (profileViewmodel.realObj.isUser) {
                    ProfileEditUserScreen(
                        navController = navController,
                        profileViewmodel = profileViewmodel
                    )
                } else {
                    ProfileEditOrgScreen(
                        navController = navController,
                        profileViewmodel = profileViewmodel
                    )
                }
        }
    }
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