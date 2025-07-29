package com.stone.fridge.presentation.feature.fridge.crud

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
import com.stone.fridge.MainActivity
import com.stone.fridge.R
import com.stone.fridge.core.util.Dimens
import com.stone.fridge.domain.model.FridgeItem
import com.stone.fridge.domain.model.NewFridge
import com.stone.fridge.navigation.Screen
import com.stone.fridge.presentation.feature.my.profile.copyUriToFile
import com.stone.fridge.presentation.feature.my.profile.getRealPathFromURI
import com.stone.fridge.presentation.util.PermissionDialog
import com.stone.fridge.ui.theme.CustomTheme
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewFridgeItemForm(
    fridgeItem: FridgeItem?,
    uiState: FridgeCRUDUiState,
    isFridge: Boolean,
    initSavedDate: () -> Unit,
    getSavedDate: () -> String?,
    navigate: (Screen) -> Unit,
    popBackStack: () -> Unit,
    modifyFridgeItem: (FridgeItem, File?) -> Unit,
    addFridgeItem : (NewFridge, File?) -> Unit,
){
    val context = LocalContext.current
    val packageName = context.packageName
    val focusManager = LocalFocusManager.current
    val showDialog = remember { mutableStateOf(false) }

    var image by rememberSaveable { mutableStateOf<String?>(null) }
    var imageFile by rememberSaveable { mutableStateOf<File?>(null) }
    var name by rememberSaveable { mutableStateOf("") }
    var quantity by rememberSaveable { mutableStateOf("") }

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    var expirationDate by remember { mutableLongStateOf(fridgeItem?.expirationDate ?: 0L) }
    val scannedDate = getSavedDate()
    var selectedDate by remember { mutableStateOf("") }

    LaunchedEffect(datePickerState.selectedDateMillis) {
        datePickerState.selectedDateMillis?.let {
            expirationDate = it
            selectedDate = convertMillisToDate(it)
        }
    }

    LaunchedEffect(fridgeItem) {
        fridgeItem?.let {
            image = it.image
            name = it.name
            quantity = it.quantity
            expirationDate = it.expirationDate
            selectedDate = convertMillisToDate(it.expirationDate)
        }
    }

    LaunchedEffect(scannedDate) {
        scannedDate?.let {
            if (it.isNotBlank()) {
                val parsed = parseDateToMillis(it)
                expirationDate = parsed
                selectedDate = convertMillisToDate(parsed)
            }
        }
    }

    var validator by remember { mutableStateOf(false) }
    var isQuantityInt by remember { mutableStateOf(true) }
    validator = name.isNotBlank() && selectedDate.isNotBlank() && quantity.isNotBlank()

    LaunchedEffect(uiState) {
        if(uiState == FridgeCRUDUiState.Success){
            popBackStack()
        }
    }

    val albumLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data
                uri?.let {
                    image = it.toString()
                    val filePath = context.getRealPathFromURI(it)
                    imageFile = filePath?.let { path -> File(path) } ?: context.copyUriToFile(it)
                    Log.d("TargetSDK", "imageUri - selected : $uri")
                }
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
        if(uiState == FridgeCRUDUiState.Loading){
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
                            .align(Alignment.TopEnd),
                        onClick = {
                            image = null
                            imageFile = null
                        }
                    ){
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.close),
                            contentDescription = "delete image",
                            tint = CustomTheme.colors.iconDefault,
                        )
                    }
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
                                text = "수량/g",
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
                        containerColor = CustomTheme.colors.onSurface
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
                            titleContentColor = CustomTheme.colors.textPrimary,

                            selectedDayContentColor = CustomTheme.colors.onSurface,
                            selectedDayContainerColor = CustomTheme.colors.primary,

                            selectedYearContentColor = CustomTheme.colors.onSurface,
                            selectedYearContainerColor = CustomTheme.colors.primary,
                            currentYearContentColor = CustomTheme.colors.textPrimary,

                            todayDateBorderColor = CustomTheme.colors.primary,
                            todayContentColor = CustomTheme.colors.primary,

                            weekdayContentColor = CustomTheme.colors.textPrimary, // 요일 텍스트 (월~일)
                            dayContentColor = CustomTheme.colors.textPrimary, // 일반 날짜 텍스트
                            yearContentColor = CustomTheme.colors.textPrimary, // 연도 선택 화면에서 일반 연도 텍스트
                            navigationContentColor = CustomTheme.colors.textPrimary // 월 변경 화살표 아이콘
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
                            navigate(
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
            message = "저장소 권한이 필요합니다.",
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
                    fridgeItem?.let {
                        modifyFridgeItem(
                            FridgeItem(
                                id = it.id,
                                name = name,
                                image = image,
                                quantity = if(quantity.isNotBlank()) quantity else "0",
                                expirationDate = expirationDate,
                                notification = it.notification,
                                isFridge = it.isFridge
                            ),
                            imageFile,
                        )
                    } ?: run {
                        addFridgeItem(
                            NewFridge(
                                foodName = name,
                                count = if(quantity.isNotBlank()) quantity.toInt() else 0,
                                useByDate = expirationDate,
                                alarmStatus = false,
                                storageType = isFridge
                            ),
                            imageFile
                        )
                    }
                    initSavedDate()
                }else{
                    isQuantityInt = false
                    Toast.makeText(context, "수량은 숫자만 입력 가능합니다.", Toast.LENGTH_SHORT).show()
                }
            }
        ) {
            Text(
                text = if(fridgeItem != null) "수정하기" else "등록하기",
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
