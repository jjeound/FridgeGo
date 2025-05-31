package com.example.untitled_capstone.presentation.util

import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.runtime.MutableState
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import com.example.untitled_capstone.MainActivity

object RequestPermissions {

    fun requestLocationPermission(locationPermissions: Array<String>, context: Context, showDialog: MutableState<Boolean>, onRequest: (Boolean) -> Unit) {
        when {
            ContextCompat.checkSelfPermission(
                context,
                locationPermissions[0]
            ) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(
                        context,
                        locationPermissions[1]
                    ) == PackageManager.PERMISSION_GRANTED->  {
                onRequest(true)
            }
            shouldShowRequestPermissionRationale(
                context as MainActivity,
                locationPermissions[0]
            ) -> {
                showDialog.value = true
            }
            else -> {
                onRequest(false)
            }
        }
    }
}