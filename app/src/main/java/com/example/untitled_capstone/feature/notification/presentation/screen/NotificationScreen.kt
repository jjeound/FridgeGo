package com.example.untitled_capstone.feature.notification.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavHostController
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.feature.home.presentation.composable.MyRecipe
import com.example.untitled_capstone.feature.home.presentation.screen.RecipeNav
import com.example.untitled_capstone.feature.notification.presentation.composable.NotificationCard
import com.example.untitled_capstone.feature.notification.presentation.state.NotificationState
import com.example.untitled_capstone.ui.theme.CustomTheme
import kotlinx.serialization.Serializable

@Serializable
data object NotificationNav

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(navController: NavHostController, state: NotificationState){
    Scaffold(
        containerColor = CustomTheme.colors.surface,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "알림",
                        style = CustomTheme.typography.title1,
                        color = CustomTheme.colors.textPrimary,
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {navController.popBackStack()}
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.chevron_left),
                            tint = CustomTheme.colors.iconSelected,
                            contentDescription = "back",
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CustomTheme.colors.surface
                ),
                actions = {
                    IconButton(
                        onClick = { }
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.more),
                            contentDescription = "option",
                            tint = CustomTheme.colors.iconDefault
                        )
                    }
                }
            )
        }
    ){ innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding).padding(horizontal = Dimens.surfacePadding, vertical = Dimens.onSurfacePadding),
            verticalArrangement = Arrangement.spacedBy(Dimens.onSurfacePadding)
        ) {
            if(!state.isLoading){
                items( state.notifications,){ item ->
                    NotificationCard(item)
                }
            }
        }
    }
}