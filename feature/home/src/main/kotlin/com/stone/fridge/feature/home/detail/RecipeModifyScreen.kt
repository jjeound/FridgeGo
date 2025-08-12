package com.stone.fridge.feature.home.detail

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusChanged
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.stone.fridge.core.common.copyUriToFile
import com.stone.fridge.core.common.getRealPathFromURI
import com.stone.fridge.core.designsystem.Dimens
import com.stone.fridge.core.designsystem.theme.CustomTheme
import com.stone.fridge.core.model.Recipe
import com.stone.fridge.core.navigation.currentComposeNavigator
import com.stone.fridge.core.designsystem.R
import com.stone.fridge.core.ui.GoPreviewTheme
import com.stone.fridge.core.ui.PermissionDialog
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeModifyScreen(
    viewModel: RecipeViewModel = hiltViewModel(),
    onShowSnackbar: suspend (String, String?) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val recipe by viewModel.recipe.collectAsStateWithLifecycle()
    if (uiState == RecipeUiState.Loading && recipe != null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator(
                color = CustomTheme.colors.primary
            )
        }
    } else if(recipe != null) {
        RecipeModifyContent(
            recipe = recipe!!,
            uiState = uiState,
            uploadImageThenModifyRecipe = viewModel::uploadImageThenModify,
            onShowSnackbar = onShowSnackbar
        )
    }
}

@Composable
private fun RecipeModifyContent(
    recipe: Recipe,
    uiState: RecipeUiState,
    uploadImageThenModifyRecipe: (Recipe, File?) -> Unit,
    onShowSnackbar: suspend (String, String?) -> Unit,
){
    val composeNavigator = currentComposeNavigator
    val showDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current
    var title by rememberSaveable { mutableStateOf(recipe.title) }
    val ingredients = remember { recipe.ingredients.toMutableStateList() }
    var instructions by rememberSaveable { mutableStateOf(recipe.instructions) }
    var image by rememberSaveable { mutableStateOf(recipe.imageUrl) }
    var imageFile by remember { mutableStateOf<File?>(null) }
    val isValid by remember {
        derivedStateOf {
            title.isNotBlank() &&
                    ingredients.any { it.isNotBlank() } &&
                    instructions.isNotBlank()
        }
    }
    LaunchedEffect(uiState) {
        if(uiState == RecipeUiState.Success){
            composeNavigator.navigateUp()
        } else if  (uiState is RecipeUiState.Error) {
            onShowSnackbar(uiState.message, null)
        }
    }
    Scaffold(
        containerColor = CustomTheme.colors.onSurface,
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.padding(Dimens.topBarPadding),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            composeNavigator.navigateUp()
                        }
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.chevron_left),
                            tint = CustomTheme.colors.iconDefault,
                            contentDescription = "back",
                        )
                    }
                },
                actions = {
                    TextButton(
                        enabled = isValid,
                        onClick = {
                            uploadImageThenModifyRecipe(
                                Recipe(
                                    id = recipe.id,
                                    title = title,
                                    imageUrl = image,
                                    ingredients = ingredients.filter { it.isNotBlank() }.toList(),
                                    instructions = instructions,
                                    liked = recipe.liked
                                ),
                                imageFile
                            )
                        },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = CustomTheme.colors.textPrimary,
                            disabledContentColor = CustomTheme.colors.textSecondary
                        )
                    ) {
                        Text(
                            text = "완료",
                            style = CustomTheme.typography.button1,
                        )
                    }
                },
                title = {
                    Text(
                        text = "레시피",
                        style = CustomTheme.typography.title1,
                        color = CustomTheme.colors.textPrimary,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CustomTheme.colors.onSurface
                )
            )
        }
    ){ innerPadding ->
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding),
            thickness = 1.dp,
            color = CustomTheme.colors.borderLight
        )
        RecipeDetail(
            modifier = Modifier.padding(innerPadding),
            title = title,
            onTitleChange = {title = it},
            onTitleClear = {title = ""},
            ingredients = ingredients,
            onAddIngredient = { ingredients.add("") },
            onIngredientChange = { index, ing -> ingredients[index] = ing },
            onRemoveIngredient = { ingredients.removeAt(it)},
            instructions = instructions,
            onInstructionsChange = { instructions = it },
            image = image,
            onImageChange = { uri, file ->
                image = uri
                imageFile = file
            },
            onDeleteImage = {
                image = null
                imageFile = null
            },
            onRequestPermissionDialog = {
                showDialog.value = true
            }
        )
        PermissionDialog(
            showDialog = showDialog.value,
            message = "이미지를 업로드하려면 저장소 접근 권한이 필요합니다.",
            onEvent = { showDialog.value = false },
            context = context,
            intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        )
    }
}

