package com.yuvarajcode.food_harbor.presentation.main.chat

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ChatViewModel @Inject constructor(
    auth : FirebaseAuth
) : ViewModel(){

    var userId = auth.currentUser?.uid
    init {
        auth.addAuthStateListener {
            userId = it.currentUser?.uid
        }
    }

    var channelId : String = ""
}

