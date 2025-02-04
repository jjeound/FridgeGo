package com.example.untitled_capstone.feature.refrigerator.presentation.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.feature.refrigerator.domain.model.FridgeItem
import com.example.untitled_capstone.ui.theme.CustomTheme

@Composable
fun FridgeItemContainer(item: FridgeItem) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = CustomTheme.colors.onSurface,
        ),
        shape = RoundedCornerShape(Dimens.cornerRadius),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(Dimens.onSurfacePadding),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens.onSurfacePadding)
            ) {
                if (item.image != null) {
                    Image(
                        painter = painterResource(item.image),
                        contentDescription = item.name,
                        alignment = Alignment.Center,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(80.dp)
                            .clip(shape = RoundedCornerShape(Dimens.cornerRadius))
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(shape = RoundedCornerShape(Dimens.onSurfacePadding))
                            .background(CustomTheme.colors.surface)
                    )
                }
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ){
                    Text(
                        text = item.name,
                        fontWeight = CustomTheme.typography.title1.fontWeight,
                        fontFamily = CustomTheme.typography.title1.fontFamily,
                        fontSize = CustomTheme.typography.title1.fontSize,
                        color = CustomTheme.colors.textPrimary,
                        maxLines = 1,
                    )
                    Text(
                        text = item.expirationDate,
                        fontWeight = CustomTheme.typography.body3.fontWeight,
                        fontFamily = CustomTheme.typography.body3.fontFamily,
                        fontSize = CustomTheme.typography.body3.fontSize,
                        color = CustomTheme.colors.textPrimary,
                        maxLines = 1,
                    )
                }
            }
            Column(
                modifier = Modifier.height(80.dp),
                verticalArrangement = Arrangement.SpaceBetween,
            ){
                Icon(
                    imageVector  = ImageVector.vectorResource(R.drawable.more),
                    contentDescription = "numberOfPeople",
                    tint = CustomTheme.colors.iconDefault,
                    modifier = Modifier.clickable {
                        //TODO
                    }
                )
                if(item.notification){
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.bell_selected),
                        contentDescription = "like",
                        tint = CustomTheme.colors.iconSelected,
                        modifier = Modifier.clickable {
                            //TODO
                        }
                    )
                } else {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.bell_outlined),
                        contentDescription = "like",
                        tint = CustomTheme.colors.iconDefault,
                        modifier = Modifier.clickable {
                            //TODO
                        }
                    )
                }
            }
        }
    }
}

