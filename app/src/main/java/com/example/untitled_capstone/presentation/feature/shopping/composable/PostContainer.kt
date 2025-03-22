package com.example.untitled_capstone.presentation.feature.shopping.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
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
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.domain.model.Post
import com.example.untitled_capstone.ui.theme.CustomTheme
import androidx.core.net.toUri

@Composable
fun PostContainer(post: Post){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Dimens.mediumPadding)
    ){
        if (post.image.isNotEmpty()) {
            AsyncImage(
                model = post.image[0]!!.toUri(), //수정 해야 함
                contentDescription = post.title,
                alignment = Alignment.Center,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxWidth().aspectRatio(1f)
                    .clip(shape = RoundedCornerShape(Dimens.cornerRadius))
            )
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
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.profile),
                    contentDescription = "profile image",
                    tint = CustomTheme.colors.iconDefault
                )
                Spacer(
                    modifier = Modifier.width(Dimens.smallPadding)
                )
                Column {
                    Text(
                        text = "닉네임", //post.user.name
                        style = CustomTheme.typography.button2,
                        color = CustomTheme.colors.textPrimary,
                    )
                    Text(
                        text = post.location,
                        style = CustomTheme.typography.caption2,
                        color = CustomTheme.colors.textSecondary,
                    )
                }
            }
            Text(
                text = "어쩌고 레벨"
            )
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
                text = "${post.category} ${post.time}",
                style = CustomTheme.typography.caption1,
                color = CustomTheme.colors.textSecondary,
            )
            Spacer(
                modifier = Modifier.height(Dimens.mediumPadding)
            )
            Text(
                text = post.content,
                style = CustomTheme.typography.body2,
                color = CustomTheme.colors.textPrimary,
                softWrap = true
            )
            Spacer(
                modifier = Modifier.height(Dimens.mediumPadding)
            )
            Text(
                text = "조회 ${post.views}",
                style = CustomTheme.typography.caption1,
                color = CustomTheme.colors.textSecondary,
            )
        }
    }
}