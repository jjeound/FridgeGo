package com.example.untitled_capstone.presentation.feature.home.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.navigation.Screen
import com.example.untitled_capstone.presentation.feature.home.composable.MyRecipe
import com.example.untitled_capstone.presentation.feature.home.composable.SetTaste
import com.example.untitled_capstone.presentation.feature.home.event.HomeAction
import com.example.untitled_capstone.presentation.feature.home.state.MyRecipeState
import com.example.untitled_capstone.ui.theme.CustomTheme

@Composable
fun HomeScreen(state: MyRecipeState, onAction: (HomeAction) -> Unit, navigate: (Long) -> Unit) {
    Column(
        modifier = Modifier.padding(
            horizontal = Dimens.surfaceHorizontalPadding)
            .padding(top = Dimens.surfaceVerticalPadding)
    ) {
        SetTaste()
        Spacer(modifier = Modifier.height(Dimens.largePadding))
        Card (
            colors = CardDefaults.cardColors(
                containerColor = CustomTheme.colors.onSurface,
            ),
            shape = RoundedCornerShape(topStart = Dimens.cornerRadius,
            topEnd = Dimens.cornerRadius),
            modifier = Modifier.fillMaxWidth()
        ){
            Column(
                modifier = Modifier.padding(horizontal = Dimens.largePadding)
                    .padding(top = Dimens.largePadding),
                verticalArrangement = Arrangement.spacedBy(Dimens.largePadding)
            ) {
                Text(
                    text = "My 레시피",
                    style = CustomTheme.typography.title1,
                    color = CustomTheme.colors.textPrimary,
                    modifier = Modifier.fillMaxWidth()
                )
                if(state.isLoading){
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .height(30.dp)
                                .align(Alignment.Center)
                        )
                    }
                }else{
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        state = rememberLazyGridState(),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                    ) {
                        items( state.recipeItems,){ item ->
                            MyRecipe(item, onAction = onAction,
                                onClick = {
                                    navigate(item.id)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
