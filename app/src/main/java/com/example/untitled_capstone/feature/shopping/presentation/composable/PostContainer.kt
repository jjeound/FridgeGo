package com.example.untitled_capstone.feature.shopping.presentation.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.untitled_capstone.R
import com.example.untitled_capstone.feature.shopping.domain.model.Post
import com.example.untitled_capstone.ui.theme.CustomTheme

@Composable
fun PostContainer(post: Post){
    Column(
        modifier = Modifier.fillMaxSize().padding(CustomTheme.elevation.bgPadding)
    ) {
        if (post.image != null) {
            Image(
                painter = painterResource(post.image),
                contentDescription = post.title,
                alignment = Alignment.Center,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxWidth().aspectRatio(1f)
                    .padding(CustomTheme.elevation.itemPadding)
                    .clip(shape = RoundedCornerShape(12.dp))
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .padding(CustomTheme.elevation.itemPadding)
                    .clip(shape = RoundedCornerShape(12.dp))
                    .background(CustomTheme.colors.surface)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(CustomTheme.elevation.itemPadding),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.profile),
                    contentDescription = "profile image",
                    tint = CustomTheme.colors.iconDefault
                )
                Column {
                    Text(
                        text = "닉네임", //post.user.name
                        fontWeight = CustomTheme.typography.button2.fontWeight,
                        fontFamily = CustomTheme.typography.button2.fontFamily,
                        fontSize = CustomTheme.typography.button2.fontSize,
                        color = CustomTheme.colors.textPrimary,
                    )
                    Text(
                        text = post.location,
                        fontWeight = CustomTheme.typography.caption2.fontWeight,
                        fontFamily = CustomTheme.typography.caption2.fontFamily,
                        fontSize = CustomTheme.typography.caption2.fontSize,
                        color = CustomTheme.colors.textSecondary,
                    )
                }
            }
            Text(
                text = "어쩌고 레벨"
            )
        }
        Box(
            modifier = Modifier.height(1.dp).fillMaxWidth().background(CustomTheme.colors.surface)
        )
        Column(
            modifier = Modifier.padding(CustomTheme.elevation.itemPadding)
        ) {
            Text(
                text = post.title,
                fontWeight = CustomTheme.typography.headline3.fontWeight,
                fontFamily = CustomTheme.typography.headline3.fontFamily,
                fontSize = CustomTheme.typography.headline3.fontSize,
                color = CustomTheme.colors.textPrimary,
            )
            Text(
                text = "${post.category} ${post.time}",
                fontWeight = CustomTheme.typography.caption1.fontWeight,
                fontFamily = CustomTheme.typography.caption1.fontFamily,
                fontSize = CustomTheme.typography.caption1.fontSize,
                color = CustomTheme.colors.textSecondary,
            )
            Text(
                text = post.content,
                fontWeight = CustomTheme.typography.body2.fontWeight,
                fontFamily = CustomTheme.typography.body2.fontFamily,
                fontSize = CustomTheme.typography.body2.fontSize,
                color = CustomTheme.colors.textPrimary,
                softWrap = true
            )
            Spacer(
                modifier = Modifier.padding(10.dp)
            )
            Text(
                text = "조회 ${post.views}",
                fontWeight = CustomTheme.typography.caption1.fontWeight,
                fontFamily = CustomTheme.typography.caption1.fontFamily,
                fontSize = CustomTheme.typography.caption1.fontSize,
                color = CustomTheme.colors.textSecondary,
            )
        }
    }
}