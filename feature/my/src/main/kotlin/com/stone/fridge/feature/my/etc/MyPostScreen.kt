package com.stone.fridge.feature.my.etc

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.stone.fridge.core.designsystem.Dimens
import com.stone.fridge.core.designsystem.R
import com.stone.fridge.core.designsystem.theme.CustomTheme
import com.stone.fridge.core.navigation.currentComposeNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPostScreen(
    viewModel: MyPostViewModel = hiltViewModel(),
    navigateToPostDetail: (Long) -> Unit,
){
    val posts = viewModel.posts.collectAsLazyPagingItems()
    val composeNavigator = currentComposeNavigator
    Scaffold(
        containerColor = CustomTheme.colors.surface,
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.padding(Dimens.topBarPadding),
                title = {
                    Text(
                        text = "나의 게시물",
                        style = CustomTheme.typography.title1,
                        color = CustomTheme.colors.textPrimary,
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {composeNavigator.navigateUp()}
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.chevron_left),
                            tint = CustomTheme.colors.iconSelected,
                            contentDescription = "back",
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CustomTheme.colors.surface
                )
            )
        }
    ){ innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = CustomTheme.colors.borderLight
            )
            Box(
                modifier = Modifier.padding(horizontal = Dimens.surfaceHorizontalPadding,
                    vertical = Dimens.surfaceVerticalPadding)
            ){
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(Dimens.mediumPadding)
                ) {
                    items(posts.itemCount) { index ->
                        val post = posts[index]
                        if(post != null){
                            Box(
                                modifier = Modifier.clickable {
                                    navigateToPostDetail(post.id)
                                }
                            ){
                                PostListContainer(
                                    post = post,
                                    toggleLike = viewModel::toggleLike
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
    }
}