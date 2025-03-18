package com.example.untitled_capstone

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.untitled_capstone.presentation.feature.main.MainScreen
import com.example.untitled_capstone.ui.theme.Untitled_CapstoneTheme
import com.kakao.sdk.common.util.Utility
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
//            setKeepOnScreenCondition(
//                !viewModel.isReady.value
//            )
        }
        enableEdgeToEdge()
        setContent {
            Untitled_CapstoneTheme {
                MainScreen()
            }
        }
    }
}

