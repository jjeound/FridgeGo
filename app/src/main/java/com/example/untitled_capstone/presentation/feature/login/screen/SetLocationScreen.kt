package com.example.untitled_capstone.presentation.feature.login.screen

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
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import com.example.untitled_capstone.MainActivity
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.presentation.feature.fridge.composable.PermissionDialog
import com.example.untitled_capstone.presentation.feature.login.LoginEvent
import com.example.untitled_capstone.presentation.feature.login.state.AddressState
import com.example.untitled_capstone.presentation.feature.login.state.LoginState
import com.example.untitled_capstone.ui.theme.CustomTheme
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import java.io.IOException
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetLocationScreen(state: AddressState, onEvent: (LoginEvent) -> Unit, popBackStack: ()-> Unit, navigateToHome: () -> Unit
, from : Boolean){
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
                        Log.d(permission, "위치 권한이 허용되었습니다.")
                    }
                }
            }
        )
    Scaffold(
        containerColor = CustomTheme.colors.onSurface,
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.padding(Dimens.topBarPadding),
                title = {
                    Text(
                        text = "내 동네 설정",
                        style = CustomTheme.typography.title1,
                        color = CustomTheme.colors.textPrimary,
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {popBackStack()}
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.chevron_left),
                            tint = CustomTheme.colors.iconSelected,
                            contentDescription = "back",
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CustomTheme.colors.onSurface
                )
            )
        }
    ){ innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(
                horizontal = Dimens.surfaceHorizontalPadding,
                vertical = Dimens.surfaceVerticalPadding
            ).background(CustomTheme.colors.onSurface),
            verticalArrangement = Arrangement.Bottom
        ) {
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(Dimens.cornerRadius),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CustomTheme.colors.primary,
                    disabledContainerColor = CustomTheme.colors.onSurface,
                    contentColor = CustomTheme.colors.onPrimary,
                    disabledContentColor = CustomTheme.colors.textTertiary
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = CustomTheme.colors.border
                ),
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
                            getCurrentLocation(context) { x, y ->
                                onEvent(LoginEvent.GetAddressByCoord(x, y))
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
            ) {
                Text(
                    text = "내 동네 설정하기",
                    style = CustomTheme.typography.button1,
                )
            }
            LaunchedEffect(state) {
                if(state.address != null){
                    Toast.makeText(context, state.address.toString(), Toast.LENGTH_SHORT).show()
//                    if(from){
//                        popBackStack()
//                    } else {
//                        navigateToHome()
//                    }
                }
                if(state.error != null){
                    Log.d("error", state.error.toString())
                }
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
}

@RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
fun getCurrentLocation(context: Context, onLocationReceived: (x: String, y: String) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
        location?.let {
            onLocationReceived(it.longitude.toString(), it.latitude.toString())
        } ?: Log.e("Location", "위치를 가져올 수 없음")
    }.addOnFailureListener {
        Log.e("Location", "위치 요청 실패: ${it.message}")
    }
}



@Preview
@Composable
fun SetLocationScreenPreview(){
    SetLocationScreen(AddressState(), {}, {}, {}, false)
}



