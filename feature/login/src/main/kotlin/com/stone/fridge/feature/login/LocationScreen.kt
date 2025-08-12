package com.stone.fridge.feature.login

import android.Manifest
import android.app.Activity
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.location.LocationServices
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraPosition
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.label.LabelTextBuilder
import com.stone.fridge.core.designsystem.Dimens
import com.stone.fridge.core.designsystem.R
import com.stone.fridge.core.designsystem.theme.CustomTheme
import com.stone.fridge.core.model.AddressInfo
import com.stone.fridge.core.navigation.currentComposeNavigator
import com.stone.fridge.core.ui.GoPreviewTheme
import com.stone.fridge.core.ui.PermissionDialog
import com.stone.fridge.feature.login.KakaoMap

@Composable
fun LocationScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onShowSnackbar: suspend (String, String?) -> Unit,
    onLocationSet: () -> Unit,
    from : Boolean,  //true면 마이 화면, false면 로그인 화면에서 넘어옴
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val address by viewModel.address.collectAsStateWithLifecycle()
    LocationScreenContent(
        uiState = uiState,
        address = address,
        onShowSnackbar = onShowSnackbar,
        onLocationSet = onLocationSet,
        getAddressByCoord = viewModel::getAddressByCoord,
        setLocation = viewModel::setLocation,
        from = from,
    )
}

@Composable
private fun LocationScreenContent(
    uiState: LoginUiState,
    address: AddressInfo?,
    onShowSnackbar: suspend (String, String?) -> Unit,
    onLocationSet: () -> Unit,
    getAddressByCoord: (lon: String, lat: String) -> Unit,
    setLocation: (gu: String, dong: String) -> Unit,
    from: Boolean,
){
    val context = LocalContext.current
    val composeNavigator = currentComposeNavigator
    val showDialog = remember { mutableStateOf(false) }
    LaunchedEffect(uiState) {
        if(uiState == LoginUiState.LocationSet && !from){
            onLocationSet()
        } else if (uiState is LoginUiState.Error){
            onShowSnackbar(uiState.message, null)
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
                        onClick = { composeNavigator.navigateUp() }
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
            } else if(address != null){
                KakaoMap(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    address = address,
                    getAddressByCoord = getAddressByCoord,
                    onShowDialog = { showDialog.value = true },
                )
            }
            SubmitButton(
                address = address,
                setLocation = setLocation,
                onLocationSet = onLocationSet,
                from = from
            )
        }
        PermissionDialog(
            showDialog = showDialog.value,
            message = "위치 권한이 필요합니다.",
            onEvent = { showDialog.value = false},
            context = context,
            intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        )
    }
}



@Composable
private fun KakaoMap(
    modifier: Modifier,
    address: AddressInfo?,
    getAddressByCoord: (lon: String, lat: String) -> Unit,
    onShowDialog: () -> Unit,
){
    val context = LocalContext.current
    val mapView = remember { MapView(context) }
    var lat by remember { mutableDoubleStateOf(0.0) }
    var lon by remember { mutableDoubleStateOf(0.0) }
    val activity = context as? Activity
    val locationPermission = Manifest.permission.ACCESS_COARSE_LOCATION
    val requestPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                if (isGranted){
                    getCurrentLocation(context) { latitude, longitude ->
                        lat = latitude
                        lon = longitude
                        getAddressByCoord(lon.toString(), lat.toString())
                    }
                }
            }
        )
    LaunchedEffect(Unit) {
        when {
            ContextCompat.checkSelfPermission(
                context,
                locationPermission
            ) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(
                        context,
                        locationPermission
                    ) == PackageManager.PERMISSION_GRANTED->  {
                getCurrentLocation(context) { latitude, longitude ->
                    lat = latitude
                    lon = longitude
                    getAddressByCoord(lon.toString(), lat.toString())
                }
            }
            activity != null && shouldShowRequestPermissionRationale(
                activity,
                locationPermission
            ) -> {
                onShowDialog()
            }
            else -> {
                requestPermissionLauncher.launch(locationPermission)
            }
        }
    }
    AndroidView(
        modifier = modifier,
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

@Composable
private fun SubmitButton(
    address: AddressInfo? = null,
    setLocation: (gu: String, dong: String) -> Unit,
    onLocationSet: () -> Unit,
    from: Boolean
){
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
                    onLocationSet()
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

@RequiresPermission(allOf = [Manifest.permission.ACCESS_COARSE_LOCATION])
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

@Preview
@Composable
fun LocationScreenContentPreview() {
    GoPreviewTheme {
        LocationScreenContent(
            uiState = LoginUiState.Idle,
            address = AddressInfo("서울특별시", "강남구"),
            onShowSnackbar = { _, _ -> },
            onLocationSet = {},
            getAddressByCoord = { _, _ -> },
            setLocation = { _, _ -> },
            from = false
        )
    }
}

