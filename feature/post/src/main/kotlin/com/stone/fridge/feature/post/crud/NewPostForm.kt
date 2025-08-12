package com.stone.fridge.feature.post.crud

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
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
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.stone.fridge.core.common.copyUriToFile
import com.stone.fridge.core.common.getRealPathFromURI
import com.stone.fridge.core.designsystem.Dimens
import com.stone.fridge.core.designsystem.R
import com.stone.fridge.core.designsystem.theme.CustomTheme
import com.stone.fridge.core.model.NewPost
import com.stone.fridge.core.model.Post
import com.stone.fridge.core.navigation.currentComposeNavigator
import com.stone.fridge.core.ui.GoPreviewTheme
import com.stone.fridge.core.ui.PermissionDialog
import java.io.File
import kotlin.collections.get
import kotlin.collections.toList

@Composable
internal fun NewPostForm(
    uiState: PostCRUDUiState,
    modifier: Modifier,
    addNewPost: (NewPost, List<File>?) -> Unit,
    onShowSnackbar: suspend (String, String?) -> Unit,
){
    val context = LocalContext.current
    val activity = context as? Activity
    val composeNavigator = currentComposeNavigator
    var isExpandedPeopleMenu by remember { mutableStateOf(false) }
    var isExpandedCategoryMenu by remember { mutableStateOf(false) }
    val menuItemDataInPeople = List(10) { "${it + 1}" }
    val menuItemDataInCategory = Category.entries.map { it.kor }
    var memberCount by remember { mutableStateOf("2") }
    var category by remember { mutableStateOf(Category.VEGETABLE.kor) }
    var price by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    var validator by remember { mutableStateOf(false) }
    val images = remember { mutableStateListOf<String>() }
    val showDialog = remember { mutableStateOf(false) }
    val files = remember { mutableStateListOf<File>() }
    val listState = rememberLazyListState()
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
                        val file = if (filePath != null) {
                            File(filePath)
                        } else {
                            context.copyUriToFile(uri)
                        }
                        file?.let { files.add(it) }
                        Log.d("TargetSDK", "imageUri - selected : $uri")
                    }
                } ?: result.data?.data?.let { uri ->
                    images.add(uri.toString())
                    val filePath = context.getRealPathFromURI(uri)
                    val file = if (filePath != null) {
                        File(filePath)
                    } else {
                        context.copyUriToFile(uri)
                    }
                    file?.let { files.add(it) }
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
    LaunchedEffect(uiState) {
        if(uiState == PostCRUDUiState.Success){
            composeNavigator.navigateUp()
        } else if (uiState is PostCRUDUiState.Error) {
            onShowSnackbar(uiState.message, null)
        }
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(
                horizontal = Dimens.surfaceHorizontalPadding,
                vertical = Dimens.surfaceVerticalPadding
            )
            .imePadding()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },
    ) {
        LazyColumn(
            state = listState,
            verticalArrangement = Arrangement.spacedBy(Dimens.mediumPadding)
        ) {
            item {
                ImageInputField(
                    onRequestPermission = {
                        when {
                            ContextCompat.checkSelfPermission(
                                context,
                                galleryPermissions[0]
                            ) == PackageManager.PERMISSION_GRANTED -> {
                                albumLauncher.launch(imageAlbumIntent)
                            }
                            activity != null && shouldShowRequestPermissionRationale(
                                activity,
                                galleryPermissions[0]
                            ) -> {
                                showDialog.value = true
                            }

                            else -> {
                                requestPermissionLauncher.launch(galleryPermissions)
                            }
                        }
                    },
                    images = images,
                    onDeleteImage = {images.removeAt(it)}
                )
            }
            item {
                TypeInputField(
                    modifier = Modifier.fillMaxWidth(),
                    memberCount = memberCount,
                    category = category,
                    isExpandedPeopleMenu = isExpandedPeopleMenu,
                    onPeopleMenuChanged = { isExpandedPeopleMenu = it },
                    isExpandedCategoryMenu = isExpandedCategoryMenu,
                    onCategoryMenuChanged = { isExpandedCategoryMenu = it },
                    menuItemDataInPeople = menuItemDataInPeople,
                    menuItemDataInCategory = menuItemDataInCategory,
                    onMemberCountChange = { memberCount = it },
                    onCategoryChange = { category = it }
                )
            }
            item {
                PriceInputField(
                    price = price,
                    onPriceChange = { price = it },
                    focusManager = focusManager
                )
            }
            item {
                TitleInputField(
                    title = title,
                    onTitleChange = { title = it },
                    focusManager = focusManager
                )
            }
            item {
                HorizontalDivider(
                    thickness = 1.dp,
                    color = CustomTheme.colors.border
                )
            }
            item {
                ContentInputField(
                    modifier = Modifier.wrapContentHeight(),
                    content = content,
                    onContentChange = { content = it },
                    focusManager = focusManager
                )
            }
        }
        LaunchedEffect(content) {
            listState.animateScrollToItem(index = 5)
        }
        SubmitButton(
            buttonText = "등록하기",
            validator = validator,
            onSubmit = {
                addNewPost(
                    NewPost(
                        title = title,
                        content = content,
                        category = Category.fromKor(category) ?: "VEGETABLE",
                        price = price.toInt(),
                        memberCount = memberCount.toInt()
                    ),
                    if(files.isNotEmpty()) files.toList() else null
                )
            }
        )
        PermissionDialog(
            showDialog = showDialog.value,
            message = "저장소 권한이 필요합니다.",
            onEvent = { showDialog.value = false },
            context = context,
            intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        )
    }
}

