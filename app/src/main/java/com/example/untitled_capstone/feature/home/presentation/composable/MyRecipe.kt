package com.example.untitled_capstone.feature.home.presentation.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import com.example.untitled_capstone.R
import com.example.untitled_capstone.feature.home.domain.model.Recipe
import com.example.untitled_capstone.ui.theme.CustomTheme

@Composable
fun MyRecipe(recipe: Recipe, onClick : () -> Unit){
    Column(
        modifier = Modifier.clickable {
            onClick()
        }
    ) {
        Box(
            modifier = Modifier
                .width(160.dp).height(160.dp)
                .clip(shape = RoundedCornerShape(12.dp))
                .background(CustomTheme.colors.surface)
        ){
            if (recipe.image != null) {
                Image(
                    painter = painterResource(recipe.image),
                    contentDescription = recipe.title,
                    alignment = Alignment.Center,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.width(160.dp).height(160.dp)
                        .clip(shape = RoundedCornerShape(12.dp))
                )
            }
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.heart),
                contentDescription = "like",
                tint = Color.Unspecified,
                modifier = Modifier.align(Alignment.BottomEnd).padding(10.dp)
            )
        }
        Text(
            text = recipe.title,
            fontFamily = CustomTheme.typography.title1.fontFamily,
            fontWeight = CustomTheme.typography.title1.fontWeight,
            fontSize = CustomTheme.typography.title1.fontSize,
            color = CustomTheme.colors.textPrimary,
            modifier = Modifier.padding(top = 4.dp, bottom = CustomTheme.elevation.bgPadding)
        )
    }
}