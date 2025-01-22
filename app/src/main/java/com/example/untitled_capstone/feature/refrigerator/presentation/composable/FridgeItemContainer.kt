package com.example.untitled_capstone.feature.refrigerator.presentation.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.untitled_capstone.R
import com.example.untitled_capstone.feature.refrigerator.domain.model.FridgeItem
import com.example.untitled_capstone.ui.theme.CustomTheme

@Composable
fun FridgeItemContainer(item: FridgeItem) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = CustomTheme.colors.onSurface,
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = CustomTheme.elevation.bgPadding)
            .padding(bottom = 10.dp),
    ) {
        Row{
            if (item.image != null) {
                Image(
                    painter = painterResource(item.image),
                    contentDescription = item.name,
                    alignment = Alignment.Center,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(120.dp)
                        .padding(CustomTheme.elevation.itemPadding)
                        .clip(shape = RoundedCornerShape(12.dp))
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(120.dp).padding(CustomTheme.elevation.itemPadding)
                        .clip(shape = RoundedCornerShape(12.dp))
                        .background(CustomTheme.colors.surface)
                )
            }
            Column(
                modifier = Modifier.padding(CustomTheme.elevation.itemPadding),
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
            Column(
                modifier = Modifier.fillMaxHeight().padding(CustomTheme.elevation.itemPadding),
            ){
                Icon(
                    imageVector  = ImageVector.vectorResource(R.drawable.more),
                    contentDescription = "numberOfPeople",
                    tint = CustomTheme.colors.iconDefault,
                )
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.bell_outlined),
                    contentDescription = "like",
                    tint = CustomTheme.colors.iconDefault,
                )
            }
        }
    }
}

