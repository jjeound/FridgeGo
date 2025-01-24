package com.example.untitled_capstone.feature.shopping.presentation.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.feature.shopping.domain.model.Post
import com.example.untitled_capstone.ui.theme.CustomTheme

@Composable
fun PostListContainer(post: Post){
    Card(
        colors = CardDefaults.cardColors(
            containerColor = CustomTheme.colors.onSurface,
        ),
        shape = RoundedCornerShape(Dimens.cornerRadius),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
    ) {
        Row{
            if (post.image != null) {
                Image(
                    painter = painterResource(post.image),
                    contentDescription = post.title,
                    alignment = Alignment.Center,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(120.dp)
                        .padding(Dimens.onSurfacePadding)
                        .clip(shape = RoundedCornerShape(12.dp))
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(120.dp).padding(Dimens.onSurfacePadding)
                        .clip(shape = RoundedCornerShape(12.dp))
                        .background(CustomTheme.colors.surface)
                )
            }
            Column(
                modifier = Modifier.weight(1f).padding(Dimens.onSurfacePadding),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ){
                Text(
                    text = post.title,
                    fontWeight = CustomTheme.typography.title1.fontWeight,
                    fontFamily = CustomTheme.typography.title1.fontFamily,
                    fontSize = CustomTheme.typography.title1.fontSize,
                    color = CustomTheme.colors.textPrimary,
                    maxLines = 1,
                )
                Text(
                    text = post.content,
                    fontWeight = CustomTheme.typography.body3.fontWeight,
                    fontFamily = CustomTheme.typography.body3.fontFamily,
                    fontSize = CustomTheme.typography.body3.fontSize,
                    color = CustomTheme.colors.textPrimary,
                    maxLines = 1,
                )
                Row {
                    Text(
                        text = post.location,
                        fontWeight = CustomTheme.typography.caption2.fontWeight,
                        fontFamily = CustomTheme.typography.caption2.fontFamily,
                        fontSize = CustomTheme.typography.caption2.fontSize,
                        color = CustomTheme.colors.textSecondary,
                        modifier = Modifier.padding(end = 3.dp)
                    )
                    Text(
                        text = post.time,
                        fontWeight = CustomTheme.typography.caption2.fontWeight,
                        fontFamily = CustomTheme.typography.caption2.fontFamily,
                        fontSize = CustomTheme.typography.caption2.fontSize,
                        color = CustomTheme.colors.textSecondary,
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxHeight().padding(Dimens.onSurfacePadding),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom
            ){
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
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.heart),
                    contentDescription = "like",
                    tint = CustomTheme.colors.iconDefault,
                )
                Text(
                    text = "${post.likes}",
                    fontWeight = CustomTheme.typography.caption2.fontWeight,
                    fontFamily = CustomTheme.typography.caption2.fontFamily,
                    fontSize = CustomTheme.typography.caption2.fontSize,
                    color = CustomTheme.colors.textSecondary,
                )
            }
        }
    }
}

@Preview(showBackground = false)
@Composable
fun PostListContainerPreview(){
    PostListContainer(
        post = Post(
            title = "title",
            content = "caption",
            image = R.drawable.ic_launcher_background,
            location = "무거동",
            time = "2시간 전",
            totalNumbOfPeople = 5,
            currentNumOfPeople = 1,
            likes = 0,
            isLiked = false,
            price = 2000,
            views = 0,
            category = "식료품"
        )
    )
}