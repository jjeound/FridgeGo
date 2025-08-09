package com.stone.fridge.ui

import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stone.fridge.core.data.fcm.FCMRepository
import com.stone.fridge.core.data.local.LocalUserRepository
import com.stone.fridge.core.data.notification.NotificationRepository
import com.stone.fridge.core.data.util.Resource
import com.stone.fridge.core.data.util.asResult
import com.stone.fridge.core.navigation.GoBaseRoute
import com.stone.fridge.feature.home.navigation.HomeBaseRoute
import com.stone.fridge.feature.login.navigation.LoginBaseRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val localUserRepository: LocalUserRepository,
    private val fcmRepository: FCMRepository,
    private val notificationRepository: NotificationRepository
) :ViewModel() {
    val uiState: MutableStateFlow<MainUiState> = MutableStateFlow(MainUiState.Idle)

    private val _splashCondition = mutableStateOf(true)
    val splashCondition: State<Boolean> = _splashCondition

    private val _location = MutableStateFlow<String?>(null)
    val location = _location.asStateFlow()

    private val _startDestination = MutableStateFlow<GoBaseRoute?>(null)
    val startDestination = _startDestination.asStateFlow()

    var shouldShowBottomSheet by mutableStateOf(false)
        private set

    private val _isUnread: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isUnread = _isUnread.asStateFlow()

    init {
        checkUnreadNotification()
        appEntry()
        saveFcmToken()
    }

    fun setStartDestination(route: GoBaseRoute) {
        _startDestination.value = route
    }

    private fun appEntry(){
        viewModelScope.launch {
            if (localUserRepository.readAppEntry()) {
                delay(3000)
            } else {
                _startDestination.value = OnBoardingBaseRoute
                localUserRepository.saveAppEntry()
            }
            _splashCondition.value = false
        }
    }

    private fun saveFcmToken() {
        viewModelScope.launch {
            fcmRepository.saveFcmToken()
        }
    }

    fun checkUnreadNotification() {
        viewModelScope.launch {
            notificationRepository.getUnreadCount()
                .asResult()
                .collectLatest { result ->
                    when(result){
                        is Resource.Success -> {
                            _startDestination.value = HomeBaseRoute
                            _isUnread.value = result.data > 0 == true
                            uiState.emit(MainUiState.Idle)
                        }
                        is Resource.Error -> {
                            _startDestination.value = LoginBaseRoute
                            uiState.emit(MainUiState.Error(result.message))
                        }
                        is Resource.Loading -> {
                            uiState.emit(MainUiState.Loading)
                        }
                    }
                }
        }
    }

    fun getLocation(){
        viewModelScope.launch {
           localUserRepository.getLocation()?.let {
               _location.value = it
           }
        }
    }

    fun showBottomSheet() {
        shouldShowBottomSheet = true
    }

    fun hideBottomSheet() {
        shouldShowBottomSheet = false
    }

    fun updateUnreadNotification() {
        viewModelScope.launch {
            _isUnread.value = false
        }
    }
}

@Stable
sealed interface MainUiState{
    data object Idle: MainUiState
    data object Loading: MainUiState
    data class Error(val message: String): MainUiState
}
