package com.example.untitled_capstone.feature.shopping.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.feature.shopping.presentation.composable.PostListContainer
import com.example.untitled_capstone.feature.shopping.presentation.state.PostState

@Composable
fun ShoppingScreen(navController: NavHostController, state: PostState) {
    LazyColumn(
        modifier = Modifier.padding(horizontal = Dimens.surfacePadding, vertical = 10.dp)
    ) {
        items(state.posts) { post ->
            Box(
                modifier = Modifier.clickable {
                    navController.navigate(
                        PostNav(
                            post = post
                        )
                    )
                }
            ){
                PostListContainer(post)
            }
        }
    }
}