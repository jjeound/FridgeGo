package com.stone.fridge.feature.post.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.stone.fridge.core.designsystem.Dimens
import com.stone.fridge.core.designsystem.R
import com.stone.fridge.core.designsystem.theme.CustomTheme
import com.stone.fridge.core.model.Keyword
import com.stone.fridge.core.model.PostRaw
import com.stone.fridge.core.navigation.currentComposeNavigator
import com.stone.fridge.core.ui.GoPreviewTheme
import com.stone.fridge.feature.post.PostListContainer
import com.stone.fridge.feature.post.navigation.PostDetailRoute
import com.stone.fridge.feature.post.navigation.PostRoute
import kotlinx.coroutines.flow.MutableStateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostSearchScreen(
    viewModel: PostSearchViewModel = hiltViewModel(),
    onShowSnackbar: suspend (String, String?) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchResult = viewModel.searchResult.collectAsLazyPagingItems()
    val searchedKeywords by viewModel.keywords.collectAsStateWithLifecycle()
    var searchKeyword by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    var showResult by remember { mutableStateOf(false) }
    PostSearchScreenContent(
        uiState = uiState,
        searchKeyword = searchKeyword,
        onKeywordChange = { searchKeyword = it },
        searchResult = searchResult,
        showResult = showResult,
        onFucusChange = { isFocused ->
            if (isFocused){
                showResult = false
            }
        },
        searchedKeywords = searchedKeywords,
        toggleLike = viewModel::toggleLike,
        deleteSearchHistory = viewModel::deleteSearchHistory,
        deleteAllSearchHistory = viewModel::deleteAllSearchHistory,
        onShowSnackbar = onShowSnackbar,
        onSearch = {
            focusManager.clearFocus()
            if(searchKeyword.isNotBlank()){
                viewModel.searchPost(searchKeyword)
                showResult = true
            }
        },
        onClickSearchHistory = { keyword ->
            focusManager.clearFocus()
            viewModel.searchPost(keyword)
            showResult = true
        }
    )
}

