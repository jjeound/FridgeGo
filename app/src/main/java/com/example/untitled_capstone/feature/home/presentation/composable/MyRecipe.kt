package com.example.untitled_capstone.feature.home.presentation.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.feature.home.domain.model.Recipe
import com.example.untitled_capstone.ui.theme.CustomTheme

@Composable
fun MyRecipe(recipe: Recipe, onClick : () -> Unit){
    Column(
        modifier = Modifier.wrapContentSize().clickable {
            onClick()
        }
    ) {
        if(recipe.image != null){
            Box{
                Image(
                    painter = painterResource(recipe.image),
                    contentDescription = recipe.title,
                    alignment = Alignment.Center,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(130.dp)
                        .clip(shape = RoundedCornerShape(Dimens.cornerRadius))
                )
                IconButton(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    onClick = {  }
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.heart),
                        contentDescription = "like",
                        tint = Color.Unspecified,
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .clip(shape = RoundedCornerShape(Dimens.cornerRadius))
                    .background(CustomTheme.colors.surface)
            ){
                IconButton(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    onClick = {  }
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.heart),
                        contentDescription = "like",
                        tint = Color.Unspecified,
                    )
                }
            }
        }
        Spacer(
            modifier = Modifier.height(Dimens.smallPadding)
        )
        Text(
            text = recipe.title,
            style = CustomTheme.typography.title1,
            color = CustomTheme.colors.textPrimary,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
}