package com.example.untitled_capstone.presentation.feature.post.screen

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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.navigation.Screen
import com.example.untitled_capstone.presentation.feature.fridge.FridgeAction
import com.example.untitled_capstone.presentation.feature.post.composable.PostContainer
import com.example.untitled_capstone.presentation.feature.post.PostEvent
import com.example.untitled_capstone.presentation.feature.post.PostState
import com.example.untitled_capstone.ui.theme.CustomTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailScreen(id: Long, nickname: String, state: PostState, onEvent: (PostEvent) -> Unit, navController: NavHostController){
    var expanded by remember { mutableStateOf(false) }
    var menuItem by remember { mutableStateOf(emptyList<String>()) }
    LaunchedEffect(Unit) {
        onEvent(PostEvent.GetPostById(id))
    }
    LaunchedEffect(state.post) {
        if(state.post != null){
            menuItem = if(nickname == state.post.nickname) listOf("수정", "삭제") else listOf("신고")
        }
    }
    if(state.isLoading){
        CircularProgressIndicator(
            modifier = Modifier.fillMaxSize(),
            color = CustomTheme.colors.primary
        )
    }
    if(state.post != null){
        val post = state.post
        Scaffold(
            containerColor = CustomTheme.colors.onSurface,
            topBar = {
                TopAppBar(
                    modifier = Modifier.padding(horizontal = Dimens.topBarPadding),
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                navController.popBackStack()
                                onEvent(PostEvent.InitState)
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
                            onClick = { }
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.share),
                                tint = CustomTheme.colors.iconDefault,
                                contentDescription = "share"
                            )
                        }
                        IconButton(
                            onClick = {
                                expanded = true
                            }
                        ) {
                            Icon(
                                tint = CustomTheme.colors.iconDefault,
                                imageVector = ImageVector.vectorResource(R.drawable.more),
                                contentDescription = "menu"
                            )
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            containerColor = CustomTheme.colors.textTertiary,
                            shape = RoundedCornerShape(Dimens.cornerRadius),
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
                                        expanded = false
                                        if(menuItem.size == 1){
                                            //신고
                                        }else if(menuItem.size == 2){
                                            when(option){
                                                menuItem[0] -> navController.navigate(Screen.WritingNav)
                                                menuItem[1] -> {
                                                    onEvent(PostEvent.DeletePost(post.id))
                                                    navController.popBackStack()
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
            },
            bottomBar = {
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
                                        onEvent(PostEvent.ToggleLike(post.id))
                                    }
                                ) {
//                                if(post.isLiked){
//                                    Icon(
//                                        imageVector = ImageVector.vectorResource(R.drawable.heart_filled),
//                                        contentDescription = "like",
//                                        tint = CustomTheme.colors.iconRed,
//                                    )
//                                }else{
//                                    Icon(
//                                        imageVector = ImageVector.vectorResource(R.drawable.heart),
//                                        contentDescription = "like",
//                                        tint = CustomTheme.colors.iconDefault,
//                                    )
//                                }
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.heart),
                                        contentDescription = "like",
                                        tint = CustomTheme.colors.iconDefault,
                                    )
                                }
                                VerticalDivider(
                                    modifier = Modifier.padding(horizontal = 6.dp).height(80.dp),
                                    thickness = 1.dp,
                                    color = CustomTheme.colors.textTertiary
                                )
                                Text(
                                    text = post.price.toString(),
                                    style = CustomTheme.typography.title2,
                                    color = CustomTheme.colors.textPrimary,
                                    modifier = Modifier.padding(end = 4.dp)
                                )
                                Text(
                                    text = "(1/${post.memberCount})",
                                    style = CustomTheme.typography.caption2,
                                    color = CustomTheme.colors.textPrimary,
                                    modifier = Modifier.align(Alignment.Bottom)
                                )
                            }
                            Row(
                                modifier = Modifier.height(80.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(
                                    modifier = Modifier.padding(end = 4.dp),
                                    onClick = {

                                    },
                                    shape = RoundedCornerShape(Dimens.cornerRadius),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = CustomTheme.colors.primary,
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
                                    text = "1/${post.memberCount}",
                                    style = CustomTheme.typography.caption2,
                                    color = CustomTheme.colors.textSecondary,
                                )
                            }
                        }
                    }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier.padding(innerPadding)
                    .padding(horizontal = Dimens.surfaceHorizontalPadding,
                        vertical = Dimens.surfaceVerticalPadding),
            ){
                PostContainer(post= post)
            }
        }
    }
}