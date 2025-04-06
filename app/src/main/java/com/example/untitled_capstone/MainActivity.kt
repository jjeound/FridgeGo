package com.example.untitled_capstone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.untitled_capstone.presentation.feature.main.MainScreen
import com.example.untitled_capstone.presentation.feature.main.MainViewModel
import com.example.untitled_capstone.ui.theme.Untitled_CapstoneTheme
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

