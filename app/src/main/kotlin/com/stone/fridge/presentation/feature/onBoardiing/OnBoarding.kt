package com.stone.fridge.presentation.feature.onBoardiing

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
import com.stone.fridge.R
import com.stone.fridge.core.util.Dimens
import com.stone.fridge.ui.theme.CustomTheme


@Composable
fun OnBoarding(navigateToLogin: () -> Unit){
    Column(
        modifier = Modifier.fillMaxSize().background(color = CustomTheme.colors.onSurface),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(imageVector = ImageVector.vectorResource(R.drawable.logo),
           contentDescription = "Logo")
        Button(
            onClick = { navigateToLogin() },
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
    OnBoarding(
        {}
    )
}