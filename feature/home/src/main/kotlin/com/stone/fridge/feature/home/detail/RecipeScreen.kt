package com.stone.fridge.feature.home.detail

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.stone.fridge.core.designsystem.Dimens
import com.stone.fridge.core.designsystem.theme.CustomTheme
import com.stone.fridge.core.designsystem.R
import com.stone.fridge.core.model.Recipe
import com.stone.fridge.core.navigation.currentComposeNavigator
import com.stone.fridge.feature.home.navigation.HomeRoute
import com.stone.fridge.feature.home.navigation.RecipeModifyNav

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeScreen(
    viewModel: RecipeViewModel = hiltViewModel(),
    onShowSnackbar: suspend (String, String?) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val recipe by viewModel.recipe.collectAsStateWithLifecycle()
    if (uiState == RecipeUiState.Loading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator(
                color = CustomTheme.colors.primary,
            )
        }
    } else if(recipe != null){
        RecipeContent(
            uiState = uiState,
            recipe = recipe!!,
            toggleLike = viewModel::toggleLike,
            deleteRecipe = viewModel::deleteRecipe,
            onShowSnackbar = onShowSnackbar
        )
    }
}

@Composable
fun RecipeContent(
    uiState: RecipeUiState,
    recipe: Recipe,
    toggleLike: (Long, Boolean) -> Unit,
    deleteRecipe: (Long) -> Unit,
    onShowSnackbar: suspend (String, String?) -> Unit
){
    val composeNavigator = currentComposeNavigator
    var expanded by remember { mutableStateOf(false) }
    val menuItem = listOf("수정", "삭제")
    LaunchedEffect(uiState) {
        if (uiState is RecipeUiState.Success) {
            composeNavigator.navigateAndClearBackStack(HomeRoute)
        } else if  (uiState is RecipeUiState.Error) {
            onShowSnackbar(uiState.message, null)
        }
    }
    Scaffold(
        containerColor = CustomTheme.colors.onSurface,
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.padding(Dimens.topBarPadding),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            composeNavigator.navigateUp()
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
                        containerColor = CustomTheme.colors.onSurface,
                        shape = RoundedCornerShape(Dimens.cornerRadius),
                        border = BorderStroke(
                            width = 1.dp,
                            color = CustomTheme.colors.borderLight
                        )
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
                                            composeNavigator.navigate(
                                                RecipeModifyNav(recipe.id)
                                            )
                                        }
                                        menuItem[1] -> {
                                            deleteRecipe(recipe.id)
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
        RecipeBody(
            modifier = Modifier.padding(innerPadding),
            recipe = recipe,
            toggleLike = toggleLike
        )
    }
}

@Composable
private fun RecipeBody(
    modifier: Modifier,
    recipe: Recipe,
    toggleLike: (Long, Boolean) -> Unit
){
    Column(
        modifier = modifier.padding(
            horizontal = Dimens.largePadding,
            vertical = Dimens.largePadding
        ).fillMaxSize().verticalScroll(rememberScrollState()),
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
                    toggleLike(recipe.id, !recipe.liked)
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
                        imageVector = ImageVector.vectorResource(R.drawable.heart_filled_inside_default),
                        contentDescription = "like",
                        tint = Color.Unspecified
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