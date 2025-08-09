package com.stone.fridge.feature.post.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.stone.fridge.core.designsystem.Dimens
import com.stone.fridge.core.designsystem.R
import com.stone.fridge.core.designsystem.theme.CustomTheme
import com.stone.fridge.core.model.Post
import com.stone.fridge.core.navigation.currentComposeNavigator
import com.stone.fridge.feature.post.crud.Category
import com.stone.fridge.feature.post.navigation.PostProfileRoute

@Composable
fun PostContainer(
    post: Post
){
    val composeNavigator = currentComposeNavigator
    val category = Category.fromString(post.category) ?: "채소"
    val pagerState = rememberPagerState(pageCount = {
        post.image?.size ?: 1
    })
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Dimens.mediumPadding)
    ){
        if (post.image != null && post.image?.isNotEmpty() == true) {
            HorizontalPager(state = pagerState){ page ->
                val image = post.image!![page].imageUrl
                AsyncImage(
                    model = image,
                    contentDescription = post.title,
                    alignment = Alignment.Center,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth().aspectRatio(1f)
                        .clip(shape = RoundedCornerShape(Dimens.cornerRadius))
                )
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
                    .padding(Dimens.smallPadding)
            ) {
                repeat(post.image!!.size) { index ->
                    val isSelected = pagerState.currentPage == index
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(if (isSelected) 10.dp else 8.dp)
                            .clip(CircleShape)
                            .background(
                                if (isSelected) CustomTheme.colors.border
                                else CustomTheme.colors.borderLight
                            )
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(shape = RoundedCornerShape(Dimens.cornerRadius))
                    .background(CustomTheme.colors.surface)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth().clickable{
                composeNavigator.navigate(PostProfileRoute(post.nickname))
            },
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row {
                if(post.profileImageUrl != null){
                    AsyncImage(
                        modifier = Modifier.size(36.dp).clip(CircleShape),
                        model = post.profileImageUrl,
                        contentScale = ContentScale.Crop,
                        contentDescription = "profile image",
                    )
                }else{
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.profile),
                        contentDescription = "profile image",
                        tint = CustomTheme.colors.iconDefault
                    )
                }
                Spacer(
                    modifier = Modifier.width(Dimens.smallPadding)
                )
                Column {
                    Text(
                        text = post.nickname,
                        style = CustomTheme.typography.button2,
                        color = CustomTheme.colors.textPrimary,
                    )
                    Text(
                        text = post.neighborhood,
                        style = CustomTheme.typography.caption2,
                        color = CustomTheme.colors.textSecondary,
                    )
                }
            }
//            Text(
//                text = "level"
//            )
        }
        HorizontalDivider(
            thickness = 1.dp,
            color = CustomTheme.colors.surface
        )
        Column{
            Text(
                text = post.title,
                style = CustomTheme.typography.headline3,
                color = CustomTheme.colors.textPrimary,
            )
            Text(
                text = "$category ${post.timeAgo}",
                style = CustomTheme.typography.caption1,
                color = CustomTheme.colors.textSecondary,
            )
            Spacer(
                modifier = Modifier.height(Dimens.mediumPadding)
            )
            Text(
                modifier = Modifier.weight(1f),
                text = post.content,
                style = CustomTheme.typography.body2,
                color = CustomTheme.colors.textPrimary,
                softWrap = true
            )
            Spacer(
                modifier = Modifier.height(Dimens.mediumPadding)
            )
//            Text(
//                text = "조회 100",
//                style = CustomTheme.typography.caption1,
//                color = CustomTheme.colors.textSecondary,
//            )
        }
    }
}