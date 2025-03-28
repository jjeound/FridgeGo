package com.example.untitled_capstone.presentation.feature.home.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.domain.model.RecipeRaw
import com.example.untitled_capstone.presentation.feature.home.HomeEvent
import com.example.untitled_capstone.presentation.feature.home.state.RecipeState
import com.example.untitled_capstone.ui.theme.CustomTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeScreen(state: RecipeState, onAction: (HomeEvent) -> Unit, navigate: () -> Unit){
    Scaffold(
        containerColor = CustomTheme.colors.onSurface,
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.padding(Dimens.topBarPadding),
                navigationIcon = {
                    IconButton(
                        onClick = {navigate()}
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.chevron_left),
                            tint = CustomTheme.colors.iconDefault,
                            contentDescription = "back",
                        )
                    }
                },
                title = {
                    Text(
                        text = state.recipe?.title ?: "",
                        style = CustomTheme.typography.title1,
                        color = CustomTheme.colors.textPrimary,
                        softWrap = true
                    )
                },
                actions = {
                    IconButton(
                        onClick = { }
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.more),
                            tint = CustomTheme.colors.iconDefault,
                            contentDescription = "more",
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CustomTheme.colors.onSurface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).fillMaxSize()
                .padding(horizontal = Dimens.surfaceHorizontalPadding,
                    vertical = Dimens.surfaceVerticalPadding),
            verticalArrangement = Arrangement.spacedBy(Dimens.mediumPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if(state.loading){
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            if(state.recipe != null){
                val recipe = state.recipe
                Box(
                    modifier = Modifier.size(300.dp)
                        .clip(shape = RoundedCornerShape(Dimens.cornerRadius))
                        .background(CustomTheme.colors.surface),
                    contentAlignment = Alignment.BottomEnd,
                ){
//                if (recipe.image != null) {
//                    AsyncImage(
//                        model = recipe.image.toUri(),
//                        contentDescription = recipe.title,
//                        alignment = Alignment.Center,
//                        contentScale = ContentScale.Fit,
//                        modifier = Modifier.size(300.dp)
//                            .clip(shape = RoundedCornerShape(Dimens.cornerRadius))
//                    )
//                }
                    Row {
                        IconButton(
                            onClick = { }
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.camera),
                                contentDescription = "like",
                                tint = CustomTheme.colors.iconDefault,
                            )
                        }
                        IconButton(
                            onClick = {
                                onAction(HomeEvent.ToggleLike(recipe.id, !recipe.liked))
                            }
                        ) {
                            if(recipe.liked){
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.heart_filled),
                                    contentDescription = "like",
                                    tint = CustomTheme.colors.iconRed,
                                )
                            }else{
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.heart),
                                    contentDescription = "like",
                                    tint = CustomTheme.colors.iconDefault,
                                )
                            }
                        }
                    }
                }
//            Text(
//                text = "재료",
//                style = CustomTheme.typography.title1,
//                color = CustomTheme.colors.textPrimary,
//            )
//            Column {
//                for(ingredient in recipe.ingredients){
//                    Text(
//                        text = ingredient,
//                        style = CustomTheme.typography.body2,
//                        color = CustomTheme.colors.textPrimary
//                    )
//                }
//            }
//            Text(
//                text = "레시피",
//                style = CustomTheme.typography.title1,
//                color = CustomTheme.colors.textPrimary,
//            )
                Column {
                    Text(
                        text = recipe.instructions,
                        style = CustomTheme.typography.body2,
                        color = CustomTheme.colors.textPrimary
                    )
                }
            }
        }
    }
}