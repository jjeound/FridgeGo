package com.example.untitled_capstone.feature.chatting.presentation.event

sealed interface ChattingAction {
    data object GoToChattingRoom: ChattingAction
}