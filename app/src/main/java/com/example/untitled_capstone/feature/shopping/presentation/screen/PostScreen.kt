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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
                modifier = Modifier.padding(CustomTheme.elevation.bgPadding),
                navigationIcon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.chevron_left),
                        tint = CustomTheme.colors.iconDefault,
                        contentDescription = "back",
                        modifier = Modifier.clickable {
                            navController.popBackStack()
                        }
                    )
                },
                title = {},
                actions = {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.share),
                        tint = CustomTheme.colors.iconDefault,
                        contentDescription = "search"
                    )
                    Icon(
                        modifier = Modifier.padding(start = 24.dp),
                        tint = CustomTheme.colors.iconDefault,
                        imageVector = ImageVector.vectorResource(R.drawable.bell),
                        contentDescription = "alarm"
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CustomTheme.colors.onSurface
                )
            )
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.height(80.dp)
                    .padding(horizontal = CustomTheme.elevation.bgPadding,
                        vertical = 10.dp),
                containerColor = CustomTheme.colors.onSurface,
                content = {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.heart),
                                contentDescription = "like",
                                tint = CustomTheme.colors.iconDefault,
                                modifier = Modifier.padding(start = 10.dp, end = 6.dp)
                            )
                            Spacer(
                                modifier = Modifier
                                    .width(1.dp)
                                    .fillMaxHeight()
                                    .background(CustomTheme.colors.textTertiary)
                            )
                            Text(
                                text = post.price.toString(),
                                fontFamily = CustomTheme.typography.title2.fontFamily,
                                fontWeight = CustomTheme.typography.title2.fontWeight,
                                fontSize = CustomTheme.typography.title2.fontSize,
                                color = CustomTheme.colors.textPrimary,
                                modifier = Modifier.padding(start = 6.dp)
                            )
                            Text(
                                text = "(${post.currentNumOfPeople}/${post.totalNumbOfPeople})",
                                fontFamily = CustomTheme.typography.caption2.fontFamily,
                                fontWeight = CustomTheme.typography.caption2.fontWeight,
                                fontSize = CustomTheme.typography.caption2.fontSize,
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
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = CustomTheme.colors.primary,
                                ),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "채팅하기",
                                    fontFamily = CustomTheme.typography.button1.fontFamily,
                                    fontWeight = CustomTheme.typography.button1.fontWeight,
                                    fontSize = CustomTheme.typography.button1.fontSize,
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
                                fontWeight = CustomTheme.typography.caption2.fontWeight,
                                fontFamily = CustomTheme.typography.caption2.fontFamily,
                                fontSize = CustomTheme.typography.caption2.fontSize,
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
       ){
           PostContainer(post= post)
       }
    }

}