package com.example.untitled_capstone.presentation.feature.main

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.untitled_capstone.data.util.ErrorCode.JWT4004
import com.example.untitled_capstone.domain.repository.TokenRepository
import com.example.untitled_capstone.domain.use_case.app_entry.ReadAppEntry
import com.example.untitled_capstone.presentation.util.AuthEvent
import com.example.untitled_capstone.presentation.util.AuthEventBus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val readAppEntry: ReadAppEntry,
    private val tokenRepository: TokenRepository
) :ViewModel() {
    var topSelector by mutableStateOf(true)
        private set

    private val _authEvent = MutableSharedFlow<AuthState>()
    val authEvent = _authEvent.asSharedFlow()

    private val _splashCondition = mutableStateOf(true)
    val splashCondition: State<Boolean> = _splashCondition

    var showBottomSheet by mutableStateOf(false)
        private set

    init {
        appEntry()
        observeAuthEvents()
    }

    private fun appEntry(){
        viewModelScope.launch {
            readAppEntry().collect { appEntry ->
                if (appEntry) {
                    checkToken()
                }
                delay(800)
                _splashCondition.value = false
            }
        }
    }

    private fun checkToken(){
        viewModelScope.launch {
            val refreshToken = runBlocking {
                tokenRepository.getRefreshToken().firstOrNull()
            }
            if (refreshToken.isNullOrEmpty()) {
                _authEvent.emit(AuthState.Logout)
                return@launch
            }

            val response = tokenRepository.refreshToken(refreshToken)
            if (response.data == null || response.data.code == JWT4004) { // 리프레시 토큰도 만료됨
                _authEvent.emit(AuthState.Logout)
            } else {
                tokenRepository.saveAccessToken(response.data.result!!.accessToken)
                tokenRepository.saveRefreshToken(response.data.result.refreshToken)
                _authEvent.emit(AuthState.Login)
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



    private fun observeAuthEvents() {
        viewModelScope.launch {
            AuthEventBus.authEventChannel.receiveAsFlow().collect { event ->
                when (event) {
                    is AuthEvent.Logout -> {
                        _authEvent.emit(AuthState.Logout)
                    }
                    is AuthEvent.Login -> {
                        _authEvent.emit(AuthState.Login)
                    }
                }
            }
        }
    }
}