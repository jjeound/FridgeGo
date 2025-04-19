package com.example.untitled_capstone.presentation.feature.post.screen

import android.util.Log
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.domain.model.PostRaw
import com.example.untitled_capstone.navigation.Screen
import com.example.untitled_capstone.presentation.feature.post.composable.PostListContainer
import com.example.untitled_capstone.presentation.feature.post.PostEvent
import com.example.untitled_capstone.ui.theme.CustomTheme

@Composable
fun PostScreen(
    postPagingData: LazyPagingItems<PostRaw>,
    onEvent: (PostEvent) -> Unit
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
        } else {
            LazyColumn(
                modifier = Modifier.padding(horizontal = Dimens.surfaceHorizontalPadding,
                    vertical = Dimens.surfaceVerticalPadding),
                verticalArrangement = Arrangement.spacedBy(Dimens.mediumPadding)
            ) {
                items(postPagingData.itemCount) { index ->
                    val post = postPagingData[index]
                    Log.d("postScreen", "post: $post")
                    if(post != null){
                        Box(
                            modifier = Modifier.clickable {
                                onEvent(PostEvent.NavigateUp(
                                    Screen.PostDetailNav(
                                        post.id
                                    )
                                ))
                            }
                        ){
                            PostListContainer(post, onEvent = onEvent)
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