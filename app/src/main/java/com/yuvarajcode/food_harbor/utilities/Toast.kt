package com.yuvarajcode.food_harbor.utilities

import android.widget.Toast
import android.widget.Toast.makeText
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun ToastForResponseState(
    message: String
) {
    makeText(LocalContext.current, "From error : $message", Toast.LENGTH_LONG).show()
}