@Composable
private fun RecipeDetail(
    modifier: Modifier,
    title: String,
    onTitleChange: (String) -> Unit,
    onTitleClear: () -> Unit,
    ingredients: SnapshotStateList<String>,
    onAddIngredient: () -> Unit,
    onIngredientChange: (Int, String) -> Unit,
    onRemoveIngredient: (Int) -> Unit,
    instructions: String,
    onInstructionsChange: (String) -> Unit,
    image: String?,
    onImageChange: (String?, File?) -> Unit,
    onDeleteImage: () -> Unit,
    onRequestPermissionDialog: () -> Unit
){
    val listState = rememberLazyListState()
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val activity = context as? Activity
    val albumLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data
                uri?.let {
                    val filePath = context.getRealPathFromURI(it)
                    val imageFile = filePath?.let { path -> File(path) } ?: context.copyUriToFile(it)
                    onImageChange(it.toString(), imageFile)
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
    LaunchedEffect(ingredients.size) {
        if (ingredients.isNotEmpty()) {
            listState.animateScrollToItem(ingredients.size + 3)
        }
    }
    LazyColumn (
        state = listState,
        modifier = modifier.fillMaxSize()
            .padding(
                horizontal = Dimens.largePadding,
                vertical = Dimens.mediumPadding
            )
            .imePadding()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },
        verticalArrangement = Arrangement.spacedBy(Dimens.largePadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item{
            TitleField(
                title = title,
                onTitleChange = onTitleChange,
                onTitleClear = onTitleClear,
                focusManager = focusManager
            )
        }
        item{
            ImagePickerBox(
                image = image,
                onDelete = onDeleteImage,
                onCheckPermission = {
                    when {
                        ContextCompat.checkSelfPermission(
                            context,
                            galleryPermissions[0]
                        ) == PackageManager.PERMISSION_GRANTED ->  {
                            albumLauncher.launch(imageAlbumIntent)
                        }
                        activity != null && shouldShowRequestPermissionRationale(
                            activity,
                            galleryPermissions[0]
                        ) -> {
                            onRequestPermissionDialog()
                        }
                        else -> {
                            requestPermissionLauncher.launch(galleryPermissions)
                        }
                    }
                }
            )
        }
        item{
            IngredientHeader(
                onAddItem = onAddIngredient
            )
        }
        items(ingredients.size){ index ->
            val ingredient = ingredients[index]
            IngredientField(
                ingredient = ingredient,
                onIngredientChange = {onIngredientChange(index, it)},
                onDelete = {onRemoveIngredient(index)},
                focusManager = focusManager
            )
        }
        item{
            Column{
                Text(
                    modifier = Modifier.padding(vertical = 6.dp),
                    text = "레시피 \uD83D\uDE80",
                    style = CustomTheme.typography.title1,
                    color = CustomTheme.colors.textPrimary,
                )
                HorizontalDivider(
                    thickness = 1.dp,
                    color = CustomTheme.colors.borderLight
                )
            }
        }
        item{
            InstructionsField(
                instructions = instructions,
                onInstructionsChange = onInstructionsChange,
                onFocusChanged = {
                    if(it){
                        scope.launch {
                            listState.animateScrollToItem(ingredients.size + 6)
                        }
                    }
                },
                focusManager = focusManager
            )
        }
    }
}

@Composable
private fun TitleField(
    title: String,
    onTitleChange: (String) -> Unit,
    onTitleClear: () -> Unit,
    focusManager: FocusManager
){
    Column {
        TextField(
            value = title,
            onValueChange = onTitleChange,
            placeholder = {
                Text(
                    text = "제목",
                    style = CustomTheme.typography.title1,
                    color = CustomTheme.colors.textSecondary
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = onTitleClear
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.delete),
                        contentDescription = "delete",
                        tint = CustomTheme.colors.iconDefault,
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            textStyle = CustomTheme.typography.title1,
            colors = TextFieldDefaults.colors(
                focusedTextColor = CustomTheme.colors.textPrimary,
                unfocusedTextColor = CustomTheme.colors.textPrimary,
                focusedContainerColor = CustomTheme.colors.onSurface,
                unfocusedContainerColor = CustomTheme.colors.onSurface,
                cursorColor = CustomTheme.colors.textPrimary,
                focusedIndicatorColor = CustomTheme.colors.border,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTrailingIconColor = CustomTheme.colors.iconDefault,
                unfocusedTrailingIconColor = Color.Transparent,
            ),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
        )
        HorizontalDivider(
            thickness = 1.dp,
            color = CustomTheme.colors.borderLight
        )
    }
}

@Composable
private fun ImagePickerBox(
    image: String?,
    onDelete: () -> Unit,
    onCheckPermission: () -> Unit,
){
    Box(
        modifier = Modifier
            .size(100.dp)
            .clip(shape = RoundedCornerShape(Dimens.cornerRadius))
            .background(CustomTheme.colors.surface),
        contentAlignment = Alignment.BottomEnd,
    ){
        if (image != null) {
            AsyncImage(
                model = image,
                contentDescription = "recipe image",
                alignment = Alignment.Center,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(shape = RoundedCornerShape(Dimens.cornerRadius))
            )
        }
        Column(
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = onDelete
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.close),
                    contentDescription = "delete",
                    tint = CustomTheme.colors.iconDefault,
                )
            }
            IconButton(
                onClick = onCheckPermission
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.camera),
                    contentDescription = "like",
                    tint = CustomTheme.colors.iconDefault,
                )
            }
        }
    }
}

