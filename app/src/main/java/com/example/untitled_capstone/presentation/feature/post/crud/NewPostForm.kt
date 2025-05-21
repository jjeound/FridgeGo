package com.example.untitled_capstone.presentation.feature.post.crud

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.example.untitled_capstone.MainActivity
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.domain.model.NewPost
import com.example.untitled_capstone.presentation.feature.fridge.crud.PermissionDialog
import com.example.untitled_capstone.presentation.feature.my.profile.getRealPathFromURI
import com.example.untitled_capstone.ui.theme.CustomTheme
import java.io.File

@Composable
fun NewPostForm(
    addNewPost: (NewPost, List<File>?) -> Unit,
){
    val context = LocalContext.current
    var isExpandedPeopleMenu by remember { mutableStateOf(false) }
    var isExpandedCategoryMenu by remember { mutableStateOf(false) }
    val menuItemDataInPeople = List(10) { "${it + 1}" }
    val menuItemDataInCategory = listOf(Category.VEGETABLE, Category.FRUIT, Category.MEAT, Category.SEAFOOD, Category.DAIRY, Category.GRAIN, Category.BEVERAGE, Category.SNACK, Category.CONDIMENT, Category.FROZEN, Category.PROCESSED).map { it.kor }
    var quantity by remember { mutableStateOf("2") }
    var category by remember { mutableStateOf(Category.VEGETABLE.kor) }
    var price by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    var validator by remember { mutableStateOf(false) }
    val images = remember { mutableStateListOf<String>() }
    val showDialog = remember { mutableStateOf(false) }
    val files = remember { mutableStateListOf<File>() }
    val albumLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.clipData?.let { clipData ->
                    val imageUris =  (0 until clipData.itemCount).map { index ->
                        clipData.getItemAt(index).uri
                    }
                    imageUris.forEach { uri ->
                        images.add(uri.toString())
                        val filePath = context.getRealPathFromURI(uri)
                        if (filePath != null) {
                            files.add(File(filePath))
                        }
                        Log.d("TargetSDK", "imageUri - selected : $uri")
                    }
                } ?: result.data?.data?.let { uri ->
                    images.add(uri.toString())
                    val filePath = context.getRealPathFromURI(uri)
                    if (filePath != null) {
                        files.add(File(filePath))
                    }
                    Log.d("TargetSDK", "imageUri - selected : $uri")
                }
            }
        }

    val imageAlbumIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
        type = "image/*"
        putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
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
                        Log.d("gallery", "$permission granted")
                    }
                }
            }
        )
    validator = price.isNotBlank() && title.isNotBlank() && content.isNotBlank()
    Column(
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures(onTap = {
                focusManager.clearFocus()
            })
        },
        verticalArrangement = Arrangement.spacedBy(Dimens.mediumPadding)
    ){
        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimens.mediumPadding)
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(shape = RoundedCornerShape(Dimens.cornerRadius))
                    .background(CustomTheme.colors.onSurface)
                    .border(
                        width = 1.dp,
                        color = CustomTheme.colors.border,
                        shape = RoundedCornerShape(Dimens.cornerRadius)
                    )
            ){
                IconButton(
                    modifier = Modifier
                        .align(Alignment.Center),
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
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(Dimens.smallPadding)
            ) {
                itemsIndexed(images) { index, image ->
                    Box {
                        AsyncImage(
                            model = image.toUri(),
                            contentDescription = "image",
                            alignment = Alignment.Center,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(80.dp)
                                .clip(shape = RoundedCornerShape(Dimens.cornerRadius))
                        )
                        IconButton(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .then(Modifier.size(24.dp))
                                .padding(Dimens.smallPadding),
                            onClick = {
                                images.removeAt(index)
                            }
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.close),
                                contentDescription = "delete image",
                                tint = CustomTheme.colors.iconSelected,
                            )
                        }
                    }
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(Dimens.mediumPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.people),
                    contentDescription = "people",
                    tint = CustomTheme.colors.iconDefault,
                )
                Text(
                    text = "인원 수",
                    style = CustomTheme.typography.caption2,
                    color = CustomTheme.colors.textSecondary,
                )
            }
            Box{
                Card(
                    modifier = Modifier.wrapContentSize().clickable {
                        isExpandedPeopleMenu = !isExpandedPeopleMenu
                    },
                    shape = RoundedCornerShape(Dimens.cornerRadius),
                    colors = CardDefaults.cardColors(
                        containerColor = CustomTheme.colors.onSurface,
                    ),
                    border = BorderStroke(width = 1.dp, color = CustomTheme.colors.border),
                ) {
                    Row(
                        modifier = Modifier.padding(start = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = quantity,
                            style = CustomTheme.typography.caption2,
                            color = CustomTheme.colors.textPrimary,
                        )
                        Spacer(
                            modifier = Modifier.width(8.dp)
                        )
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.dropdown),
                            contentDescription = "select number of people",
                            tint = CustomTheme.colors.iconSelected,
                        )
                    }
                }
                DropdownMenu(
                    expanded = isExpandedPeopleMenu,
                    onDismissRequest = { isExpandedPeopleMenu = false },
                    containerColor = CustomTheme.colors.textTertiary,
                    shadowElevation = 0.dp,
                    tonalElevation = 0.dp,
                    shape = RoundedCornerShape(Dimens.cornerRadius),
                ) {
                    menuItemDataInPeople.forEach { option ->
                        DropdownMenuItem(
                            modifier = Modifier.height(30.dp),
                            text = {
                                Text(
                                    text = option,
                                    style = CustomTheme.typography.caption2,
                                    color = CustomTheme.colors.textPrimary,
                                )},
                            onClick = {
                                isExpandedPeopleMenu = false
                                quantity = option
                            },
                        )
                    }
                }
            }
            Text(
                text = "카테고리",
                style = CustomTheme.typography.caption2,
                color = CustomTheme.colors.textSecondary,
            )
            Box{
                Card(
                    modifier = Modifier.wrapContentSize().clickable {
                        isExpandedCategoryMenu = !isExpandedCategoryMenu
                    },
                    shape = RoundedCornerShape(Dimens.cornerRadius),
                    colors = CardDefaults.cardColors(
                        containerColor = CustomTheme.colors.onSurface,
                    ),
                    border = BorderStroke(width = 1.dp, color = CustomTheme.colors.border),
                ) {
                    Row(
                        modifier = Modifier.padding(start = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = category,
                            style = CustomTheme.typography.caption2,
                            color = CustomTheme.colors.textPrimary,
                        )
                        Spacer(
                            modifier = Modifier.width(8.dp)
                        )
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.dropdown),
                            contentDescription = "select number of people",
                            tint = CustomTheme.colors.iconSelected,
                        )
                    }
                }
                DropdownMenu(
                    expanded = isExpandedCategoryMenu,
                    onDismissRequest = { isExpandedCategoryMenu = false },
                    containerColor = CustomTheme.colors.textTertiary,
                    shadowElevation = 0.dp,
                    tonalElevation = 0.dp,
                    shape = RoundedCornerShape(Dimens.cornerRadius),
                ) {
                    menuItemDataInCategory.forEach { option ->
                        DropdownMenuItem(
                            modifier = Modifier.height(30.dp),
                            text = {
                                Text(
                                    text = option,
                                    style = CustomTheme.typography.caption2,
                                    color = CustomTheme.colors.textPrimary,
                                )},
                            onClick = {
                                isExpandedCategoryMenu = false
                                category = option
                            },
                        )
                    }
                }
            }
        }
        OutlinedTextField(
            value = price,
            onValueChange = {price = it},
            placeholder = {
                Text(
                    text = "가격",
                    style = CustomTheme.typography.body1,
                    color = CustomTheme.colors.textSecondary
                )
            },
            leadingIcon = {
                Text(
                    text = "₩",
                    style = CustomTheme.typography.body1,
                    color = CustomTheme.colors.textPrimary
                )
            },
            trailingIcon = {
                if(price.isNotBlank()){
                    IconButton(
                        onClick = { price = "" }
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.delete),
                            contentDescription = "delete",
                        )
                    }
                }
            },
            textStyle = CustomTheme.typography.body1,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = CustomTheme.colors.textSecondary,
                unfocusedBorderColor = CustomTheme.colors.textSecondary,
                focusedTextColor = CustomTheme.colors.textPrimary,
                unfocusedTextColor = CustomTheme.colors.textPrimary,
                focusedContainerColor = CustomTheme.colors.onSurface,
                unfocusedContainerColor = CustomTheme.colors.onSurface,
                cursorColor = CustomTheme.colors.textPrimary,
                errorCursorColor = CustomTheme.colors.error,
                focusedTrailingIconColor = CustomTheme.colors.iconDefault,
                unfocusedTrailingIconColor = Color.Transparent,
            ),
            shape = RoundedCornerShape(Dimens.cornerRadius),
            singleLine = true,
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        TextField(
            value = title,
            onValueChange = {title = it},
            placeholder = {
                Text(
                    text = "제목",
                    style = CustomTheme.typography.body1,
                    color = CustomTheme.colors.textSecondary
                )
            },
            modifier = Modifier.fillMaxWidth(),
            textStyle = CustomTheme.typography.body1,
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
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
        )
        HorizontalDivider(
            thickness = 1.dp,
            color = CustomTheme.colors.border
        )
        Column(
            modifier = Modifier.weight(1f)
        ) {
            TextField(
                value = content,
                onValueChange = {content = it},
                placeholder = {
                    Text(
                        text = "내용을 입력하세요.",
                        style = CustomTheme.typography.body3,
                        color = CustomTheme.colors.textSecondary
                    )
                },
                modifier = Modifier.fillMaxWidth().height(160.dp),
                textStyle = CustomTheme.typography.body3,
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
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
            )
        }
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
            enabled = validator,
            onClick = {
                addNewPost(
                    NewPost(
                        title = title,
                        content = content,
                        category = Category.fromKor(category) ?: "VEGETABLE",
                        price = price.toInt(),
                        memberCount = quantity.toInt()
                    ),
                    if(files.isNotEmpty()) files.toList() else null
                )
            }
        ) {
            Text(
                text = "등록하기",
                style = CustomTheme.typography.button1,
            )
        }
        PermissionDialog(
            showDialog = showDialog,
            message = "이미지를 업로드하려면 저장소 접근 권한이 필요합니다.",
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

enum class Category(
    val kor: String
) {
    VEGETABLE("채소"),
    FRUIT("과일"),
    MEAT("육류"),
    SEAFOOD( "수산물"),
    DAIRY("유제품"),
    GRAIN("곡물"),
    BEVERAGE("음료"),
    SNACK("과자"),
    CONDIMENT("조미료"),
    FROZEN("냉동식품"),
    PROCESSED("가공식품");

    companion object {
        fun fromKor(value: String): String? {
            return entries.find { it.kor == value }?.name
        }
        fun fromString(value: String): String? {
            return Category.entries.find { it.name == value }?.kor
        }
    }
}