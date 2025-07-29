package com.stone.fridge.presentation.feature.post

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.stone.fridge.core.util.Dimens
import com.stone.fridge.domain.model.PostRaw
import com.stone.fridge.navigation.Screen
import com.stone.fridge.ui.theme.CustomTheme

@Composable
fun PostScreen(
    uiState: PostUiState,
    postPagingData: LazyPagingItems<PostRaw>,
    navigate: (Screen) -> Unit,
    toggleLike: (Long) -> Unit,
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = postPagingData.loadState) {
        if(postPagingData.loadState.refresh is LoadState.Error) {
            Toast.makeText(
                context,
                "Error: " + (postPagingData.loadState.refresh as LoadState.Error).error.message,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if(postPagingData.loadState.refresh is LoadState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = CustomTheme.colors.primary
            )
        } else if(uiState == PostUiState.NoLocation) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "동네 설정이 필요합니다.",
                color = CustomTheme.colors.textPrimary,
                style = CustomTheme.typography.body1
            )
        } else {
            LazyColumn(
                modifier = Modifier.padding(horizontal = Dimens.surfaceHorizontalPadding,
                    vertical = Dimens.surfaceVerticalPadding),
                verticalArrangement = Arrangement.spacedBy(Dimens.mediumPadding)
            ) {
                items(postPagingData.itemCount) { index ->
                    val post = postPagingData[index]
                    if(post != null){
                        Box(
                            modifier = Modifier.clickable {
                                navigate(
                                    Screen.PostDetailNav(
                                        post.id
                                    )
                                )
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
                    if (postPagingData.loadState.append is LoadState.Loading && postPagingData.itemCount > 10) {
                        CircularProgressIndicator(modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.CenterHorizontally))
                    }
                }
            }
        }
    }
}