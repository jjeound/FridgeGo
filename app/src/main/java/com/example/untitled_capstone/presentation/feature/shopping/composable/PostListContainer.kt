package com.example.untitled_capstone.presentation.feature.shopping.composable

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.domain.model.Post
import com.example.untitled_capstone.presentation.feature.shopping.event.PostAction
import com.example.untitled_capstone.ui.theme.CustomTheme
import androidx.core.net.toUri

@Composable
fun PostListContainer(post: Post, onAction: (PostAction) -> Unit){
    Card(
        colors = CardDefaults.cardColors(
            containerColor = CustomTheme.colors.onSurface,
        ),
        shape = RoundedCornerShape(Dimens.cornerRadius),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(Dimens.mediumPadding)
        ){
            if (post.image.isNotEmpty()){
                AsyncImage(
                    model = post.image[0]!!.toUri(),
                    contentDescription = post.title,
                    alignment = Alignment.Center,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(80.dp)
                        .clip(shape = RoundedCornerShape(Dimens.cornerRadius))
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(shape = RoundedCornerShape(Dimens.cornerRadius))
                        .background(CustomTheme.colors.surface)
                )
            }
            Spacer(
                modifier = Modifier.width(Dimens.largePadding)
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ){
                Text(
                    text = post.title,
                    style = CustomTheme.typography.title1,
                    color = CustomTheme.colors.textPrimary,
                    maxLines = 1,
                )
                Text(
                    text = post.content,
                    style = CustomTheme.typography.body3,
                    color = CustomTheme.colors.textPrimary,
                    maxLines = 1,
                )
                Row {
                    Text(
                        text = post.location,
                        style = CustomTheme.typography.caption2,
                        color = CustomTheme.colors.textSecondary,
                    )
                    Spacer(
                        modifier = Modifier.width(3.dp)
                    )
                    Text(
                        text = post.time,
                        style = CustomTheme.typography.caption2,
                        color = CustomTheme.colors.textSecondary,
                    )
                }
            }
            Row(
                modifier = Modifier.height(80.dp),
                verticalAlignment = Alignment.Bottom
            ){
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
                IconButton(
                    modifier = Modifier.then(Modifier.size(24.dp)),
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
                    }else {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.heart),
                            contentDescription = "like",
                            tint = CustomTheme.colors.iconDefault,
                        )
                    }
                }
                Text(
                    text = "${post.likes}",
                    style = CustomTheme.typography.caption2,
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
            id = 1,
            title = "title",
            content = "caption",
            image = emptyList(),
            location = "무거동",
            time = "2시간 전",
            totalNumbOfPeople = 5,
            currentNumOfPeople = 1,
            likes = 0,
            isLiked = false,
            price = 2000,
            views = 0,
            category = "식료품"
        ),
        onAction = {}
    )
}