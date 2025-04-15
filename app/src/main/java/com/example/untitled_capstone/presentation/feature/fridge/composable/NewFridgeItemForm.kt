package com.example.untitled_capstone.presentation.feature.fridge.composable

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.example.untitled_capstone.MainActivity
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.domain.model.FridgeItem
import com.example.untitled_capstone.navigation.Screen
import com.example.untitled_capstone.presentation.feature.fridge.FridgeAction
import com.example.untitled_capstone.presentation.feature.fridge.FridgeState
import com.example.untitled_capstone.ui.theme.CustomTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewFridgeItemForm(
    id: Long?,
    state: FridgeState,
    onAction: (FridgeAction) -> Unit,
    initSavedState: () -> Unit,
    getSavedDate: () -> String?,
    onNavigate: (Screen) -> Unit,
    popBackStack: () -> Unit,
    showSnackbar: (String) -> Unit,
){
    val context = LocalContext.current
    val packageName = context.packageName
    val focusManager = LocalFocusManager.current
    val showDialog = remember { mutableStateOf(false) }

    var image by remember { mutableStateOf(state.response?.image) }
    var name by rememberSaveable { mutableStateOf("") }
    var quantity by rememberSaveable { mutableStateOf("") }

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    var expirationDate by remember { mutableLongStateOf(0L) }
    val scannedDate = getSavedDate()
    var selectedDate = datePickerState.selectedDateMillis?.let {
        expirationDate = it
        convertMillisToDate(it)
    } ?: if (state.response != null) {
        state.response!!.expirationDate.let {
            expirationDate = it // 기존 데이터가 있으면 expirationDate 설정
            convertMillisToDate(it)
        }
    }  else {
        ""
    }

    scannedDate?.let {
        if (scannedDate.isNotBlank()) {
            val parsedDate = parseDateToMillis(scannedDate)
            expirationDate = parsedDate
            selectedDate = convertMillisToDate(parsedDate)
        }
    }

    var validator by remember { mutableStateOf(false) }
    var isQuantityInt by remember { mutableStateOf(true) }
    validator = name.isNotBlank() && selectedDate.isNotBlank() && quantity.isNotBlank()
    LaunchedEffect(Unit) {
        id?.let { onAction(FridgeAction.GetItemById(it)) }
    }
    LaunchedEffect(state.response) {
        state.response?.let {
            name = it.name
            image = it.image
            quantity = it.quantity
            expirationDate = it.expirationDate
        }
    }

    val albumLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    result.data?.data?.let { uri ->
                        uri.let {
                            image = uri.toString()
                            Log.d("TargetSDK", "imageUri - selected : $uri")
                        }
                    }
                }
                Activity.RESULT_CANCELED -> Unit
            }
        }
    val imageAlbumIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
        type = "image/*"
        putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
        addCategory(Intent.CATEGORY_OPENABLE)
    }
    val galleryPermissions = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> arrayOf(
            Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED,
            Manifest.permission.READ_MEDIA_IMAGES,
        )
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> arrayOf(
            Manifest.permission.READ_MEDIA_IMAGES
        )
        else -> arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    }
    val requestPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions(),
            onResult = { permissions ->
                galleryPermissions.forEach { permission ->
                    if (permissions[permission] == true){
                        Log.d("gallery", "gallery permission granted")
                    }
                }
            }
        )
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Log.d("camera", "camera permission granted")
        } else {
            Toast.makeText(context, "카메라 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize().imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if(state.isLoading){
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator()
            }
        }
        Column(
            modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens.largePadding)
        ) {
            if(image != null){
                Box{
                    AsyncImage(
                        model = image,
                        contentDescription = "image",
                        alignment = Alignment.Center,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(shape = RoundedCornerShape(Dimens.cornerRadius))
                    )
                    IconButton(
                        modifier = Modifier
                            .align(Alignment.BottomEnd),
                        onClick = {
                            albumLauncher.launch(imageAlbumIntent)
                        }
                    ){
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.camera),
                            contentDescription = "get image",
                            tint = CustomTheme.colors.iconDefault,
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(shape = RoundedCornerShape(Dimens.cornerRadius))
                        .background(CustomTheme.colors.surface)
                ){
                    IconButton(
                        modifier = Modifier
                            .align(Alignment.BottomEnd),
                        onClick = {
                            when {
                                ContextCompat.checkSelfPermission(
                                    context,
                                    galleryPermissions[0]
                                ) == PackageManager.PERMISSION_GRANTED ->  {
                                    albumLauncher.launch(imageAlbumIntent)
                                }
                                shouldShowRequestPermissionRationale(
                                    context as MainActivity,
                                    galleryPermissions[0]
                                ) -> {
                                    showDialog.value = true
                                }
                                else -> {
                                    requestPermissionLauncher.launch(galleryPermissions)
                                }
                            }
                        }
                    ){
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.camera),
                            contentDescription = "get image",
                            tint = CustomTheme.colors.iconDefault,
                        )
                    }
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens.mediumPadding)
            ) {
                TextField(
                    label = {
                        Row {
                            Text(
                                text = "이름",
                                style = CustomTheme.typography.caption1,
                                color = CustomTheme.colors.textSecondary,
                            )
                            Text(
                                text = " *",
                                style = CustomTheme.typography.caption1,
                                color = CustomTheme.colors.iconRed,
                            )
                        }
                    },
                    value = name,
                    onValueChange = {name = it},
                    modifier = Modifier.width(80.dp),
                    textStyle = CustomTheme.typography.button2,
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = CustomTheme.colors.textPrimary,
                        unfocusedTextColor = CustomTheme.colors.textPrimary,
                        focusedContainerColor = CustomTheme.colors.onSurface,
                        unfocusedContainerColor = CustomTheme.colors.onSurface,
                        cursorColor = CustomTheme.colors.textPrimary,
                        focusedIndicatorColor = CustomTheme.colors.primary,
                        unfocusedIndicatorColor = CustomTheme.colors.border,
                        focusedTrailingIconColor = CustomTheme.colors.iconDefault,
                        unfocusedTrailingIconColor = Color.Transparent,
                    ),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    singleLine = true,
                    maxLines = 1
                )
                TextField(
                    label = {
                        Row {
                            Text(
                                text = "수량",
                                style = CustomTheme.typography.caption1,
                                color = CustomTheme.colors.textSecondary,
                            )
                            Text(
                                text = " *",
                                style = CustomTheme.typography.caption1,
                                color = CustomTheme.colors.iconRed,
                            )
                        }
                    },
                    value = quantity,
                    onValueChange = {quantity = it},
                    modifier = Modifier.width(80.dp),
                    textStyle = CustomTheme.typography.button2,
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = CustomTheme.colors.textPrimary,
                        unfocusedTextColor = CustomTheme.colors.textPrimary,
                        focusedContainerColor = CustomTheme.colors.onSurface,
                        unfocusedContainerColor = CustomTheme.colors.onSurface,
                        cursorColor = CustomTheme.colors.textPrimary,
                        focusedIndicatorColor = CustomTheme.colors.primary,
                        unfocusedIndicatorColor = CustomTheme.colors.border,
                        focusedTrailingIconColor = CustomTheme.colors.iconDefault,
                        unfocusedTrailingIconColor = Color.Transparent,
                        errorIndicatorColor = CustomTheme.colors.error,
                        errorContainerColor = CustomTheme.colors.onSurface
                    ),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    maxLines = 1,
                    isError = !isQuantityInt
                )
            }
            TextField(
                label = {
                    Row {
                        Text(
                            text = "유통기한",
                            style = CustomTheme.typography.caption1,
                            color = CustomTheme.colors.textSecondary,
                        )
                        Text(
                            text = " *",
                            style = CustomTheme.typography.caption1,
                            color = CustomTheme.colors.iconRed,
                        )
                    }
                },
                modifier = Modifier.width(300.dp),
                value = selectedDate,
                onValueChange = { },
                textStyle = CustomTheme.typography.button2,
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = {
                        showDatePicker = !showDatePicker
                        focusManager.clearFocus()
                    }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.calendar),
                            contentDescription = "Select date",
                            tint = CustomTheme.colors.iconSelected
                        )
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = CustomTheme.colors.textPrimary,
                    unfocusedTextColor = CustomTheme.colors.textPrimary,
                    focusedContainerColor = CustomTheme.colors.onSurface,
                    unfocusedContainerColor = CustomTheme.colors.onSurface,
                    cursorColor = CustomTheme.colors.textPrimary,
                    focusedIndicatorColor = CustomTheme.colors.primary,
                    unfocusedIndicatorColor = CustomTheme.colors.border,
                    focusedTrailingIconColor = CustomTheme.colors.iconDefault,
                    unfocusedTrailingIconColor = Color.Transparent,
                ),

            )
            if(showDatePicker){
                DatePickerDialog(
                    onDismissRequest = {},
                    confirmButton = {
                        TextButton(onClick = {
                            showDatePicker = false
                        }) {
                            Text(
                                text = "확인",
                                color = CustomTheme.colors.textPrimary,
                                style = CustomTheme.typography.button2,
                            )
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {showDatePicker = false}) {
                            Text(
                                text = "취소",
                                color = CustomTheme.colors.textPrimary,
                                style = CustomTheme.typography.button2,
                            )
                        }
                    },
                    colors = DatePickerDefaults.colors(
                        containerColor = CustomTheme.colors.onSurface,
                    )
                ) {
                    DatePicker(
                        state = datePickerState,
                        showModeToggle = false,
                        title = {
                            Text(
                                text = ""
                            )
                        },
                        colors = DatePickerDefaults.colors(
                            containerColor = CustomTheme.colors.onSurface,
                        )
                    )
                }
            }
            Button(
                onClick = {
                    when {
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED ->  {
                            onNavigate(
                                Screen.ScanNav
                            )
                        }
                        shouldShowRequestPermissionRationale(
                            context as MainActivity,
                            Manifest.permission.CAMERA
                        ) -> {
                            showDialog.value = true
                        }
                        else -> {
                            cameraLauncher.launch(Manifest.permission.CAMERA)
                        }
                    }
                },
                shape = RoundedCornerShape(Dimens.cornerRadius),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CustomTheme.colors.primary,
                ),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "스캔하기",
                    style = CustomTheme.typography.button1,
                    color = CustomTheme.colors.onPrimary
                )
            }
        }
        PermissionDialog(
            showDialog = showDialog,
            message = "접근 권한이 필요합니다.",
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
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.mediumPadding),
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
            enabled = validator,
            onClick = {
                if(quantity.all { it.isDigit() }){
                    popBackStack()
                    initSavedState()
                    state.response?.let {
                        onAction(FridgeAction.ModifyItem(
                            FridgeItem(
                                id = it.id,
                                name = name,
                                image = image,
                                quantity = quantity,
                                expirationDate = expirationDate,
                                notification = it.notification,
                                isFridge = it.isFridge
                            )
                        ))
                        onAction(FridgeAction.InitState)
                    } ?: run {
                        onAction(FridgeAction.AddItem(
                            FridgeItem(
                                id = 0L,
                                name = name,
                                image = image,
                                quantity = quantity,
                                expirationDate = expirationDate,
                                notification = false,
                                isFridge = true
                            )
                        ))
                    }
                }else{
                    isQuantityInt = false
                    showSnackbar("수량은 숫자만 입력하세요.")
                }
            }
        ) {
            Text(
                text = if(state.response != null) "수정하기" else "등록하기",
                style = CustomTheme.typography.button1,
            )
        }
    }
}



fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault())
    return formatter.format(Date(millis))
}

fun parseDateToMillis(dateString: String): Long {
    val dateFormats = listOf(
        "yyyy.MM.dd",
        "yyyy/MM/dd",
        "yyyy-MM-dd"
    )

    for (format in dateFormats) {
        try {
            val formatter = SimpleDateFormat(format, Locale.getDefault())
            val date = formatter.parse(dateString)
            if (date != null) {
                return date.time
            }
        } catch (e: Exception) {
            Log.e("ParseDate", "Failed to parse date: $dateString with format: $format", e)
        }
    }

    // 모든 포맷이 실패하면 0L 반환
    return 0L
}
