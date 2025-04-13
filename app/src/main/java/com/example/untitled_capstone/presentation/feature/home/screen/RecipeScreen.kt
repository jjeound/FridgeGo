package com.example.untitled_capstone.presentation.feature.home.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.domain.model.Recipe
import com.example.untitled_capstone.navigation.Screen
import com.example.untitled_capstone.presentation.feature.home.HomeEvent
import com.example.untitled_capstone.presentation.feature.home.HomeViewModel
import com.example.untitled_capstone.presentation.feature.home.state.RecipeState
import com.example.untitled_capstone.ui.theme.CustomTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeScreen(id: Long, viewModel: HomeViewModel, onEvent: (HomeEvent) -> Unit, navController: NavHostController){
    val state = remember { viewModel.recipeState }
    val isLiked = remember { derivedStateOf {
        state.recipe?.liked == true
    } }
    var expanded by remember { mutableStateOf(false) }
    val menuItem = listOf("수정", "삭제")
    LaunchedEffect(Unit) {
        onEvent(HomeEvent.GetRecipeById(id))
    }
    if(state.isLoading){
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator(
                color = CustomTheme.colors.primary,
            )
        }
    }
    if (state.recipe != null){
        val recipe = state.recipe!!
        Scaffold(
            containerColor = CustomTheme.colors.onSurface,
            topBar = {
                CenterAlignedTopAppBar(
                    modifier = Modifier.padding(Dimens.topBarPadding),
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                navController.popBackStack()
                                onEvent(HomeEvent.InitState)
                            }
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
                            text = recipe.title,
                            style = CustomTheme.typography.title1,
                            color = CustomTheme.colors.textPrimary,
                            softWrap = true
                        )
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                expanded = !expanded
                            }
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.more),
                                tint = CustomTheme.colors.iconDefault,
                                contentDescription = "more",
                            )
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            containerColor = CustomTheme.colors.textTertiary,
                            shape = RoundedCornerShape(Dimens.cornerRadius),
                        ) {
                            menuItem.forEach { option ->
                                DropdownMenuItem(
                                    modifier = Modifier.height(30.dp).width(90.dp),
                                    text = {
                                        Text(
                                            text = option,
                                            style = CustomTheme.typography.caption1,
                                            color = CustomTheme.colors.textPrimary,
                                        )
                                    },
                                    onClick = {
                                        expanded = false
                                        when(option){
                                            menuItem[0] -> {
                                                navController.navigate(
                                                    Screen.RecipeModifyNav
                                                )
                                            }
                                            menuItem[1] -> {
                                                onEvent(HomeEvent.DeleteRecipe(recipe.id))
                                                navController.popBackStack()
                                            }
                                        }
                                    },
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = CustomTheme.colors.onSurface
                    )
                )
            }
        ) { innerPadding ->
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth().padding(innerPadding),
                thickness = 1.dp,
                color = CustomTheme.colors.borderLight
            )
            Column(
                modifier = Modifier.padding(innerPadding).padding(
                    horizontal = Dimens.largePadding,
                    vertical = Dimens.largePadding
                ).fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(Dimens.largePadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.size(100.dp)
                        .clip(shape = RoundedCornerShape(Dimens.cornerRadius))
                        .background(CustomTheme.colors.surface),
                    contentAlignment = Alignment.BottomEnd,
                ){
                    if (recipe.imageUrl != null) {
                        AsyncImage(
                            model = recipe.imageUrl,
                            contentDescription = recipe.title,
                            alignment = Alignment.Center,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(100.dp)
                                .clip(shape = RoundedCornerShape(Dimens.cornerRadius))
                        )
                    }
                    IconButton(
                        onClick = {
                            onEvent(HomeEvent.ToggleLike(recipe.id, !recipe.liked))
                        }
                    ) {
                        if(isLiked.value){
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
                Column{
                    Text(
                        modifier = Modifier.padding(vertical = 6.dp),
                        text = "재료 \uD83D\uDCCC",
                        style = CustomTheme.typography.title1,
                        color = CustomTheme.colors.textPrimary,
                    )
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = CustomTheme.colors.borderLight
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(Dimens.smallPadding),
                ){
                    recipe.ingredients.forEach {
                        Row(
                            modifier = Modifier.wrapContentWidth(),
                        ) {
                            Text(
                            text = "✅",
                            style = CustomTheme.typography.body2,
                            color = CustomTheme.colors.textPrimary,
                            )
                            Spacer(
                                modifier = Modifier.width(Dimens.smallPadding)
                            )
                            Text(
                                text = it,
                                style = CustomTheme.typography.body2,
                                color = CustomTheme.colors.textPrimary,
                            )
                        }
                    }
                }
                Column{
                    Text(
                        modifier = Modifier.padding(vertical = 6.dp),
                        text = "레시피 \uD83D\uDE80",
                        style = CustomTheme.typography.title1,
                        color = CustomTheme.colors.textPrimary,
                    )
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = CustomTheme.colors.borderLight
                    )
                }
                val steps = Regex("""(\d+\.)\s""").findAll(recipe.instructions)
                    .map { it.value + recipe.instructions.substring(it.range.last + 1).split(Regex("""\d+\.\s"""))[0] }
                    .toList()
                Column{
                    steps.forEach {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text(
                                text = it,
                                style = CustomTheme.typography.body2,
                                color = CustomTheme.colors.textPrimary,
                            )
                        }
                    }
                }
            }
        }
    }
}