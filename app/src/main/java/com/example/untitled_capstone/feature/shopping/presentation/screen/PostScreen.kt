package com.example.untitled_capstone.feature.shopping.presentation.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.feature.notification.presentation.screen.NotificationNav
import com.example.untitled_capstone.feature.shopping.domain.model.Post
import com.example.untitled_capstone.feature.shopping.presentation.composable.PostContainer
import com.example.untitled_capstone.ui.theme.CustomTheme
import kotlinx.serialization.Serializable

@Serializable
data class PostNav(
    val post: Post
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostScreen(post: Post, navController: NavHostController){
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
                        onClick = { navController.navigate(NotificationNav)}
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
                modifier = Modifier.height(80.dp)
                    .padding(horizontal = Dimens.largePadding,
                        vertical = Dimens.mediumPadding),
                containerColor = CustomTheme.colors.onSurface,
                content = {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            IconButton(
                                onClick = { }
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.heart),
                                    contentDescription = "like",
                                    tint = CustomTheme.colors.iconDefault,
                                )
                            }
                            VerticalDivider(
                                modifier = Modifier.padding(horizontal = 6.dp),
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