package com.stone.fridge.presentation.feature.post.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stone.fridge.core.util.Resource
import com.stone.fridge.domain.model.Post
import com.stone.fridge.domain.use_case.my.GetMyNicknameUseCase
import com.stone.fridge.domain.use_case.my.ReportUserUseCase
import com.stone.fridge.domain.use_case.post.DeletePostUseCase
import com.stone.fridge.domain.use_case.post.GetPostByIdUseCase
import com.stone.fridge.domain.use_case.post.ReportPostUseCase
import com.stone.fridge.domain.use_case.post.ToggleLikePostUseCase
import com.stone.fridge.presentation.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val getPostByIdUseCase: GetPostByIdUseCase,
    private val deletePostUseCase: DeletePostUseCase,
    private val toggleLikePostUseCase: ToggleLikePostUseCase,
    private val reportPostUseCase: ReportPostUseCase,
    private val reportUserUseCase: ReportUserUseCase,
    private val getMyNicknameUseCase: GetMyNicknameUseCase,
): ViewModel() {
    val uiState: MutableStateFlow<PostDetailUiState> = MutableStateFlow<PostDetailUiState>(PostDetailUiState.Loading)

    private val _post = MutableStateFlow<Post?>(null)
    val post = _post.asStateFlow()

    private val _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

    private val _nickname = MutableStateFlow<String?>(null)
    val nickname = _nickname.asStateFlow()

    init {
        getNickname()
    }

    private fun getNickname(){
        viewModelScope.launch {
            _nickname.value  = getMyNicknameUseCase()
        }
    }

    fun getPostById(id: Long){
        viewModelScope.launch {
            getPostByIdUseCase(id).collectLatest{
                when(it){
                    is Resource.Success -> {
                        it.data?.let{
                            _post.value = it
                            uiState.tryEmit(PostDetailUiState.Idle)
                        }
                    }
                    is Resource.Error -> {
                        uiState.tryEmit(PostDetailUiState.Error(it.message))
                        _event.emit(UiEvent.ShowSnackbar(it.message ?: "Unknown error"))
                    }
                    is Resource.Loading -> {
                        uiState.tryEmit(PostDetailUiState.Loading)
                    }
                }
            }
        }
    }

    fun deletePost(id: Long) {
        viewModelScope.launch {
            deletePostUseCase(id).collectLatest{
                when(it){
                    is Resource.Success -> {
                        it.data?.let{
                            uiState.tryEmit(PostDetailUiState.Success)
                            //_event.emit(PostEvent.ClearBackStack)
                        }
                    }
                    is Resource.Error -> {
                        uiState.tryEmit(PostDetailUiState.Error(it.message))
                        _event.emit(UiEvent.ShowSnackbar(it.message ?: "Unknown error"))
                    }
                    is Resource.Loading -> {
                        uiState.tryEmit(PostDetailUiState.Loading)
                    }
                }
            }
        }
    }

    fun toggleLike(id: Long){
        viewModelScope.launch {
            toggleLikePostUseCase(id).collectLatest{
                when(it){
                    is Resource.Success -> {
                        it.data?.let{
                            _post.value = post.value?.copy(liked = it)
                            uiState.tryEmit(PostDetailUiState.Idle)
                        }
                    }
                    is Resource.Error -> {
                        uiState.tryEmit(PostDetailUiState.Error(it.message))
                        _event.emit(UiEvent.ShowSnackbar(it.message ?: "Unknown error"))
                    }
                    is Resource.Loading -> {
                        uiState.tryEmit(PostDetailUiState.Loading)
                    }
                }
            }
        }
    }

    fun reportPost(id: Long, reportType: String, content: String) {
        viewModelScope.launch {
            reportPostUseCase(id, reportType, content).collectLatest{
                when(it){
                    is Resource.Success -> {
                        it.data?.let{
                            uiState.tryEmit(PostDetailUiState.Success)
                        }
                    }
                    is Resource.Error -> {
                        uiState.tryEmit(PostDetailUiState.Error(it.message))
                        _event.emit(UiEvent.ShowSnackbar(it.message ?: "Unknown error"))
                    }
                    is Resource.Loading -> {
                        uiState.tryEmit(PostDetailUiState.Loading)
                    }
                }
            }
        }
    }

    fun reportUser(id: Long, reportType: String, content: String) {
        viewModelScope.launch {
            reportUserUseCase(id, reportType, content).collectLatest{
                when(it){
                    is Resource.Success -> {
                        it.data?.let{
                            uiState.tryEmit(PostDetailUiState.Success)
                        }
                    }
                    is Resource.Error -> {
                        uiState.tryEmit(PostDetailUiState.Error(it.message))
                        _event.emit(UiEvent.ShowSnackbar(it.message ?: "Unknown error"))
                    }
                    is Resource.Loading -> {
                        uiState.tryEmit(PostDetailUiState.Loading)
                    }
                }
            }
        }
    }

}

interface PostDetailUiState {
    data object Idle : PostDetailUiState
    data object Success : PostDetailUiState
    data object Loading : PostDetailUiState
    data class Error(val message: String?) : PostDetailUiState
}