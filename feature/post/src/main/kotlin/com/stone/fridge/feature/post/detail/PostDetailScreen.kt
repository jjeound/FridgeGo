package com.stone.fridge.feature.post.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stone.fridge.core.designsystem.Dimens
import com.stone.fridge.core.designsystem.R
import com.stone.fridge.core.designsystem.theme.CustomTheme
import com.stone.fridge.core.model.Post
import com.stone.fridge.core.navigation.currentComposeNavigator
import com.stone.fridge.core.ui.GoPreviewTheme
import com.stone.fridge.feature.post.navigation.PostCRUDRoute
import com.stone.fridge.feature.post.navigation.PostRoute
import com.stone.fridge.feature.post.navigation.ReportRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailScreen(
    viewModel: PostDetailViewModel = hiltViewModel(),
    onShowSnackbar: suspend (String, String?) -> Unit,
    navigateToChattingRoom: (Long, Boolean) -> Unit,
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val post by viewModel.post.collectAsStateWithLifecycle()
    val nickname by viewModel.nickname.collectAsStateWithLifecycle()
    if(uiState == PostDetailUiState.Loading){
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator(
                color = CustomTheme.colors.primary
            )
        }
    } else if (post != null && nickname != null) {
        PostDetailScreenContent(
            uiState = uiState,
            post = post!!,
            nickname = nickname!!,
            deletePost = viewModel::deletePost,
            toggleLike = viewModel::toggleLike,
            closeChatRoom = viewModel::closeChatRoom,
            navigateToChattingRoom = navigateToChattingRoom,
            onShowSnackbar = onShowSnackbar
        )
    }
}

@Composable
private fun PostDetailScreenContent(
    uiState: PostDetailUiState,
    post: Post,
    nickname: String,
    deletePost: (Long) -> Unit,
    toggleLike: (Long) -> Unit,
    closeChatRoom: (Long) -> Unit,
    navigateToChattingRoom: (Long, Boolean) -> Unit,
    onShowSnackbar: suspend (String, String?) -> Unit
){
    var expanded by remember { mutableStateOf(false) }
    val menuItem =  if(nickname == post.nickname){
        listOf("수정", "삭제")
    }else{
        listOf("신고")
    }
    val composeNavigator = currentComposeNavigator
    LaunchedEffect(uiState) {
        if(uiState == PostDetailUiState.Success){
            composeNavigator.navigateAndClearBackStack(PostRoute)
        } else if (uiState is PostDetailUiState.Error){
            onShowSnackbar(uiState.message, null)
        }
    }
    Scaffold(
        containerColor = CustomTheme.colors.onSurface,
        topBar = {
            PostDetailTopBar(
                post = post,
                menuItem = menuItem,
                isExpand = expanded,
                onExpandChange = { expanded = it },
                deletePost = deletePost,
                closeChatRoom = closeChatRoom
            )
        },
        bottomBar = { PostDetailBottomBar(
            post = post,
            navigateToChattingRoom = navigateToChattingRoom,
            toggleLike = toggleLike
        )}
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
                .padding(horizontal = Dimens.surfaceHorizontalPadding,
                    vertical = Dimens.surfaceVerticalPadding),
        ){
            PostContainer(
                post = post,
            )
        }
    }
}

