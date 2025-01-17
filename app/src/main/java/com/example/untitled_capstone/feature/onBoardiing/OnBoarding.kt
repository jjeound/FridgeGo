package com.example.untitled_capstone.feature.onBoardiing

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
import com.example.untitled_capstone.R
import com.example.untitled_capstone.ui.theme.CustomTheme
import kotlinx.serialization.Serializable

@Serializable
object OnBoarding

@Composable
fun OnBoarding(){
    Column(
        modifier = Modifier.fillMaxSize().background(color = CustomTheme.colors.onSurface),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(imageVector = ImageVector.vectorResource(R.drawable.splash_logo_temp),
           contentDescription = "Logo")
        Spacer(modifier = Modifier.height(200.dp))
        Button(
            onClick = { /* TODO: Handle start click */ },
            colors = ButtonDefaults.buttonColors(containerColor = CustomTheme.colors.primary),
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(50.dp)
        ) {
            Text(
                text = "시작하기",
                color = CustomTheme.colors.onPrimary,
                fontFamily = CustomTheme.typography.button1.fontFamily,
                fontWeight = CustomTheme.typography.button1.fontWeight,
                fontSize = CustomTheme.typography.button1.fontSize
            )
        }
        Row (modifier = Modifier.padding(vertical = 6.dp), verticalAlignment = Alignment.CenterVertically){
            Text(
                text = "이미 계정이 있나요?",
                color = CustomTheme.colors.textSecondary,
                fontFamily = CustomTheme.typography.caption2.fontFamily,
                fontSize = CustomTheme.typography.caption2.fontSize,
                fontWeight = CustomTheme.typography.caption2.fontWeight
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                modifier = Modifier.clickable {  },
                text = "로그인",
                color = CustomTheme.colors.textPrimary,
                fontFamily = CustomTheme.typography.caption1.fontFamily,
                fontSize = CustomTheme.typography.caption1.fontSize,
                fontWeight = CustomTheme.typography.caption1.fontWeight
            )
        }
    }
}

@Preview
@Composable
fun OnBoardingPreview(){
    OnBoarding()
}