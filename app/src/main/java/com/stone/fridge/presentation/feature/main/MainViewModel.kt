package com.stone.fridge.presentation.feature.main

import android.util.Log
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stone.fridge.core.util.Resource
import com.stone.fridge.domain.repository.TokenRepository
import com.stone.fridge.domain.use_case.app_entry.ReadAppEntry
import com.stone.fridge.domain.use_case.login.SaveFCMTokenUseCase
import com.stone.fridge.domain.use_case.my.GetLocationUseCase
import com.stone.fridge.presentation.util.AuthEvent
import com.stone.fridge.presentation.util.UiEvent
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import com.stone.fridge.domain.use_case.notification.GetUnreadCountUseCase
import com.stone.fridge.domain.use_case.notification.IsUnreadNotificationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val readAppEntry: ReadAppEntry,
    private val tokenRepository: TokenRepository,
    private val saveFCMTokenUseCase: SaveFCMTokenUseCase,
    private val getLocationUseCase: GetLocationUseCase,
    private val getUnreadCountUseCase: GetUnreadCountUseCase
) :ViewModel() {
    var topSelector by mutableStateOf(true)
        private set

    val uiState: MutableStateFlow<MainUiState> = MutableStateFlow<MainUiState>(MainUiState.Idle)

    private val _authEvent = MutableSharedFlow<AuthEvent>()
    val authEvent = _authEvent.asSharedFlow()

    private val _splashCondition = mutableStateOf(true)
    val splashCondition: State<Boolean> = _splashCondition

    private val _dong = MutableStateFlow<String?>(null)
    val dong = _dong.asStateFlow()

    var showBottomSheet by mutableStateOf(false)
        private set

    private val _isUnread: MutableStateFlow<Boolean> = MutableStateFlow<Boolean>(false)
    val isUnread = _isUnread.asStateFlow()

    private val _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

    init {
        appEntry()
        checkUnreadNotification()
    }

    private fun appEntry(){
        viewModelScope.launch {
            val hasEntered = readAppEntry()
            if (hasEntered) {
                checkToken()
                delay(2000)
            }
            _splashCondition.value = false
        }
    }

    private fun checkToken() {
        viewModelScope.launch {
            val token = tokenRepository.refreshAndSaveToken()
            if (token == null) {
                _authEvent.emit(AuthEvent.Logout)
            } else {
                Firebase.messaging.token.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("FCM", "FCM Token: ${task.result}")
                        saveFcmToken(task.result)
                    }
                }
                _authEvent.emit(AuthEvent.Login)
            }
        }
    }

    private fun saveFcmToken(token: String) {
        viewModelScope.launch {
            saveFCMTokenUseCase(token)
        }
    }

    fun checkUnreadNotification() {
        viewModelScope.launch {
            getUnreadCountUseCase().collectLatest {
                when(it){
                    is Resource.Success -> {
                        it.data?.let {
                            _isUnread.value = it > 0 == true
                        }
                        uiState.tryEmit(MainUiState.Idle)
                    }
                    is Resource.Error -> {
                        uiState.tryEmit(MainUiState.Error(it.message))
                        _event.emit(UiEvent.ShowSnackbar(it.message ?: "Unknown error"))
                    }
                    is Resource.Loading -> {
                        uiState.tryEmit(MainUiState.Loading)
                    }
                }
            }
        }
    }

    fun getLocation(){
        viewModelScope.launch {
            getLocationUseCase().collectLatest{
                when(it){
                    is Resource.Success -> {
                        it.data?.let {
                            _dong.value = it
                        } ?: _event.emit(UiEvent.ShowSnackbar("동네를 설정해야 합니다."))
                        uiState.tryEmit(MainUiState.Idle)
                    }
                    is Resource.Error -> {
                        uiState.tryEmit(MainUiState.Error(it.message))
                        _event.emit(UiEvent.ShowSnackbar(it.message ?: "Unknown error"))
                    }
                    is Resource.Loading -> {
                        uiState.tryEmit(MainUiState.Loading)
                    }
                }
            }
        }
    }

    fun updateTopSelector() {
        topSelector = !topSelector
    }

    fun showBottomSheet() {
        showBottomSheet = true
    }

    fun hideBottomSheet() {
        showBottomSheet = false
    }

    fun updateUnreadNotification() {
        viewModelScope.launch {
            _isUnread.value = false
        }
    }
}

@Stable
interface MainUiState{
    data object Idle: MainUiState
    data object Loading: MainUiState
    data class Error(val message: String?): MainUiState
}
