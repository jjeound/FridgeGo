package com.stone.fridge.feature.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stone.fridge.core.data.notification.NotificationRepository
import com.stone.fridge.core.model.Notification
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository,
): ViewModel() {

    val uiState: MutableStateFlow<NotificationUiState> = MutableStateFlow(NotificationUiState.Loading)

    private val _notificationList = MutableStateFlow<List<Notification>>(emptyList())
    val notificationList = _notificationList.asStateFlow()

    val notifications: StateFlow<List<Notification>> = notificationRepository.getAllNotifications()
        .onCompletion { uiState.value = NotificationUiState.Idle }
        .catch { uiState.value = NotificationUiState.Error(it.message ?: "알 수 없는 오류가 발생하였습니다.") }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )
}


sealed interface NotificationUiState{
    data object Idle : NotificationUiState
    data object Loading : NotificationUiState
    data class Error(val message: String) : NotificationUiState
}