@Composable
private fun PostSearchScreenContent(
    uiState: SearchUiState,
    searchKeyword: String,
    onKeywordChange: (String) -> Unit,
    searchResult: LazyPagingItems<PostRaw>,
    showResult: Boolean,
    onFucusChange: (Boolean) -> Unit ,
    searchedKeywords: List<Keyword>,
    toggleLike: (Long) -> Unit,
    deleteSearchHistory: (String) -> Unit,
    deleteAllSearchHistory: () -> Unit,
    onShowSnackbar: suspend (String, String?) -> Unit,
    onSearch: () -> Unit,
    onClickSearchHistory: (String) -> Unit,
){
    val composeNavigator = currentComposeNavigator
    LaunchedEffect(uiState) {
        if(uiState is SearchUiState.Error) {
            onShowSnackbar(uiState.message, null)
        }
    }
    Scaffold(
        containerColor = CustomTheme.colors.surface,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth().padding(
                        vertical = Dimens.largePadding
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = Dimens.largePadding,
                            horizontal = Dimens.topBarPadding,
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Dimens.mediumPadding)
                ) {
                    IconButton(
                        onClick = {
                            composeNavigator.navigateAndClearBackStack(PostRoute)
                        }
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.chevron_left),
                            tint = CustomTheme.colors.iconDefault,
                            contentDescription = "back",
                        )
                    }
                    TextField(
                        modifier = Modifier
                            .weight(1f).height(48.dp)
                            .onFocusChanged {
                                onFucusChange(it.isFocused)
                            },
                        value = searchKeyword,
                        onValueChange = onKeywordChange,
                        placeholder = {
                            Text(
                                text = "검색",
                                style = CustomTheme.typography.caption1,
                                color = CustomTheme.colors.textSecondary
                            )
                        },
                        textStyle = CustomTheme.typography.button2,
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = CustomTheme.colors.textPrimary,
                            unfocusedTextColor = CustomTheme.colors.textPrimary,
                            focusedContainerColor = CustomTheme.colors.borderLight,
                            unfocusedContainerColor = CustomTheme.colors.borderLight,
                            cursorColor = CustomTheme.colors.textPrimary,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(Dimens.cornerRadius),
                        singleLine = true,
                        maxLines = 1,
                        keyboardActions = KeyboardActions(onDone = { onSearch() })
                    )
                    Box(
                        modifier = Modifier.size(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(modifier = Modifier.clickable {
                            composeNavigator.navigateAndClearBackStack(PostRoute)
                        },
                            text = "닫기",
                            style = CustomTheme.typography.button1,
                            color = CustomTheme.colors.textPrimary,
                        )
                    }
                }
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding)
                .padding(
                    horizontal = Dimens.surfaceHorizontalPadding,
                    vertical = Dimens.surfaceVerticalPadding
                )
                .background(CustomTheme.colors.surface),
        ) {
            if (showResult) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimens.mediumPadding),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "검색 결과",
                        style = CustomTheme.typography.button2,
                        color = CustomTheme.colors.textPrimary,
                    )
                }
                if (searchResult.itemCount == 0) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "검색 결과가 없습니다.",
                            style = CustomTheme.typography.body1,
                            color = CustomTheme.colors.textPrimary,
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(Dimens.mediumPadding)
                    ) {
                        items(searchResult.itemCount) { index ->
                            val post = searchResult[index]
                            if (post != null) {
                                Box(
                                    modifier = Modifier.clickable {
                                        composeNavigator.navigate(PostDetailRoute(post.id))
                                    }
                                ) {
                                    PostListContainer(
                                        post = post,
                                        toggleLike = toggleLike
                                    )
                                }
                            }
                        }
                        item {
                            if (searchResult.loadState.append is LoadState.Loading && searchResult.itemCount > 10) {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentWidth(Alignment.CenterHorizontally)
                                )
                            }
                        }
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Dimens.mediumPadding),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "최근 검색",
                            style = CustomTheme.typography.button2,
                            color = CustomTheme.colors.textPrimary,
                        )
                        Text(
                            modifier = Modifier.clickable {
                                if (searchedKeywords.isNotEmpty()) {
                                    deleteAllSearchHistory()
                                }
                            },
                            text = "전체 삭제",
                            style = CustomTheme.typography.button2,
                            color = CustomTheme.colors.textSecondary,
                        )
                    }
                    Spacer(
                        modifier = Modifier.height(Dimens.mediumPadding)
                    )
                    if (uiState == SearchUiState.Loading) {
                        CircularProgressIndicator(
                            color = CustomTheme.colors.primary
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    vertical = Dimens.mediumPadding,
                                    horizontal = 6.dp
                                ),
                            verticalArrangement = Arrangement.spacedBy(Dimens.mediumPadding)
                        ) {
                            items(searchedKeywords.size) { index ->
                                val keyword = searchedKeywords[index]
                                Row(
                                    modifier = Modifier.fillMaxWidth().clickable{
                                        onClickSearchHistory(keyword.keyword)
                                    },
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.history),
                                        contentDescription = "search",
                                        tint = CustomTheme.colors.iconDefault
                                    )
                                    Spacer(
                                        modifier = Modifier.width(Dimens.mediumPadding)
                                    )
                                    Text(
                                        modifier = Modifier.weight(1f),
                                        text = keyword.keyword,
                                        style = CustomTheme.typography.body1,
                                        color = CustomTheme.colors.textPrimary,
                                    )
                                    IconButton(
                                        onClick = {
                                            deleteSearchHistory(keyword.keyword)
                                        },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(R.drawable.close),
                                            contentDescription = "close",
                                            tint = CustomTheme.colors.iconDefault
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PostSearchScreenContentPreview(){
    GoPreviewTheme {
        PostSearchScreenContent(
            uiState = SearchUiState.Idle,
            searchKeyword = "",
            onKeywordChange = { },
            searchResult = MutableStateFlow(PagingData.empty<PostRaw>()).collectAsLazyPagingItems(),
            showResult = false,
            onFucusChange = {},
            searchedKeywords = listOf(
                Keyword("동네"),
                Keyword("맛집"),
                Keyword("카페")
            ),
            toggleLike = {},
            deleteSearchHistory = {},
            deleteAllSearchHistory = {},
            onShowSnackbar = { _, _ -> },
            onSearch = {},
            onClickSearchHistory = {}
        )
    }
}