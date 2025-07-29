package com.stone.fridge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.stone.fridge.presentation.feature.main.MainScreen
import com.stone.fridge.presentation.feature.main.MainViewModel
import com.stone.fridge.ui.theme.Untitled_CapstoneTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition(
                condition = {viewModel.splashCondition.value}
            )
        }
        enableEdgeToEdge()
        setContent {
            Untitled_CapstoneTheme {
                MainScreen(viewModel)
            }
        }
    }
}

