package com.example.untitled_capstone.presentation.feature.home.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.domain.model.Recipe
import com.example.untitled_capstone.navigation.Screen
import com.example.untitled_capstone.presentation.feature.home.composable.MyRecipe
import com.example.untitled_capstone.presentation.feature.home.composable.SetTaste
import com.example.untitled_capstone.presentation.feature.home.state.MyRecipeState
import com.example.untitled_capstone.ui.theme.CustomTheme

@Composable
fun HomeScreen(state: MyRecipeState, navController: NavHostController) {
    Column(
        modifier = Modifier.padding(horizontal = Dimens.surfaceHorizontalPadding, vertical = Dimens.surfaceVerticalPadding)
    ) {
        SetTaste()
        Spacer(modifier = Modifier.height(Dimens.largePadding))
        Card (
            colors = CardDefaults.cardColors(
                containerColor = CustomTheme.colors.onSurface,
            ),
            shape = RoundedCornerShape(Dimens.cornerRadius),
            modifier = Modifier.fillMaxWidth()
        ){
            Column(
                modifier = Modifier.padding(Dimens.largePadding),
                verticalArrangement = Arrangement.spacedBy(Dimens.largePadding)
            ) {
                Text(
                    text = "My 레시피",
                    style = CustomTheme.typography.title1,
                    color = CustomTheme.colors.textPrimary,
                    modifier = Modifier.fillMaxWidth()
                )
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    state = rememberLazyGridState(),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                ) {
                    if(!state.isLoading){
                        items( state.recipeItems,){ item ->
                            MyRecipe(item, onClick = {
                                navController.navigate(
                                    Screen.RecipeNav(
                                        recipe = item
                                    )
                                )
                            })
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(state = MyRecipeState(
        recipeItems = listOf(
            Recipe(
                title = "title1",
                image = R.drawable.ic_launcher_background,
                ingredients = emptyList(),
                steps = emptyList(),
                isLiked = false
            ),
            Recipe(
                title = "title1",
                image = R.drawable.ic_launcher_background,
                ingredients = emptyList(),
                steps = emptyList(),
                isLiked = false
            ),
            Recipe(
                title = "title1",
                image = null,
                ingredients = emptyList(),
                steps = emptyList(),
                isLiked = false
            ),
            Recipe(
                title = "title1",
                image = null,
                ingredients = emptyList(),
                steps = emptyList(),
                isLiked = false
            )
        )
    ), navController = rememberNavController()
    )
}
