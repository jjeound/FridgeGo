package com.example.untitled_capstone.presentation.feature.shopping.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.domain.model.Post
import com.example.untitled_capstone.navigation.Screen
import com.example.untitled_capstone.presentation.feature.shopping.composable.PostContainer
import com.example.untitled_capstone.presentation.feature.shopping.event.PostAction
import com.example.untitled_capstone.presentation.feature.shopping.state.PostState
import com.example.untitled_capstone.ui.theme.CustomTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostScreen(id: Long, state: PostState, onAction: (PostAction) -> Unit, navController: NavHostController){
    val post = state.posts.find { it.id == id } ?: return
    Scaffold(
        containerColor = CustomTheme.colors.onSurface,
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(horizontal = Dimens.topBarPadding),
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack()}
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
                        onClick = { navController.navigate(Screen.NotificationNav)}
                    ) {
                        Icon(
                            tint = CustomTheme.colors.iconDefault,
                            imageVector = ImageVector.vectorResource(R.drawable.bell),
                            contentDescription = "notification"
                        )
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
                                    onAction(PostAction.ToggleLike(post.id))
                                }
                            ) {
                                if(post.isLiked){
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.heart_filled),
                                        contentDescription = "like",
                                        tint = CustomTheme.colors.iconRed,
                                    )
                                }else{
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.heart),
                                        contentDescription = "like",
                                        tint = CustomTheme.colors.iconDefault,
                                    )
                                }
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
                                text = "(${post.currentNumOfPeople}/${post.totalNumbOfPeople})",
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
                                text = "${post.currentNumOfPeople}/${post.totalNumbOfPeople}",
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