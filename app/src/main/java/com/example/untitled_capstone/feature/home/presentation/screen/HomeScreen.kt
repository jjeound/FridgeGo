package com.example.untitled_capstone.feature.home.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.feature.home.domain.model.Recipe
import com.example.untitled_capstone.feature.home.presentation.HomeViewModel
import com.example.untitled_capstone.feature.home.presentation.composable.MyRecipe
import com.example.untitled_capstone.feature.home.presentation.composable.SetTaste
import com.example.untitled_capstone.feature.home.presentation.state.MyRecipeState
import com.example.untitled_capstone.ui.theme.CustomTheme

@Composable
fun HomeScreen(state: MyRecipeState, navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(Dimens.surfacePadding)
    ) {
        SetTaste()
        Card (
            colors = CardDefaults.cardColors(
                containerColor = CustomTheme.colors.onSurface,
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().padding(top = 25.dp)
        ){
            Text(
                text = "My 레시피",
                fontSize = CustomTheme.typography.title1.fontSize,
                fontWeight = CustomTheme.typography.title1.fontWeight,
                fontFamily = CustomTheme.typography.title1.fontFamily,
                color = CustomTheme.colors.textPrimary,
                modifier = Modifier.fillMaxWidth().padding(20.dp)
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                state = rememberLazyGridState(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(50.dp),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp)
            ) {
                if(!state.isLoading){
                    items( state.recipeItems,){ item ->
                        MyRecipe(item, onClick = {
                            navController.navigate(RecipeNav(
                                recipe = item
                            ))
                        })
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
