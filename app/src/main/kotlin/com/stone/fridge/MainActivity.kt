package com.stone.fridge

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.stone.fridge.core.auth.AuthEvent
import com.stone.fridge.core.auth.AuthEventBus
import com.stone.fridge.core.designsystem.theme.GoTheme
import com.stone.fridge.core.navigation.AppComposeNavigator
import com.stone.fridge.core.navigation.GoScreen
import com.stone.fridge.core.navigation.LocalComposeNavigator
import com.stone.fridge.feature.chat.navigation.ChattingRoomNav
import com.stone.fridge.feature.home.navigation.HomeBaseRoute
import com.stone.fridge.feature.login.navigation.LoginBaseRoute
import com.stone.fridge.ui.GoMain
import com.stone.fridge.ui.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    @Inject
    internal lateinit var composeNavigator: AppComposeNavigator<GoScreen>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition(
                condition = {viewModel.splashCondition.value}
            )
        }
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                AuthEventBus.authEventFlow.collect { event ->
                    when (event) {
                        is AuthEvent.Login -> {
                            Log.d("MainActivity", "Login event received")
                            viewModel.setStartDestination(HomeBaseRoute)
                        }
                        is AuthEvent.Logout -> {
                            Log.d("MainActivity", "Logout event received")
                            viewModel.setStartDestination(LoginBaseRoute)
                        }
                    }
                }
            }
        }
        enableEdgeToEdge()
        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val isUnread by viewModel.isUnread.collectAsStateWithLifecycle()
            val location by viewModel.location.collectAsStateWithLifecycle()
            val startDestination by viewModel.startDestination.collectAsStateWithLifecycle()
            val roomId = intent.getLongExtra("roomId", -1L)
            if (roomId != -1L) {
                composeNavigator.navigate(ChattingRoomNav(roomId, true))
            }
            CompositionLocalProvider(
                LocalComposeNavigator provides composeNavigator,
            ){
                GoTheme {
                    if(startDestination != null){
                        GoMain(
                            composeNavigator = composeNavigator,
                            uiState = uiState,
                            isUnread = isUnread,
                            location = location,
                            shouldShowBottomSheet = viewModel.shouldShowBottomSheet,
                            showBottomSheet = viewModel::showBottomSheet,
                            hideBottomSheet = viewModel::hideBottomSheet,
                            updateUnreadNotification = viewModel::updateUnreadNotification,
                            startDestination = startDestination!!,
                        )
                    }
                }
            }
        }
    }
}

