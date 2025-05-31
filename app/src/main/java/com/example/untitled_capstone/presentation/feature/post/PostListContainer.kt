package com.example.untitled_capstone.presentation.feature.post

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.ui.theme.CustomTheme
import com.example.untitled_capstone.domain.model.PostRaw

@Composable
fun PostListContainer(
    post: PostRaw,
    toggleLike: (Long) -> Unit,
){
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
            if (post.imageUrls.isNotEmpty()){
                AsyncImage(
                    model = post.imageUrls[0],
                    contentDescription = post.title,
                    alignment = Alignment.Center,
                    contentScale = ContentScale.Crop,
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
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ){
                Text(
                    text = post.title,
                    style = CustomTheme.typography.title1,
                    color = CustomTheme.colors.textPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = post.price.toString() + "원",
                    style = CustomTheme.typography.body3,
                    color = CustomTheme.colors.textPrimary,
                    maxLines = 1,
                )
                Row {
                    Text(
                        text = post.neighborhood,
                        style = CustomTheme.typography.caption2,
                        color = CustomTheme.colors.textSecondary,
                    )
                    Spacer(
                        modifier = Modifier.width(3.dp)
                    )
                    Text(
                        text = post.timeAgo,
                        style = CustomTheme.typography.caption2,
                        color = CustomTheme.colors.textSecondary,
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.Bottom
                ){
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
                    IconButton(
                        modifier = Modifier.then(Modifier.size(24.dp)),
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
                        }else {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.heart),
                                contentDescription = "like",
                                tint = CustomTheme.colors.iconDefault
                            )
                        }
                    }
                    Text(
                        text = "${post.likeCount}",
                        style = CustomTheme.typography.caption2,
                        color = CustomTheme.colors.textSecondary,
                    )
                }
            }
        }
    }
}
