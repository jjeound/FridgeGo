package com.stone.fridge.feature.home

import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.stone.fridge.core.designsystem.Dimens
import com.stone.fridge.core.designsystem.theme.CustomTheme
import com.stone.fridge.core.model.RecipeRaw
import com.stone.fridge.core.designsystem.R
import com.stone.fridge.core.navigation.currentComposeNavigator
import com.stone.fridge.core.ui.GoPreviewTheme
import com.stone.fridge.feature.home.navigation.RecipeNav
import kotlinx.coroutines.flow.MutableStateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    isUnread: Boolean,
    shouldShowBottomSheet: Boolean,
    hideBottomSheet: () -> Unit,
    navigateToNotification: () -> Unit,
    onShowSnackbar: suspend (String, String?) -> Unit,
) {
    val homeUiState by viewModel.homeUIState.collectAsStateWithLifecycle()
    val aiUIState by viewModel.aiUIState.collectAsStateWithLifecycle()
    val recipeItems = viewModel.recipePagingData.collectAsLazyPagingItems()
    val aiResponse by viewModel.aiResponse.collectAsStateWithLifecycle()
    val tastePref by viewModel.tastePref.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier
            .fillMaxSize()
    ){
        HomeTopBar(
            isUnread = isUnread,
            navigateToNotification = navigateToNotification
        )
        if(homeUiState == HomeUiState.Loading){
            Box(
                modifier = Modifier.weight(1f)
            ){
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = CustomTheme.colors.primary
                )
            }
        } else {
            HomeContent(
                homeUiState = homeUiState,
                aiUIState = aiUIState,
                recipeItems = recipeItems,
                aiResponse = aiResponse,
                tastePref = tastePref,
                shouldShowBottomSheet = shouldShowBottomSheet,
                hideBottomSheet = hideBottomSheet,
                addRecipe = viewModel::addRecipe,
                toggleLike = viewModel::toggleLike,
                setTastePreference = viewModel::setTastePreference,
                getAIRecipe = viewModel::getAIRecipe,
                onShowSnackbar = onShowSnackbar
            )
        }
    }
}

@Composable
private fun HomeContent(
    homeUiState: HomeUiState,
    aiUIState: AIUIState,
    recipeItems: LazyPagingItems<RecipeRaw>,
    aiResponse: List<String>,
    tastePref: String?,
    shouldShowBottomSheet: Boolean,
    hideBottomSheet: () -> Unit,
    addRecipe: (String) -> Unit,
    toggleLike: (Long, Boolean) -> Unit,
    setTastePreference: (String) -> Unit,
    getAIRecipe: () -> Unit,
    onShowSnackbar: suspend (String, String?) -> Unit,
){
    val focusManager = LocalFocusManager.current
    var text by remember { mutableStateOf(tastePref ?: "") }
    LaunchedEffect(homeUiState) {
        if(homeUiState is HomeUiState.Error) {
            onShowSnackbar(homeUiState.message, null)
        } else if  (homeUiState is HomeUiState.Success) {
            onShowSnackbar(homeUiState.message, null)
        }
    }
    LaunchedEffect(aiUIState) {
        if(aiUIState is AIUIState.Error) {
            onShowSnackbar(aiUIState.message, null)
        }
    }
    Column(
        modifier = Modifier.padding(
            horizontal = Dimens.surfaceHorizontalPadding)
            .padding(top = Dimens.surfaceVerticalPadding)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })}
    ) {
        TasteTextField(
            text = text,
            onValueChange = { text = it },
            setTastePreference = setTastePreference
        )
        Spacer(modifier = Modifier.height(Dimens.largePadding))
        RecipeBox(
            homeUiState = homeUiState,
            recipeItems = recipeItems,
            addRecipe = addRecipe,
            toggleLike = toggleLike,
        )
        if(shouldShowBottomSheet){
            ChatBotBottomSheet(
                aiUIState = aiUIState,
                aiResponse = aiResponse,
                addRecipe = addRecipe,
                getAIRecipe = getAIRecipe,
                hideBottomSheet = hideBottomSheet
            )
        }
    }
}

