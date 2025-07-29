package com.stone.fridge.presentation.feature.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stone.fridge.core.util.Resource
import com.stone.fridge.domain.model.Notification
import com.stone.fridge.domain.use_case.notification.GetAllNotificationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val getAllNotificationsUseCase: GetAllNotificationsUseCase,
): ViewModel() {

    val uiState: MutableStateFlow<NotificationUiState> = MutableStateFlow(NotificationUiState.Loading)

    private val _notificationList = MutableStateFlow<List<Notification>>(emptyList())
    val notificationList = _notificationList.asStateFlow()

    init {
        fetchNotifications()
    }

    fun fetchNotifications(){
        viewModelScope.launch {
            getAllNotificationsUseCase().collectLatest {
                when(it){
                    is Resource.Success -> {
                        _notificationList.value = it.data ?: emptyList()
                        uiState.tryEmit(NotificationUiState.Idle)
                    }
                    is Resource.Error -> {
                        uiState.tryEmit(NotificationUiState.Error(it.message ?: "Unknown error"))
                    }
                    is Resource.Loading -> {
                        uiState.tryEmit(NotificationUiState.Loading)
                    }
                }
            }
        }
    }
}


sealed interface NotificationUiState{
    data object Idle : NotificationUiState
    data object Loading : NotificationUiState
    data class Error(val message: String) : NotificationUiState
}