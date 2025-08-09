package com.stone.fridge.feature.post

import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import com.stone.fridge.core.data.local.LocalUserRepository
import com.stone.fridge.core.data.post.PostRepository
import com.stone.fridge.core.data.util.Resource
import com.stone.fridge.core.data.util.asResult
import com.stone.fridge.core.model.PostRaw
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val localUserRepository: LocalUserRepository
): ViewModel() {

    val uiState: MutableStateFlow<PostUiState> = MutableStateFlow(PostUiState.Loading)

    private val _posts: MutableStateFlow<PagingData<PostRaw>> = MutableStateFlow(PagingData.empty())
    val posts = _posts.asStateFlow()

    private val _location: MutableStateFlow<String?> = MutableStateFlow(null)
    val location = _location.asStateFlow()

    init {
        getLocation()
    }

    fun fetchPosts(){
        viewModelScope.launch {
            postRepository.getMyPosts()
                .collectLatest { pagingData ->
                    _posts.value = pagingData
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
                            _posts.update { pagingData ->
                                pagingData.map {
                                    if(it.id == id){
                                        it.copy(
                                            liked = result.data,
                                            likeCount = if(it.liked) it.likeCount - 1 else it.likeCount + 1
                                        )
                                    }else{
                                        it
                                    }
                                }
                            }
                            uiState.emit(PostUiState.Idle)
                        }
                        is Resource.Error -> uiState.emit(PostUiState.Error(result.message))
                        is Resource.Loading -> uiState.emit(PostUiState.Loading)
                    }
                }
        }
    }

    fun getLocation(){
        viewModelScope.launch {
            localUserRepository.getLocation()?.let {
                _location.value = it
                fetchPosts()
            }
        }
    }
}

@Stable
sealed interface PostUiState {
    data object Idle : PostUiState
    data object Success : PostUiState
    data object Loading : PostUiState
    data class Error(val message: String) : PostUiState
}