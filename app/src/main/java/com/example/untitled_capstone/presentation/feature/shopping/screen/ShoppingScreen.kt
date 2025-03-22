package com.example.untitled_capstone.presentation.feature.shopping.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.domain.model.Post
import com.example.untitled_capstone.presentation.feature.shopping.composable.PostListContainer
import com.example.untitled_capstone.presentation.feature.shopping.event.PostAction
import com.example.untitled_capstone.presentation.feature.shopping.state.PostState

@Composable
fun ShoppingScreen(navigate: (Long) -> Unit, state: PostState, onAction: (PostAction) -> Unit) {
    if (state.loading){
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .height(30.dp)
                    .align(Alignment.Center)
            )
        }
    }else{
        LazyColumn(
            modifier = Modifier.padding(horizontal = Dimens.surfaceHorizontalPadding,
                vertical = Dimens.surfaceVerticalPadding),
            verticalArrangement = Arrangement.spacedBy(Dimens.mediumPadding)
        ) {
            items(state.posts) { post ->
                Box(
                    modifier = Modifier.clickable {
                        navigate(post.id)
                    }
                ){
                    PostListContainer(post, onAction = onAction)
                }
            }
        }
    }
}