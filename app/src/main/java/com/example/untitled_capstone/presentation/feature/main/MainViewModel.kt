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

    private val _authEvent = MutableSharedFlow<AuthEvent>()
    val authEvent = _authEvent.asSharedFlow()

    private val _splashCondition = mutableStateOf(true)
    val splashCondition: State<Boolean> = _splashCondition

    var showBottomSheet by mutableStateOf(false)
        private set

    init {
        appEntry()
    }

    private fun appEntry(){
        viewModelScope.launch {
            val hasEntered = readAppEntry()
            if (hasEntered) {
                checkToken()
                delay(800)
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
                _authEvent.emit(AuthEvent.Login)
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
}
