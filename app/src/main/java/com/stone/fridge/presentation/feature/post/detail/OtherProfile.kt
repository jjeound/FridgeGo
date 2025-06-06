package com.stone.fridge.presentation.feature.post.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import com.stone.fridge.R
import com.stone.fridge.core.util.Dimens
import com.stone.fridge.domain.model.Profile
import com.stone.fridge.navigation.Screen
import com.stone.fridge.presentation.feature.my.profile.levelToKor
import com.stone.fridge.ui.theme.CustomTheme

@Composable
fun OtherProfile(
    profile: Profile,
    navigate: (Screen) -> Unit,
    isMe: Boolean,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Dimens.mediumPadding),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(
                vertical = Dimens.mediumPadding
            ),
            horizontalArrangement = Arrangement.spacedBy(Dimens.mediumPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            profile.imageUrl?.let {
                AsyncImage(
                    modifier = Modifier.size(80.dp).clip(
                        CircleShape
                    ),
                    model = it,
                    contentScale = ContentScale.Crop,
                    contentDescription = "trust level",
                )
            } ?: Icon(
                modifier = Modifier.size(80.dp),
                imageVector = ImageVector.vectorResource(R.drawable.profile),
                contentDescription = "profile image",
                tint = CustomTheme.colors.iconDefault)
            Text(
                text = profile.nickname ?: "User",
                style = CustomTheme.typography.title1,
                color = CustomTheme.colors.textPrimary,
            )
        }
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = CustomTheme.colors.borderLight,
            thickness = 1.dp
        )
        Text(
            text = levelToKor(profile.trustLevel),
            style = CustomTheme.typography.title1,
            color = CustomTheme.colors.textPrimary,
        )
        Spacer(
            modifier = Modifier.weight(1f)
        )
//        Box(
//            modifier = Modifier.fillMaxWidth(),
//            contentAlignment = Alignment.Center
//        ){
//            Text(
//                modifier = Modifier.padding(Dimens.mediumPadding),
//                text = "평가하기",
//                style = CustomTheme.typography.body1,
//                color = CustomTheme.colors.textPrimary,
//            )
//        }
        if(!isMe){
            Box(
                modifier = Modifier.fillMaxWidth().clickable{
                    navigate(Screen.ReportNav(profile.id, false))
                },
                contentAlignment = Alignment.Center
            ){
                Text(
                    modifier = Modifier.padding(Dimens.mediumPadding),
                    text = "신고하기",
                    style = CustomTheme.typography.body1,
                    color = CustomTheme.colors.iconRed,
                )
            }
        }
    }
}