package com.stone.fridge.presentation.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import com.stone.fridge.R
import com.stone.fridge.core.util.Dimens
import com.stone.fridge.ui.theme.CustomTheme
import com.stone.fridge.domain.model.RecipeRaw

@Composable
fun MyRecipe(recipe: RecipeRaw, modifier: Modifier = Modifier, onEvent: (HomeEvent) -> Unit, onClick: () -> Unit){
    var isLiked by remember { mutableStateOf(recipe.liked) }
    Column(
        modifier = modifier.clickable{
            onClick()
        }
    ) {
        if(recipe.imageUrl != null){
            Box{
                AsyncImage(
                    model = recipe.imageUrl,
                    contentDescription = recipe.title,
                    alignment = Alignment.Center,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth().aspectRatio(1f)
                        .clip(shape = RoundedCornerShape(Dimens.cornerRadius))
                )
                IconButton(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    onClick = {
                        onEvent(HomeEvent.ToggleLike(recipe.id, !recipe.liked))
                        isLiked = !isLiked
                    }
                ) {
                    if(isLiked){
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.heart_filled),
                            contentDescription = "like",
                            tint = CustomTheme.colors.iconRed,
                        )
                    }else{
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.heart_filled_inside_default),
                            contentDescription = "like",
                            tint = CustomTheme.colors.iconDefault,
                        )
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier.fillMaxWidth().aspectRatio(1f)
                    .clip(shape = RoundedCornerShape(Dimens.cornerRadius))
                    .background(CustomTheme.colors.surface)
            ){
                IconButton(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    onClick = {
                        onEvent(HomeEvent.ToggleLike(recipe.id, !recipe.liked))
                        isLiked = !isLiked
                    }
                ) {
                    if(isLiked){
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.heart_filled),
                            contentDescription = "like",
                            tint = CustomTheme.colors.iconRed,
                        )
                    }else{
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.heart_filled_inside_default),
                            contentDescription = "like",
                            tint = Color.Unspecified
                        )
                    }
                }
            }
        }
        Spacer(
            modifier = Modifier.height(Dimens.mediumPadding)
        )
        Text(
            text = recipe.title,
            style = CustomTheme.typography.title1,
            color = CustomTheme.colors.textPrimary,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            softWrap = false,
        )
    }
}