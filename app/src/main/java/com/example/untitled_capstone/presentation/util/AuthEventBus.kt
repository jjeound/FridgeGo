package com.example.untitled_capstone.presentation.util

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

object AuthEventBus {
    val authEventChannel = Channel<AuthEvent>(Channel.BUFFERED)

    @OptIn(DelicateCoroutinesApi::class)
    fun send(event: AuthEvent) {
        GlobalScope.launch {
            authEventChannel.send(event)
        }
    }
}