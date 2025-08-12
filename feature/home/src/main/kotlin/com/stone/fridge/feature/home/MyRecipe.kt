package com.stone.fridge.feature.home

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.stone.fridge.core.designsystem.Dimens
import com.stone.fridge.core.designsystem.theme.CustomTheme
import com.stone.fridge.core.model.RecipeRaw
import com.stone.fridge.core.designsystem.R
import com.stone.fridge.core.ui.GoPreviewTheme

@Composable
fun MyRecipe(
    recipe: RecipeRaw,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isLiked: Boolean,
    onToggleLike: () -> Unit,
){
    Column(
        modifier = modifier.clickable(
            onClick = onClick
        )
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
                    onClick = onToggleLike
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
                    onClick = onToggleLike
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

@Preview
@Composable
fun MyRecipePreview() {
    GoPreviewTheme {
        MyRecipe(
            recipe = RecipeRaw(
                id = 1L,
                title = "Sample Recipe",
                imageUrl = null,
                liked = true
            ),
            onClick = {},
            isLiked = false,
            onToggleLike = {}
        )
    }
}