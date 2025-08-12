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
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
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
import com.stone.fridge.core.model.ModifyPost
import com.stone.fridge.core.model.Post
import com.stone.fridge.core.navigation.currentComposeNavigator
import com.stone.fridge.core.ui.GoPreviewTheme
import com.stone.fridge.core.ui.PermissionDialog
import java.io.File

@Composable
internal fun ModifyPostForm(
    uiState: PostCRUDUiState,
    modifier: Modifier,
    post: Post,
    deletePostImage: (Long, Long) -> Unit,
    modifyPost: (Long, ModifyPost, List<File>) -> Unit,
    onShowSnackbar: suspend (String, String?) -> Unit,
){
    val context = LocalContext.current
    val activity = context as? Activity
    val composeNavigator = currentComposeNavigator
    var isExpandedPeopleMenu by remember { mutableStateOf(false) }
    var isExpandedCategoryMenu by remember { mutableStateOf(false) }
    val menuItemDataInPeople = List(10) { "${it + 1}" }
    val menuItemDataInCategory = Category.entries.map { it.kor }
    var memberCount by remember { mutableStateOf(post.memberCount.toString()) }
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
                        val file = if (filePath != null) {
                            File(filePath)
                        } else {
                            context.copyUriToFile(uri)
                        }
                        file?.let { files.add(it) }
                        Log.d("TargetSDK", "imageUri - selected : $uri")
                    }
                } ?: result.data?.data?.let { uri ->
                    imagesNew.add(uri.toString())
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
        modifier = modifier.fillMaxSize()
            .padding(horizontal = Dimens.surfaceHorizontalPadding,
                vertical = Dimens.surfaceVerticalPadding)
            .imePadding()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })},
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
                    images = imagesNew,
                    onDeleteImage = {
                        imagesNew.removeAt(it)
                        files.removeAt(it)
                    },
                    imagesOld = {
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
                                            post.image!!.find { image == it.imageUrl }!!.imageId
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
            buttonText = "수정하기",
            validator = validator,
            onSubmit = {
                modifyPost(
                    post.id,
                    ModifyPost(
                        title = title,
                        content = content,
                        category = Category.fromKor(category) ?: "VEGETABLE",
                        price = price.toInt(),
                        memberCount = memberCount.toInt()
                    ),
                    files.toList()
                )
                if(imagesForDelete.isNotEmpty()){
                    imagesForDelete.forEach { id ->
                        deletePostImage(post.id, id)
                    }
                }
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

@Preview(showBackground = true)
@Composable
fun ModifyPostFormPreview(){
    GoPreviewTheme {
        ModifyPostForm(
            uiState = PostCRUDUiState.Idle,
            modifier = Modifier.fillMaxSize(),
            post = Post(
                id = 1L,
                title = "감자 공구합니다.",
                price = 10000,
                neighborhood = "남구",
                timeAgo = "2시간 전",
                currentParticipants = 2,
                memberCount = 5,
                liked = false,
                roomActive = true,
                chatRoomId = 1L,
                nickname = "User",
                category = "VEGETABLE",
                image = null,
                profileImageUrl = null,
                content = "This is a sample post content.",
                createdAt = null,
                district = "무거동",
                likeCount = 3
            ),
            deletePostImage = { _, _ -> },
            modifyPost = { _, _, _ -> },
            onShowSnackbar = { _, _ -> }
        )
    }
}