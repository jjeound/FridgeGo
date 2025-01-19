package com.example.untitled_capstone.feature.home.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.untitled_capstone.feature.home.domain.model.Recipe
import com.example.untitled_capstone.ui.theme.CustomTheme
import kotlinx.serialization.Serializable

@Serializable
data class RecipeNav(
    val recipe: Recipe
)

@Composable
fun RecipeScreen(recipe: Recipe){
    Box(
        modifier = Modifier.fillMaxSize().background(CustomTheme.colors.onSurface)
    )
}