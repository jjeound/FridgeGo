package com.example.untitled_capstone

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.untitled_capstone.feature.main.MainScreen
import com.example.untitled_capstone.ui.theme.Untitled_CapstoneTheme


class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "RestrictedApi")
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