@Composable
private fun IngredientHeader(
    onAddItem: () -> Unit,
){
    Column{
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(vertical = 6.dp),
                text = "재료 \uD83D\uDCCC",
                style = CustomTheme.typography.title1,
                color = CustomTheme.colors.textPrimary,
            )
            IconButton(
                onClick = onAddItem
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.add),
                    contentDescription = "add",
                    tint = CustomTheme.colors.iconDefault,
                )
            }
        }
        HorizontalDivider(
            thickness = 1.dp,
            color = CustomTheme.colors.borderLight
        )
    }
}

@Composable
private fun IngredientField(
    ingredient: String,
    onIngredientChange: (String) -> Unit,
    onDelete: () -> Unit,
    focusManager: FocusManager
){
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "✅",
            style = CustomTheme.typography.body2,
            color = CustomTheme.colors.textPrimary,
        )
        TextField(
            value = ingredient,
            onValueChange = onIngredientChange,
            placeholder = {
                Text(
                    text = "재료 추가..//",
                    style = CustomTheme.typography.body3,
                    color = CustomTheme.colors.textSecondary
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = onDelete
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.delete),
                        contentDescription = "delete",
                        tint = CustomTheme.colors.iconDefault,
                    )
                }
            },
            textStyle = CustomTheme.typography.body2,
            colors = TextFieldDefaults.colors(
                focusedTextColor = CustomTheme.colors.textPrimary,
                unfocusedTextColor = CustomTheme.colors.textPrimary,
                focusedContainerColor = CustomTheme.colors.onSurface,
                unfocusedContainerColor = CustomTheme.colors.onSurface,
                cursorColor = CustomTheme.colors.textPrimary,
                focusedIndicatorColor = CustomTheme.colors.border,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTrailingIconColor = CustomTheme.colors.iconDefault,
                unfocusedTrailingIconColor = Color.Transparent,
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions (
                onDone = { focusManager.clearFocus() },
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )
        IconButton(
            onClick = onDelete
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.close),
                contentDescription = "delete",
                tint = CustomTheme.colors.iconDefault,
            )
        }
    }
}

@Composable
private fun InstructionsField(
    instructions: String,
    onInstructionsChange: (String) -> Unit,
    onFocusChanged: (Boolean) -> Unit,
    focusManager: FocusManager
){
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight().onFocusChanged(
                onFocusChanged ={onFocusChanged(it.isFocused)}
            ),
        value = instructions,
        onValueChange = onInstructionsChange,
        placeholder = {
            Text(
                text = "레시피..//",
                style = CustomTheme.typography.body3,
                color = CustomTheme.colors.textSecondary
            )
        },
        textStyle = CustomTheme.typography.body2,
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
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
    )
}

@Preview
@Composable
fun RecipeModifyScreenContentPreview() {
    GoPreviewTheme {
        RecipeModifyContent(
            recipe = Recipe(
                id = 1L,
                title = "제목",
                imageUrl = null,
                ingredients = listOf("재료1", "재료2"),
                instructions = "1. 레시피 설명",
                liked = false
            ),
            uiState = RecipeUiState.Idle,
            uploadImageThenModifyRecipe = { _, _ -> },
            onShowSnackbar = { _, _ -> }
        )
    }
}