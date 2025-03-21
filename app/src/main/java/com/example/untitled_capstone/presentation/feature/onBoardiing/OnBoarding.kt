package com.example.untitled_capstone.presentation.feature.onBoardiing

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.ui.theme.CustomTheme
import kotlinx.serialization.Serializable


@Composable
fun OnBoarding(navigateToHome: () -> Unit, navigateToLogin: () -> Unit, onEvent: (OnBoardingEvent) -> Unit){
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
                text = "로그인하기",
                color = CustomTheme.colors.onPrimary,
                style = CustomTheme.typography.button1
            )
        }
        Row (verticalAlignment = Alignment.CenterVertically){
            Text(
                text = "로그인 다음에 할래요",
                color = CustomTheme.colors.textSecondary,
                style = CustomTheme.typography.caption2
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                modifier = Modifier.clickable {
                    navigateToHome()
                    onEvent(OnBoardingEvent.SaveAppEntry)
                },
                text = "시작하기",
                color = CustomTheme.colors.textPrimary,
                style = CustomTheme.typography.caption1
            )
        }
    }
}

@Preview
@Composable
fun OnBoardingPreview(){
    OnBoarding(
        {}, {}, {}
    )
}