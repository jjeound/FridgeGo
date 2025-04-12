package com.example.untitled_capstone.presentation.feature.main

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.untitled_capstone.domain.use_case.app_entry.ReadAppEntry
import com.example.untitled_capstone.navigation.Graph
import com.example.untitled_capstone.presentation.util.AuthEvent
import com.example.untitled_capstone.presentation.util.AuthEventBus
import com.example.untitled_capstone.presentation.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val readAppEntry: ReadAppEntry
) :ViewModel() {
    var topSelector by mutableStateOf(true)
        private set

    private val _authState = MutableStateFlow<AuthState>(AuthState.Login)
    val authState = _authState.asStateFlow()

    private val _startDestination = mutableStateOf<Graph>(Graph.OnBoardingGraph)
    val startDestination: State<Graph> = _startDestination

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
                if (appEntry && _authState.value is AuthState.Login) {
                    _startDestination.value = Graph.HomeGraph
                } else {
                    _startDestination.value = Graph.OnBoardingGraph
                }
                delay(800)
                _splashCondition.value = false
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
                        _authState.value = AuthState.Logout
                    }
                }
            }
        }
    }
}