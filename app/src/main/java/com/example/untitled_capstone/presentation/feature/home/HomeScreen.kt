package com.example.untitled_capstone.presentation.feature.home

import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.domain.model.RecipeRaw
import com.example.untitled_capstone.navigation.Screen
import com.example.untitled_capstone.presentation.feature.main.MainViewModel
import com.example.untitled_capstone.ui.theme.CustomTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    mainViewModel: MainViewModel,
    uiState: HomeUiState,
    recipeItems: LazyPagingItems<RecipeRaw>,
    aiResponse: List<String>,
    tastePref: String?,
    onEvent: (HomeEvent) -> Unit,
    navigate: (Screen) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )
    val scrollState = rememberLazyGridState()
    val isExpanded = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    LaunchedEffect(sheetState) {
        snapshotFlow { sheetState.currentValue }
            .collect { value ->
                isExpanded.value = (value == SheetValue.Expanded)
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
        TasteTextField(tastePref, onEvent)
        Spacer(modifier = Modifier.height(Dimens.largePadding))
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
                Text(
                    text = "My 레시피",
                    style = CustomTheme.typography.title1,
                    color = CustomTheme.colors.textPrimary,
                    modifier = Modifier.fillMaxWidth()
                )
                LaunchedEffect(key1 = recipeItems.loadState) {
                    if(recipeItems.loadState.refresh is LoadState.Error) {
                        Log.d("error", (recipeItems.loadState.refresh as LoadState.Error).error.message.toString())
                    }
                }
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    if(recipeItems.loadState.refresh is LoadState.Loading || uiState == HomeUiState.Loading) {
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
                                val item = recipeItems[index]
                                if(item != null){
                                    MyRecipe(recipe = item ,modifier = Modifier
                                        .fillMaxWidth().padding(Dimens.smallPadding), onEvent = onEvent){
                                        navigate(
                                            Screen.RecipeNav(item.id),
                                        )
                                    }
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
        if(mainViewModel.showBottomSheet){
            ModalBottomSheet(
                modifier = Modifier.fillMaxSize(),
                sheetState = sheetState,
                onDismissRequest = { mainViewModel.hideBottomSheet()},
                containerColor = Color.White,
            ) {
                ChatBot(
                    uiState = uiState,
                    aiResponse = aiResponse,
                    onEvent = onEvent,
                    isExpanded = isExpanded.value,
                    expandSheet = {
                        scope.launch {
                            sheetState.expand()
                        }
                    }
                )
            }
        }
    }
}
