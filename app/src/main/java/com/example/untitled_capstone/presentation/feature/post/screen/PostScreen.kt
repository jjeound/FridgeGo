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
import com.example.untitled_capstone.presentation.feature.post.composable.PostListContainer
import com.example.untitled_capstone.presentation.feature.post.PostEvent
import com.example.untitled_capstone.presentation.feature.post.PostState

@Composable
fun PostScreen(navigate: (Long) -> Unit, postItems: LazyPagingItems<PostRaw>, state: PostState, onEvent: (PostEvent) -> Unit) {
    val context = LocalContext.current
    LaunchedEffect(key1 = postItems.loadState) {
        if(postItems.loadState.refresh is LoadState.Error) {
            Toast.makeText(
                context,
                "Error: " + (postItems.loadState.refresh as LoadState.Error).error.message,
                Toast.LENGTH_LONG
            ).show()
        }
    }
    LaunchedEffect(state.error) {
        Log.d("error", state.error ?: "null")
    }
    Box(modifier = Modifier.fillMaxSize()) {
        if(postItems.loadState.refresh is LoadState.Loading || state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            LazyColumn(
                modifier = Modifier.padding(horizontal = Dimens.surfaceHorizontalPadding,
                    vertical = Dimens.surfaceVerticalPadding),
                verticalArrangement = Arrangement.spacedBy(Dimens.mediumPadding)
            ) {
                items(postItems.itemCount) { index ->
                    val post = postItems[index]
                    if(post != null){
                        Box(
                            modifier = Modifier.clickable {
                                navigate(post.id)
                            }
                        ){
                            PostListContainer(post, onEvent = onEvent)
                        }
                    }
                }
                item {
                    if (postItems.loadState.append is LoadState.Loading && postItems.itemCount > 10) {
                        CircularProgressIndicator(modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.CenterHorizontally))
                    }
                }
            }
        }
    }
}