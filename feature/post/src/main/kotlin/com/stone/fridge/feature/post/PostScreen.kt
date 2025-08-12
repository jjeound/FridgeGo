package com.stone.fridge.feature.post

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.stone.fridge.core.designsystem.Dimens
import com.stone.fridge.core.designsystem.theme.CustomTheme
import com.stone.fridge.core.model.PostRaw
import com.stone.fridge.core.navigation.currentComposeNavigator
import com.stone.fridge.core.ui.GoPreviewTheme
import com.stone.fridge.feature.post.navigation.PostDetailRoute
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun PostScreen(
    viewModel: PostViewModel = hiltViewModel(),
    isUnread: Boolean,
    navigateToNotification: () -> Unit,
    onShowSnackbar: suspend (String, String?) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val posts = viewModel.posts.collectAsLazyPagingItems()
    val location by viewModel.location.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        if(location != null){
            PostTopBar(
                location = location,
                isUnread = isUnread,
                navigateToNotification = navigateToNotification,
            )
            PostScreenContent(
                uiState = uiState,
                posts = posts,
                toggleLike = viewModel::toggleLike,
                onShowSnackbar = onShowSnackbar
            )
        } else {
            Box(
                modifier = Modifier.weight(1f)
            ){
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "동네 설정이 필요합니다.",
                    color = CustomTheme.colors.textPrimary,
                    style = CustomTheme.typography.body1
                )
            }
        }
    }
}

@Composable
private fun PostScreenContent(
    uiState: PostUiState,
    posts: LazyPagingItems<PostRaw>,
    toggleLike: (Long) -> Unit,
    onShowSnackbar: suspend (String, String?) -> Unit
){
    val context = LocalContext.current
    val composeNavigator = currentComposeNavigator
    LaunchedEffect(key1 = posts.loadState) {
        if(posts.loadState.refresh is LoadState.Error) {
            Toast.makeText(
                context,
                "Error: " + (posts.loadState.refresh as LoadState.Error).error.message,
                Toast.LENGTH_LONG
            ).show()
        }
    }
    LaunchedEffect(uiState) {
        if(uiState is PostUiState.Error) {
            onShowSnackbar(uiState.message, null)
        }
    }
    if(posts.loadState.refresh is LoadState.Loading) {
        Box(
            modifier = Modifier.fillMaxSize()
        ){
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = CustomTheme.colors.primary
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.padding(horizontal = Dimens.surfaceHorizontalPadding,
                vertical = Dimens.surfaceVerticalPadding),
            verticalArrangement = Arrangement.spacedBy(Dimens.mediumPadding)
        ) {
            items(posts.itemCount) { index ->
                val post = posts[index]
                if(post != null){
                    Box(
                        modifier = Modifier.clickable {
                            composeNavigator.navigate(PostDetailRoute(post.id))
                        }
                    ){
                        PostListContainer(
                            post = post,
                            toggleLike = toggleLike
                        )
                    }
                }
            }
            item {
                if (posts.loadState.append is LoadState.Loading && posts.itemCount > 10) {
                    CircularProgressIndicator(modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.CenterHorizontally))
                }
            }
        }
    }
}

@Preview
@Composable
fun PostScreenContentPreview() {
    GoPreviewTheme {
        PostScreenContent(
            uiState = PostUiState.Success,
            posts = MutableStateFlow(PagingData.empty<PostRaw>()).collectAsLazyPagingItems(),
            toggleLike = {},
            onShowSnackbar = { _, _ -> }
        )
    }
}