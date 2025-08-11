package com.stone.fridge.feature.my

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stone.fridge.core.data.my.MyRepository
import com.stone.fridge.core.model.Profile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    private val myRepository: MyRepository
): ViewModel(){

    internal val uiState: MutableStateFlow<MyUiState> = MutableStateFlow(MyUiState.Loading)

    val profile: StateFlow<Profile?> = myRepository.getMyProfile()
        .onCompletion { uiState.value = MyUiState.Idle }
        .catch { uiState.value = MyUiState.Error(it.message ?: "알 수 없는 오류가 발생했습니다.") }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )
}

@Stable
internal sealed interface MyUiState {
    data object Idle: MyUiState
    data object Loading: MyUiState
    data class Error(val message: String): MyUiState
}