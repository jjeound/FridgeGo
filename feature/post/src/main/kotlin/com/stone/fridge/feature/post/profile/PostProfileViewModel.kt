package com.stone.fridge.feature.post.profile

import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stone.fridge.core.data.my.MyRepository
import com.stone.fridge.core.model.Profile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PostProfileViewModel @Inject constructor(
    private val myRepository: MyRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    val uiState: MutableStateFlow<PostProfileUiState> =
        MutableStateFlow(PostProfileUiState.Loading)

    val userName: StateFlow<String?> = savedStateHandle.getStateFlow("userName", null)

    val profile: StateFlow<Profile?> = userName.flatMapLatest { userName ->
        if(userName != null){
            myRepository.getOtherProfile(userName)
        } else {
            myRepository.getMyProfile()
        }
    }.onCompletion {
        uiState.value = PostProfileUiState.Idle
    }.catch {
        uiState.value = PostProfileUiState.Error(it.message ?: "알 수 없는 오류가 발생하였습니다.")
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null
    )
}

@Stable
sealed interface PostProfileUiState {
    data object Idle: PostProfileUiState
    data object Loading: PostProfileUiState
    data class Error(val message: String): PostProfileUiState
}