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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.core.net.toUri
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.untitled_capstone.MainActivity
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.domain.model.FridgeItem
import com.example.untitled_capstone.presentation.feature.fridge.FridgeAction
import com.example.untitled_capstone.presentation.feature.fridge.FridgeState
import com.example.untitled_capstone.ui.theme.CustomTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewFridgeItemForm(id: Long?, state: FridgeState, navigate: () -> Unit, onAction: (FridgeAction) -> Unit){
    val context = LocalContext.current
    val packageName = context.packageName
    val showDialog = remember { mutableStateOf(false) }
    val fridgeItem = state.fridgeItems?.collectAsLazyPagingItems()?.itemSnapshotList?.find {
        it?.id == id
    }
    var image by remember { mutableStateOf(fridgeItem?.image?.toUri()) }

    val albumLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    result.data?.data?.let { uri ->
                        uri.let {
                            image = uri
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
    var name by remember { mutableStateOf(fridgeItem?.name ?: "") }
    var quantity by remember { mutableStateOf(fridgeItem?.quantity ?: "") }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    var expirationDate by remember { mutableLongStateOf(fridgeItem?.expirationDate ?: 0L) }
    val selectedDate = datePickerState.selectedDateMillis?.let {
        expirationDate = it
        convertMillisToDate(it)
    } ?: if(id != null){
        convertMillisToDate(expirationDate)
    }else ""
    val focusManager = LocalFocusManager.current
    var validator by remember { mutableStateOf(false) }
    validator = name.isNotBlank() && selectedDate.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.weight(1f),
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
                            .size(300.dp)
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
                        .size(300.dp)
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
                Column {
                    Row {
                        Text(
                            text = "이름",
                            style = CustomTheme.typography.caption1,
                            color = CustomTheme.colors.textPrimary,
                        )
                        Text(
                            text = " *",
                            style = CustomTheme.typography.caption1,
                            color = CustomTheme.colors.iconRed,
                        )
                    }
                    TextField(
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
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTrailingIconColor = CustomTheme.colors.iconDefault,
                            unfocusedTrailingIconColor = Color.Transparent,
                        ),
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                        singleLine = true,
                        maxLines = 1
                    )
                    HorizontalDivider(
                        modifier = Modifier.width(80.dp),
                        thickness = 1.dp,
                        color = CustomTheme.colors.border
                    )
                }
                Column {
                    Row {
                        Text(
                            text = "수량",
                            style = CustomTheme.typography.caption1,
                            color = CustomTheme.colors.textPrimary,
                        )
                    }
                    TextField(
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
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTrailingIconColor = CustomTheme.colors.iconDefault,
                            unfocusedTrailingIconColor = Color.Transparent,
                        ),
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        maxLines = 1
                    )
                    HorizontalDivider(
                        modifier = Modifier.width(80.dp),
                        thickness = 1.dp,
                        color = CustomTheme.colors.border
                    )
                }
            }
            Column {
                Row {
                    Text(
                        text = "유통기한",
                        style = CustomTheme.typography.caption1,
                        color = CustomTheme.colors.textPrimary,
                    )
                    Text(
                        text = " *",
                        style = CustomTheme.typography.caption1,
                        color = CustomTheme.colors.iconRed,
                    )
                }
                TextField(
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
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTrailingIconColor = CustomTheme.colors.iconDefault,
                        unfocusedTrailingIconColor = Color.Transparent,
                    ),

                )
                HorizontalDivider(
                    modifier = Modifier.width(300.dp),
                    thickness = 1.dp,
                    color = CustomTheme.colors.border
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
            }
            Button(
                onClick = {
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
            message = "이미지를 업로드하려면 저장소 접근 권한이 필요합니다.",
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
                navigate()
                if(id != null){
                    onAction(FridgeAction.ModifyItem(
                        FridgeItem(
                            id = id,
                            name = name,
                            image = image?.toString(),
                            quantity = quantity,
                            expirationDate = expirationDate,
                            notification = fridgeItem!!.notification,
                            isFridge = fridgeItem.isFridge
                        )
                    ))
                }else{
                    onAction(FridgeAction.AddItem(
                        FridgeItem(
                            id = 0L,
                            name = name,
                            image = image?.toString(),
                            quantity = quantity,
                            expirationDate = expirationDate,
                            notification = false,
                            isFridge = true
                        )
                    ))
                }
            }
        ) {
            Text(
                text = if(id != null) "수정하기" else "등록하기",
                style = CustomTheme.typography.button1,
            )
        }
    }
}


fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault())
    return formatter.format(Date(millis))
}

