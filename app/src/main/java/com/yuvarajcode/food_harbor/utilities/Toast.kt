package com.yuvarajcode.food_harbor.utilities

import android.widget.Toast
import android.widget.Toast.makeText
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun ToastForResponseState(
    message: String
) {
    makeText(LocalContext.current, message, Toast.LENGTH_SHORT).show()
}