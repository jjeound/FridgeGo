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
import androidx.navigation.NavHostController
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.navigation.Screen
import com.example.untitled_capstone.presentation.feature.shopping.PostViewModel
import com.example.untitled_capstone.presentation.feature.shopping.composable.PostListContainer

@Composable
fun ShoppingScreen(navController: NavHostController, viewModel: PostViewModel) {
    val state by viewModel.state.collectAsState()
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
                        navController.navigate(
                            Screen.PostNav(
                                post = post
                            )
                        )
                    }
                ){
                    PostListContainer(post, onAction = viewModel::onAction)
                }
            }
        }
    }
}