@Composable
fun ImageInputField(
    onRequestPermission: () -> Unit,
    images: SnapshotStateList<String>,
    onDeleteImage: (Int) -> Unit,
    imagesOld: LazyListScope.() -> Unit = {}
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
        ) {
            IconButton(
                modifier = Modifier
                    .align(Alignment.Center),
                onClick = onRequestPermission
            ) {
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
                            onDeleteImage(index)
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
            imagesOld()
        }
    }
}

@Composable
fun TypeInputField(
    modifier: Modifier,
    memberCount: String,
    category: String,
    isExpandedPeopleMenu: Boolean,
    onPeopleMenuChanged: (Boolean) -> Unit,
    isExpandedCategoryMenu: Boolean,
    onCategoryMenuChanged: (Boolean) -> Unit,
    menuItemDataInPeople: List<String>,
    menuItemDataInCategory: List<String>,
    onMemberCountChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit
){
    Row(
        modifier = modifier
            .padding(Dimens.mediumPadding),
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
        Box {
            Card(
                modifier = Modifier
                    .wrapContentSize()
                    .clickable { onPeopleMenuChanged(true) },
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
                        text = memberCount,
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
                onDismissRequest = {onPeopleMenuChanged(false)},
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
                            )
                        },
                        onClick = {
                            onPeopleMenuChanged(false)
                            onMemberCountChange(option)
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
        Box {
            Card(
                modifier = Modifier
                    .wrapContentSize()
                    .clickable {
                        onCategoryMenuChanged(true)
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
                onDismissRequest = {onCategoryMenuChanged(false)},
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
                            )
                        },
                        onClick = {
                            onCategoryMenuChanged(false)
                            onCategoryChange(option)
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun PriceInputField(
    price: String,
    onPriceChange: (String) -> Unit,
    focusManager: FocusManager = LocalFocusManager.current
){
    OutlinedTextField(
        value = price,
        onValueChange = onPriceChange,
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
            if (price.isNotBlank()) {
                IconButton(
                    onClick = { onPriceChange("") }
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
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() },
            onNext = {
                focusManager.moveFocus(
                    FocusDirection.Down
                )
            }
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        )
    )
}

@Composable
fun TitleInputField(
    title: String,
    onTitleChange: (String) -> Unit,
    focusManager: FocusManager = LocalFocusManager.current
){
    TextField(
        value = title,
        onValueChange = onTitleChange,
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
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() },
            onNext = {
                focusManager.moveFocus(
                    FocusDirection.Down
                )
            }
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next
        )
    )
}

@Composable
fun ContentInputField(
    modifier: Modifier,
    content: String,
    onContentChange: (String) -> Unit,
    focusManager: FocusManager = LocalFocusManager.current
){
    Column(
        modifier = modifier
    ) {
        TextField(
            value = content,
            onValueChange = onContentChange,
            placeholder = {
                Text(
                    text = "내용을 입력하세요.",
                    style = CustomTheme.typography.body3,
                    color = CustomTheme.colors.textSecondary
                )
            },
            modifier = Modifier.fillMaxWidth(),
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
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            )
        )
    }
}

@Composable
fun BoxScope.SubmitButton(
    buttonText: String,
    validator: Boolean,
    onSubmit: () -> Unit,
){
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter),
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
        onClick = onSubmit
    ) {
        Text(
            text = buttonText,
            style = CustomTheme.typography.button1,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NewPostFormPreview(){
    GoPreviewTheme {
        NewPostForm(
            uiState = PostCRUDUiState.Idle,
            modifier = Modifier.fillMaxSize(),
            addNewPost = { _, _ -> },
            onShowSnackbar = { _, _ -> }
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
            return entries.find { it.name == value }?.kor
        }
    }
}