@Composable
private fun RecipeBox(
    homeUiState: HomeUiState,
    recipeItems: LazyPagingItems<RecipeRaw>,
    addRecipe: (String) -> Unit,
    toggleLike: (Long, Boolean) -> Unit,
){
    val scrollState = rememberLazyGridState()
    val composeNavigator = currentComposeNavigator
    LaunchedEffect(key1 = recipeItems.loadState) {
        if(recipeItems.loadState.refresh is LoadState.Error) {
            Log.d("error", (recipeItems.loadState.refresh as LoadState.Error).error.message.toString())
        }
    }
    Card (
        colors = CardDefaults.cardColors(
            containerColor = CustomTheme.colors.onSurface,
        ),
        shape = RoundedCornerShape(Dimens.cornerRadius),
        modifier = Modifier.fillMaxWidth()
    ){
        Column(
            modifier = Modifier.padding(horizontal = Dimens.largePadding)
                .padding(vertical = Dimens.largePadding),
            verticalArrangement = Arrangement.spacedBy(Dimens.largePadding)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(Dimens.smallPadding),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "My 레시피",
                    style = CustomTheme.typography.title1,
                    color = CustomTheme.colors.textPrimary,
                )
                IconButton(
                    modifier = Modifier.then(Modifier.size(24.dp)),
                    onClick = {
                        addRecipe("[수정해 주세요.]\n[재료] - 재료를 추가해 주세요.\n[레시피] 1. 앞에 숫자를 붙혀 주세요.")
                    }
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.plus),
                        contentDescription = "add_recipe",
                        tint = CustomTheme.colors.iconDefault
                    )
                }
            }
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                if(recipeItems.loadState.refresh is LoadState.Loading || homeUiState == HomeUiState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = CustomTheme.colors.primary
                    )
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        state = scrollState,
                        verticalArrangement = Arrangement.spacedBy(Dimens.hugePadding),
                        horizontalArrangement = Arrangement.spacedBy(Dimens.hugePadding),
                    ) {
                        items( recipeItems.itemCount){ index ->
                            val recipe = recipeItems[index]
                            if(recipe != null){
                                var isLiked by remember { mutableStateOf(recipe.liked) }
                                MyRecipe(
                                    recipe = recipe,
                                    modifier = Modifier.fillMaxWidth().padding(Dimens.smallPadding),
                                    isLiked = isLiked,
                                    onClick = {
                                        composeNavigator.navigate(RecipeNav(recipe.id))
                                    } ,
                                    onToggleLike = {
                                        toggleLike(recipe.id, !recipe.liked)
                                        isLiked = !isLiked
                                    }
                                )
                            }
                        }
                        item {
                            if (recipeItems.loadState.append is LoadState.Loading && recipeItems.itemCount > 10) {
                                CircularProgressIndicator(modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.CenterHorizontally))
                            }
                        }
                    }
                    if(recipeItems.loadState.refresh != LoadState.Loading && recipeItems.itemCount == 0){
                        AsyncImage(
                            modifier = Modifier.fillMaxWidth(),
                            model = R.drawable.home_banner,
                            contentDescription = "home_banner",
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun HomeTopbarPreview(){
    GoPreviewTheme {
        HomeTopBar(
            isUnread = true,
            navigateToNotification = {}
        )
    }
}

@Preview
@Composable
fun HomeContentPreview() {
    GoPreviewTheme {
        HomeContent(
            homeUiState = HomeUiState.Idle,
            aiUIState = AIUIState.Idle,
            recipeItems = MutableStateFlow(PagingData.empty<RecipeRaw>()).collectAsLazyPagingItems(),
            aiResponse = emptyList(),
            tastePref = "고기를 좋아해",
            shouldShowBottomSheet = false,
            hideBottomSheet = {},
            addRecipe = {},
            toggleLike = {_, _ -> },
            setTastePreference = {},
            getAIRecipe = {},
            onShowSnackbar = { _, _ -> }
        )
    }
}