package com.stone.fridge.feature.my.etc

import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import com.stone.fridge.core.data.post.PostRepository
import com.stone.fridge.core.data.util.Resource
import com.stone.fridge.core.data.util.asResult
import com.stone.fridge.core.model.PostRaw
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPostViewModel @Inject constructor(
    private val postRepository: PostRepository,
    savedStateHandle: SavedStateHandle,
): ViewModel() {

    internal val uiState: MutableStateFlow<MyPostUiState> = MutableStateFlow(MyPostUiState.Idle)

    val type = savedStateHandle.getStateFlow<Boolean?>("type", null)

    private val _posts: MutableStateFlow<PagingData<PostRaw>> = MutableStateFlow(PagingData.empty())
    val posts = _posts.asStateFlow()

    init {
        type.filterNotNull().flatMapLatest { it ->
            if(it){
                postRepository.getLikedPosts()
            } else {
                postRepository.getMyPosts()
            }
        }.onStart { uiState.value = MyPostUiState.Loading }
            .onEach { _posts.value = it }
            .onCompletion { uiState.value = MyPostUiState.Idle }
            .catch { uiState.value = MyPostUiState.Error(it.message ?: "알 수 없는 오류가 발생하였습니다.") }
            .launchIn(viewModelScope)
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
                            uiState.emit(MyPostUiState.Idle)
                        }
                        is Resource.Error -> uiState.emit(MyPostUiState.Error(result.message))
                        is Resource.Loading -> uiState.emit(MyPostUiState.Loading)
                    }
                }
        }
    }
}

@Stable
internal sealed interface MyPostUiState {
    data object Idle : MyPostUiState
    data object Loading : MyPostUiState
    data class Error(val message: String) : MyPostUiState
}