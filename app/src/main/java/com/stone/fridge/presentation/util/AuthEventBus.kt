package com.stone.fridge.presentation.util

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object AuthEventBus {
    private val _authEventFlow = MutableSharedFlow<AuthEvent>(replay = 0, extraBufferCapacity = 1)
    val authEventFlow = _authEventFlow.asSharedFlow()

    fun send(event: AuthEvent) {
        _authEventFlow.tryEmit(event)
    }
}