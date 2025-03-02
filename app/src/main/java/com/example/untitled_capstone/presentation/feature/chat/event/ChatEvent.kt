package com.example.untitled_capstone.presentation.feature.chat.event

sealed interface ChattingAction {
    data object GoToChattingRoom: ChattingAction
}