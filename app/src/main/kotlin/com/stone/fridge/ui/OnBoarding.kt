package com.stone.fridge.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.stone.fridge.core.designsystem.Dimens
import com.stone.fridge.core.designsystem.R
import com.stone.fridge.core.designsystem.theme.CustomTheme
import com.stone.fridge.core.navigation.GoBaseRoute
import com.stone.fridge.core.navigation.GoScreen
import com.stone.fridge.core.navigation.currentComposeNavigator
import com.stone.fridge.feature.login.navigation.LoginRoute
import kotlinx.serialization.Serializable

@Serializable data object OnBoardingRoute: GoScreen
@Serializable data object OnBoardingBaseRoute: GoBaseRoute

fun NavGraphBuilder.onBoardingNavigation(
){
    navigation<OnBoardingBaseRoute>(startDestination = OnBoardingRoute) {
        composable<OnBoardingRoute>{
            OnBoarding()
        }
    }
}

@Composable
fun OnBoarding(){
    val composeNavigator = currentComposeNavigator
    Column(
        modifier = Modifier.fillMaxSize().background(color = CustomTheme.colors.onSurface),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(imageVector = ImageVector.vectorResource(R.drawable.logo),
           contentDescription = "Logo")
        Button(
            onClick = {
                composeNavigator.navigate(LoginRoute)
            },
            colors = ButtonDefaults.buttonColors(containerColor = CustomTheme.colors.primary),
            modifier = Modifier.width(600.dp).height(90.dp).padding(
                Dimens.largePadding
            )
        ) {
            Text(
                text = "시작하기",
                color = CustomTheme.colors.onPrimary,
                style = CustomTheme.typography.button1
            )
        }
    }
}

@Preview
@Composable
fun OnBoardingPreview(){
    OnBoarding()
}