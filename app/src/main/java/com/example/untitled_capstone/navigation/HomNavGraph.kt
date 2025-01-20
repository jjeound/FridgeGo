package com.example.untitled_capstone.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.untitled_capstone.feature.home.domain.model.Recipe
import com.example.untitled_capstone.feature.home.presentation.HomeViewModel
import com.example.untitled_capstone.feature.home.presentation.screen.HomeScreen
import com.example.untitled_capstone.feature.home.presentation.screen.RecipeNav
import com.example.untitled_capstone.feature.home.presentation.screen.RecipeScreen
import com.example.untitled_capstone.feature.main.BottomScreen
import kotlin.reflect.typeOf
