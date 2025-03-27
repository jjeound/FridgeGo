package com.example.untitled_capstone.presentation.feature.login

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import com.example.untitled_capstone.MainActivity
import com.example.untitled_capstone.presentation.feature.fridge.composable.PermissionDialog
import com.example.untitled_capstone.ui.theme.CustomTheme
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import java.io.IOException
import java.util.Locale

@Composable
fun SetLocationScreen(state: LoginState, onEvent: (LoginEvent) -> Unit, navigateToHome: () -> Unit){
    val context = LocalContext.current
    val packageName = context.packageName
    val showDialog = remember { mutableStateOf(false)}
    val locationPermissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    val requestPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions(),
            onResult = { permissions ->
                locationPermissions.forEach { permission ->
                    if (permissions[permission] == true){
                        Log.d("permission", "location permission granted")
                    }
                }
            }
        )
    Column(
        modifier = Modifier.fillMaxSize().background(CustomTheme.colors.primary)
    ) {
        Button(
            onClick = {
                when {
                    ContextCompat.checkSelfPermission(
                        context,
                        locationPermissions[0]
                    ) == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(
                                context,
                                locationPermissions[1]
                            ) == PackageManager.PERMISSION_GRANTED->  {
                        getCurrentLocation(context) { latLng ->
                            getAddressFromLocation(context, latLng.latitude, latLng.longitude) { address ->
                                Log.d("address", address)
                                //navigateToHome()
                                //성공 메시지 후 홈 화면으로 이동
                            }
                        }
                    }
                    shouldShowRequestPermissionRationale(
                        context as MainActivity,
                        locationPermissions[0]
                    ) -> {
                        showDialog.value = true
                    }
                    else -> {
                        requestPermissionLauncher.launch(locationPermissions)
                    }
                }
            }
        ){
            Text(
                text = "위치 권한 요청"
            )
        }
        PermissionDialog(
            showDialog = showDialog,
            message = "위치 권한이 필요합니다.",
            onDismiss = { showDialog.value = false },
            onConfirm = {
                showDialog.value = false
                context.startActivity(
                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", packageName, null)
                    }
                )
            }
        )
    }
}

@RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
fun getCurrentLocation(context: Context, onLocationReceived: (LatLng) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
        location?.let {
            val latLng = LatLng(it.latitude, it.longitude)
            onLocationReceived(latLng)
        } ?: Log.e("Location", "위치를 가져올 수 없음")
    }.addOnFailureListener {
        Log.e("Location", "위치 요청 실패: ${it.message}")
    }
}

fun getAddressFromLocation(context: Context, lat: Double, lng: Double, onAddressReceived: (String) -> Unit) {
    val geocoder = Geocoder(context, Locale.getDefault())

    try {
        val addressList: List<Address>? = geocoder.getFromLocation(lat, lng, 1)
        if (!addressList.isNullOrEmpty()) {
            val address = addressList[0]
            val district = address.subLocality ?: address.locality ?: "알 수 없음"
            onAddressReceived(district)
        } else {
            onAddressReceived("주소를 찾을 수 없음")
        }
    } catch (e: IOException) {
        onAddressReceived("주소 변환 실패")
        e.printStackTrace()
    }
}



