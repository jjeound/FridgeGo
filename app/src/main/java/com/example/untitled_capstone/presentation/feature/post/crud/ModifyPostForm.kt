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
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
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
import com.example.untitled_capstone.domain.model.Post
import com.example.untitled_capstone.presentation.util.PermissionDialog
import com.example.untitled_capstone.presentation.feature.my.profile.getRealPathFromURI
import com.example.untitled_capstone.ui.theme.CustomTheme
import java.io.File

@Composable
fun ModifyPostForm(
    modifier: Modifier,
    post: Post,
    deletePostImage: (Long, Long) -> Unit,
    modifyPost: (Long, NewPost, List<File>) -> Unit,
){
    val context = LocalContext.current
    var isExpandedPeopleMenu by remember { mutableStateOf(false) }
    var isExpandedCategoryMenu by remember { mutableStateOf(false) }
    val menuItemDataInPeople = List(10) { "${it + 1}" }
    val menuItemDataInCategory = Category.entries.map { it.kor }
    var numbers by remember { mutableStateOf(post.memberCount.toString()) }
    var category by remember { mutableStateOf(Category.fromString(post.category) ?: "채소") }
    var price by remember { mutableStateOf(post.price.toString()) }
    var title by remember { mutableStateOf(post.title) }
    var content by remember { mutableStateOf(post.content) }
    val focusManager = LocalFocusManager.current
    var validator by remember { mutableStateOf(false) }
    val imagesOld = remember(post.image){
        post.image?.map { it.imageUrl }?.toMutableStateList() ?: mutableStateListOf()
    }
    val imagesNew = remember { mutableStateListOf<String>() }
    val imagesForDelete = remember { mutableStateListOf<Long>() }
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
                        imagesNew.add(uri.toString())
                        val filePath = context.getRealPathFromURI(uri)
                        if (filePath != null) {
                            files.add(File(filePath))
                        }
                        Log.d("TargetSDK", "imageUri - selected : $uri")
                    }
                } ?: result.data?.data?.let { uri ->
                    imagesNew.add(uri.toString())
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
    Box(
        modifier = modifier.fillMaxSize()
            .padding(horizontal = Dimens.surfaceHorizontalPadding,
                vertical = Dimens.surfaceVerticalPadding),
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.imePadding().pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },
            verticalArrangement = Arrangement.spacedBy(Dimens.mediumPadding)
        ) {
            item {
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
                            onClick = {
                                when {
                                    ContextCompat.checkSelfPermission(
                                        context,
                                        galleryPermissions[0]
                                    ) == PackageManager.PERMISSION_GRANTED -> {
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
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.camera),
                                contentDescription = "get image",
                                tint = CustomTheme.colors.iconDefault,
                            )
                        }
                    }
                    LazyRow (
                        horizontalArrangement = Arrangement.spacedBy(Dimens.smallPadding)
                    ) {
                        itemsIndexed(imagesNew) { index, image ->
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
                                        imagesNew.removeAt(index)
                                        files.removeAt(index)
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
                        itemsIndexed(imagesOld){ index, image ->
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
                                        imagesOld.removeAt(index)
                                        imagesForDelete.add(
                                            post.image!!.find { image == it.imageUrl }!!.id
                                        )
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
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
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
                                .clickable {
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
                                    text = numbers,
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
                                        )
                                    },
                                    onClick = {
                                        isExpandedPeopleMenu = false
                                        numbers = option
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
                                        )
                                    },
                                    onClick = {
                                        isExpandedCategoryMenu = false
                                        category = option
                                    },
                                )
                            }
                        }
                    }
                }
            }
            item {
                OutlinedTextField(
                    // add validator
                    value = price,
                    onValueChange = { price = it },
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
            item {
                TextField(
                    value = title,
                    onValueChange = { title = it },
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
            item {
                HorizontalDivider(
                    thickness = 1.dp,
                    color = CustomTheme.colors.border
                )
            }
            item {
                Column(
                    modifier = Modifier.wrapContentHeight()
                ) {
                    TextField(
                        value = content,
                        onValueChange = { content = it },
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
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                    )
                }
            }
        }
        LaunchedEffect(content) {
            listState.animateScrollToItem(index = 5)
        }
        Button(
            modifier = Modifier
                .fillMaxWidth().align(Alignment.BottomCenter),
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
                modifyPost(
                    post.id,
                    NewPost(
                        title = title,
                        content = content,
                        category = Category.fromKor(category) ?: "VEGETABLE",
                        price = price.toInt(),
                        memberCount = numbers.toInt()
                    ),
                    files.toList()
                )
                if(imagesForDelete.isNotEmpty()){
                    imagesForDelete.forEach { id ->
                        deletePostImage(post.id, id)
                    }
                }
            }
        ) {
            Text(
                text = "수정하기",
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