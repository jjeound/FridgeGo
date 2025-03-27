package com.example.untitled_capstone.presentation.feature.home.screen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import com.example.untitled_capstone.presentation.feature.home.HomeEvent
import com.example.untitled_capstone.presentation.feature.home.state.RecipeState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeScreen(recipeId: Long, state: RecipeState, onAction: (HomeEvent) -> Unit, navigate: () -> Unit){
//    val recipe = state.recipeItems.find { it.id == recipeId } ?: return
//    Scaffold(
//        containerColor = CustomTheme.colors.onSurface,
//        topBar = {
//            CenterAlignedTopAppBar(
//                modifier = Modifier.padding(Dimens.topBarPadding),
//                navigationIcon = {
//                    IconButton(
//                        onClick = {navigate()}
//                    ) {
//                        Icon(
//                            imageVector = ImageVector.vectorResource(R.drawable.chevron_left),
//                            tint = CustomTheme.colors.iconDefault,
//                            contentDescription = "back",
//                        )
//                    }
//                },
//                title = {
//                    Text(
//                        text = recipe.title,
//                        style = CustomTheme.typography.title1,
//                        color = CustomTheme.colors.textPrimary,
//                        softWrap = true
//                    )
//                },
//                actions = {
//                    IconButton(
//                        onClick = { }
//                    ) {
//                        Icon(
//                            imageVector = ImageVector.vectorResource(R.drawable.more),
//                            tint = CustomTheme.colors.iconDefault,
//                            contentDescription = "more",
//                        )
//                    }
//                },
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = CustomTheme.colors.onSurface
//                )
//            )
//        }
//    ) { innerPadding ->
//        Column(
//            modifier = Modifier.padding(innerPadding).fillMaxSize()
//                .padding(horizontal = Dimens.surfaceHorizontalPadding,
//                    vertical = Dimens.surfaceVerticalPadding),
//            verticalArrangement = Arrangement.spacedBy(Dimens.mediumPadding),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Box(
//                modifier = Modifier.size(300.dp)
//                    .clip(shape = RoundedCornerShape(Dimens.cornerRadius))
//                    .background(CustomTheme.colors.surface),
//                contentAlignment = Alignment.BottomEnd,
//            ){
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
//                Row {
//                    IconButton(
//                        onClick = { }
//                    ) {
//                        Icon(
//                            imageVector = ImageVector.vectorResource(R.drawable.camera),
//                            contentDescription = "like",
//                            tint = CustomTheme.colors.iconDefault,
//                        )
//                    }
//                    IconButton(
//                        onClick = {
//                            onAction(HomeEvent.ToggleLike(recipe.id))
//                        }
//                    ) {
//                        if(recipe.isLiked){
//                            Icon(
//                                imageVector = ImageVector.vectorResource(R.drawable.heart_filled),
//                                contentDescription = "like",
//                                tint = CustomTheme.colors.iconRed,
//                            )
//                        }else{
//                            Icon(
//                                imageVector = ImageVector.vectorResource(R.drawable.heart),
//                                contentDescription = "like",
//                                tint = CustomTheme.colors.iconDefault,
//                            )
//                        }
//                    }
//                }
//            }
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
//            Column {
//                for(step in recipe.steps){
//                    Text(
//                        text = step,
//                        style = CustomTheme.typography.body2,
//                        color = CustomTheme.colors.textPrimary
//                    )
//                }
//            }
//        }
//    }
}