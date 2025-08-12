package com.stone.fridge.feature.post.detail

import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stone.fridge.core.data.chat.ChatRepository
import com.stone.fridge.core.data.local.LocalUserRepository
import com.stone.fridge.core.data.post.PostRepository
import com.stone.fridge.core.data.util.Resource
import com.stone.fridge.core.data.util.asResult
import com.stone.fridge.core.model.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val localUserRepository: LocalUserRepository,
    private val chatRepository: ChatRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    internal val uiState: MutableStateFlow<PostDetailUiState> = MutableStateFlow(PostDetailUiState.Loading)

    val postId = savedStateHandle.getStateFlow<Long?>("postId", null)

    private val _post = MutableStateFlow<Post?>(null)
    val post = _post.asStateFlow()

    private val _nickname = MutableStateFlow<String?>(null)
    val nickname = _nickname.asStateFlow()

    init {
        postId.filterNotNull().flatMapLatest { id ->
            postRepository.getPostById(id)
        }.onCompletion { uiState.value = PostDetailUiState.Idle }
        .catch { uiState.value = PostDetailUiState.Error(it.message ?: "알 수 없는 오류가 발생하였습니다.") }
        .launchIn(viewModelScope)
        getNickname()
    }

    private fun getNickname(){
        viewModelScope.launch {
            _nickname.value  = localUserRepository.getNickname()
        }
    }

    fun deletePost(id: Long) {
        viewModelScope.launch {
            postRepository.deletePost(id)
                .asResult()
                .collectLatest{ result ->
                    when(result){
                        is Resource.Success -> uiState.emit(PostDetailUiState.Success)
                        is Resource.Error ->  uiState.emit(PostDetailUiState.Error(result.message))
                        is Resource.Loading -> uiState.emit(PostDetailUiState.Loading)
                    }
                }
        }
    }

    fun toggleLike(id: Long){
        viewModelScope.launch {
            postRepository.toggleLike(id)
                .asResult()
                .collectLatest{ result ->
                    when(result){
                        is Resource.Success -> {
                            _post.value = post.value?.copy(liked = result.data)
                            uiState.emit(PostDetailUiState.Idle)
                        }
                        is Resource.Error -> uiState.emit(PostDetailUiState.Error(result.message))
                        is Resource.Loading -> uiState.emit(PostDetailUiState.Loading)
                    }
                }
        }
    }

    fun closeChatRoom(roomId: Long){
        viewModelScope.launch {
            chatRepository.closeChatRoom(roomId)
                .asResult()
                .collectLatest { result ->
                    when(result){
                        is Resource.Success -> uiState.emit(PostDetailUiState.Idle)
                        is Resource.Error ->  uiState.emit(PostDetailUiState.Error(result.message))
                        is Resource.Loading -> uiState.emit(PostDetailUiState.Loading)
                    }
            }
        }
    }
}

@Stable
internal sealed interface PostDetailUiState {
    data object Idle : PostDetailUiState
    data object Success : PostDetailUiState
    data object Loading : PostDetailUiState
    data class Error(val message: String) : PostDetailUiState
}