@Composable
fun PostDetailTopBar(
    post: Post,
    menuItem: List<String>,
    isExpand: Boolean,
    onExpandChange: (Boolean) -> Unit,
    deletePost: (Long) -> Unit,
    closeChatRoom: (Long) -> Unit,
){
    val composeNavigator = currentComposeNavigator
    TopAppBar(
        modifier = Modifier.padding(horizontal = Dimens.topBarPadding),
        navigationIcon = {
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
        },
        title = {},
        actions = {
            IconButton(
                onClick = {
                    onExpandChange(true)
                }
            ) {
                Icon(
                    tint = CustomTheme.colors.iconDefault,
                    imageVector = ImageVector.vectorResource(R.drawable.more),
                    contentDescription = "menu"
                )
            }
            DropdownMenu(
                expanded = isExpand,
                onDismissRequest = { onExpandChange(false) },
                containerColor = CustomTheme.colors.onSurface,
                shape = RoundedCornerShape(Dimens.cornerRadius),
                border = BorderStroke(
                    width = 1.dp,
                    color = CustomTheme.colors.borderLight
                )
            ) {
                menuItem.forEach { option ->
                    DropdownMenuItem(
                        modifier = Modifier.height(30.dp).width(90.dp),
                        text = {
                            Text(
                                text = option,
                                style = CustomTheme.typography.caption1,
                                color = CustomTheme.colors.textPrimary,
                            )
                        },
                        onClick = {
                            onExpandChange(false)
                            if(menuItem.size == 1){
                                composeNavigator.navigate(ReportRoute(post.id, true))
                            }else if(menuItem.size == 2){
                                when(option){
                                    menuItem[0] -> {
                                        composeNavigator.navigate(PostCRUDRoute(post))
                                    }
                                    menuItem[1] -> {
                                        closeChatRoom(post.chatRoomId)
                                        deletePost(post.id)
                                    }
                                }
                            }
                        },
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = CustomTheme.colors.onSurface
        )
    )
}

@Composable
fun PostDetailBottomBar(
    post: Post,
    navigateToChattingRoom: (Long, Boolean) -> Unit,
    toggleLike: (Long) -> Unit
){
    BottomAppBar(
        modifier = Modifier.wrapContentHeight(),
        containerColor = CustomTheme.colors.onSurface,
        content = {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = Dimens.largePadding),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    modifier = Modifier.height(80.dp)
                        .padding(vertical = Dimens.largePadding),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    IconButton(
                        onClick = {
                            toggleLike(post.id)
                        }
                    ) {
                        if(post.liked){
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.heart_filled),
                                contentDescription = "like",
                                tint = CustomTheme.colors.iconRed,
                            )
                        }else{
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.heart),
                                contentDescription = "like",
                                tint = CustomTheme.colors.iconDefault
                            )
                        }
                    }
                    VerticalDivider(
                        modifier = Modifier.padding(horizontal = 6.dp).height(80.dp),
                        thickness = 1.dp,
                        color = CustomTheme.colors.textTertiary
                    )
                    Text(
                        text = post.price.toString() + " 원",
                        style = CustomTheme.typography.title2,
                        color = CustomTheme.colors.textPrimary,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                }
                Row(
                    modifier = Modifier.height(80.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        modifier = Modifier.padding(end = 4.dp),
                        onClick = {
                            navigateToChattingRoom(post.chatRoomId, true)
                        },
                        enabled = post.roomActive && post.currentParticipants < post.memberCount,
                        shape = RoundedCornerShape(Dimens.cornerRadius),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CustomTheme.colors.primary,
                            contentColor = CustomTheme.colors.textPrimary,
                            disabledContainerColor = CustomTheme.colors.borderLight,
                        ),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "채팅하기",
                            style = CustomTheme.typography.button1,
                            color = CustomTheme.colors.onPrimary
                        )
                    }
                    Icon(
                        imageVector  = ImageVector.vectorResource(R.drawable.people),
                        contentDescription = "numberOfPeople",
                        tint = CustomTheme.colors.iconDefault,
                    )
                    Text(
                        text = "${post.currentParticipants}/${post.memberCount}",
                        style = CustomTheme.typography.caption2,
                        color = CustomTheme.colors.textSecondary,
                    )
                }
            }
        }
    )
}

@Preview
@Composable
fun PostDetailScreenContentPreview(){
    GoPreviewTheme {
        PostDetailScreenContent(
            uiState = PostDetailUiState.Success,
            post = Post(
                id = 1L,
                title = "Sample Post",
                price = 10000,
                neighborhood = "남구",
                timeAgo = "2시간 전",
                currentParticipants = 2,
                memberCount = 5,
                liked = false,
                roomActive = true,
                chatRoomId = 1L,
                nickname = "User",
                category = "VEGETABLE",
                image = null,
                profileImageUrl = null,
                content = "This is a sample post content.",
                createdAt = null,
                district = "무거동",
                likeCount = 3
            ),
            nickname = "Sample User",
            deletePost = {},
            toggleLike = {},
            closeChatRoom = {},
            navigateToChattingRoom = { _, _ -> },
            onShowSnackbar = { _, _ -> }
        )
    }
}