package com.stone.fridge.presentation.feature.login

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.core.content.ContextCompat
import com.stone.fridge.R
import com.stone.fridge.core.util.Dimens
import com.stone.fridge.ui.theme.CustomTheme
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import com.stone.fridge.MainActivity
import com.stone.fridge.domain.model.Address
import com.stone.fridge.presentation.util.PermissionDialog
import com.kakao.vectormap.MapView
import com.google.android.gms.location.LocationServices
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.camera.CameraPosition
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.label.LabelTextBuilder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetLocationScreen(
    uiState: LoginUiState,
    address: Address?,
    getAddressByCoord: (String, String) -> Unit,
    setLocation: (String, String) -> Unit,
    popBackStack: ()-> Unit,
    navigateToHome: () -> Unit,
    from : Boolean,  //true면 마이 화면, false면 로그인 화면에서 넘어옴
){
    val context = LocalContext.current
    var isGranted by remember { mutableStateOf(false) }
    val locationPermissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    val requestPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions(),
            onResult = { permissions ->
                locationPermissions.forEach { permission ->
                    if (permissions[permission] == true){
                        isGranted = true
                        Log.d(permission, "위치 권한이 허용되었습니다.")
                    }
                }
            }
        )
    val mapView = remember { MapView(context) }
    var lat by remember { mutableDoubleStateOf(0.0) }
    var lon by remember { mutableDoubleStateOf(0.0) }
    var showMap by remember { mutableStateOf(false) }
    val showDialog = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        when {
            ContextCompat.checkSelfPermission(
                context,
                locationPermissions[0]
            ) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(
                        context,
                        locationPermissions[1]
                    ) == PackageManager.PERMISSION_GRANTED->  {
                getCurrentLocation(context) { latitude, longitude ->
                    lat = latitude
                    lon = longitude
                    getAddressByCoord(lon.toString(), lat.toString())
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
    LaunchedEffect(isGranted) {
        if(isGranted){
            getCurrentLocation(context) { latitude, longitude ->
                lat = latitude
                lon = longitude
                getAddressByCoord(lon.toString(), lat.toString())
            }
        }
    }
    LaunchedEffect(address) {
        if(address != null){
            showMap = true
        }
    }
    LaunchedEffect(uiState) {
        if(uiState == LoginUiState.LocationSet && !from){
            navigateToHome()
        }
    }
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
            if(uiState == LoginUiState.Loading){
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    CircularProgressIndicator(
                        color = CustomTheme.colors.primary,
                    )
                }
            }
            if(showMap){
                AndroidView(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    factory = { context ->
                        mapView.apply {
                            mapView.start(
                                object : MapLifeCycleCallback() {
                                    override fun onMapDestroy() {
                                        Log.d("MapView", "Map destroyed")
                                    }

                                    override fun onMapError(exception: Exception?) {
                                        Log.e("MapView", "Error: ${exception?.message}")
                                    }
                                },
                                object : KakaoMapReadyCallback() {
                                    override fun onMapReady(p0: KakaoMap) {
                                        val cameraPosition = CameraPosition.from(lat, lon,// 중심 위치
                                            15, 0.0, 0.0, -1.0)
                                        p0.cameraMinLevel = 15
                                        val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
                                        p0.moveCamera(cameraUpdate)
                                        val styles = LabelStyles.from(
                                            LabelStyle.from(R.drawable.marker).setTextStyles(35, 0x0000000).setZoomLevel(15)
                                        )

                                        val options = LabelOptions.from(position)
                                            .setStyles(styles).setTexts(
                                                LabelTextBuilder().setTexts(
                                                    address!!.regionDong
                                                )
                                            )

                                        val layer = p0.labelManager?.layer

                                        layer?.addLabel(options)
                                    }

                                    override fun getPosition(): LatLng {
                                        // 현재 위치를 반환
                                        return LatLng.from(lat, lon)
                                    }
                                },
                            )
                        }
                    },
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.largePadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if(!from){
                    Button(
                        modifier = Modifier
                            .weight(1f),
                        shape = RoundedCornerShape(Dimens.cornerRadius),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CustomTheme.colors.onSurface,
                            contentColor = CustomTheme.colors.textSecondary,
                        ),
                        onClick = {
                            navigateToHome()
                        },
                    ) {
                        Text(
                            text = "나중에 하기",
                            style = CustomTheme.typography.button1,
                        )
                    }
                }
                Button(
                    modifier = Modifier
                        .weight(1f),
                    shape = RoundedCornerShape(Dimens.cornerRadius),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CustomTheme.colors.primary,
                        disabledContainerColor = CustomTheme.colors.onSurface,
                        contentColor = CustomTheme.colors.onPrimary,
                        disabledContentColor = CustomTheme.colors.textTertiary
                    ),
                    onClick = {
                        if(address != null){
                            setLocation(address.regionGu, address.regionDong)
                        }
                    }
                ) {
                    Text(
                        text = "동네 설정하기",
                        style = CustomTheme.typography.button1,
                    )
                }
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
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                )
            }
        )
    }
}

@RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
fun getCurrentLocation(context: Context, onLocationReceived: (lat: Double, lon: Double) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
        location?.let {
            onLocationReceived(it.latitude, it.longitude)
        } ?: Log.e("Location", "위치를 가져올 수 없음")
    }.addOnFailureListener {
        Log.e("Location", "위치 요청 실패: ${it.message}")
    }
}



