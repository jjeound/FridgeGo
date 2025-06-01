package com.example.untitled_capstone.presentation.feature.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.model.PostRaw
import com.example.untitled_capstone.domain.use_case.my.GetLocationUseCase
import com.example.untitled_capstone.domain.use_case.post.GetLikedPostsUseCase
import com.example.untitled_capstone.domain.use_case.post.GetMyPostsUseCase
import com.example.untitled_capstone.domain.use_case.post.SearchPostsUseCase
import com.example.untitled_capstone.domain.use_case.post.ToggleLikePostUseCase
import com.example.untitled_capstone.presentation.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val searchPostsUseCase: SearchPostsUseCase,
    private val toggleLikePostUseCase: ToggleLikePostUseCase,
    private val getMyPostsUseCase: GetMyPostsUseCase,
    private val getMyLikedPostsUseCase: GetLikedPostsUseCase,
    private val getLocationUseCase: GetLocationUseCase
): ViewModel() {

    val uiState: MutableStateFlow<PostUiState> = MutableStateFlow<PostUiState>(PostUiState.Loading)

    private val _postPagingData: MutableStateFlow<PagingData<PostRaw>> = MutableStateFlow(PagingData.empty())
    val postPagingData = _postPagingData.asStateFlow()


    private val _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

    init {
        getLocation()
    }

    fun getMyPosts() {
        viewModelScope.launch {
            getMyPostsUseCase()
                .distinctUntilChanged()
                .cachedIn(viewModelScope)
                .collect { pagingData ->
                    _postPagingData.value = pagingData
                }
        }
    }

    fun getLikedPosts() {
        viewModelScope.launch {
            getMyLikedPostsUseCase()
                .distinctUntilChanged()
                .cachedIn(viewModelScope)
                .collect { pagingData ->
                    _postPagingData.value = pagingData
                }
        }
    }

    fun fetchPosts(){
        viewModelScope.launch {
            searchPostsUseCase()
                .distinctUntilChanged()
                .cachedIn(viewModelScope)
                .collect { pagingData ->
                    _postPagingData.value = pagingData
                }
        }
    }

    fun toggleLike(id: Long){
        viewModelScope.launch {
            toggleLikePostUseCase(id).collectLatest{
                when(it){
                    is Resource.Success -> {
                        it.data?.let{ result ->
                            _postPagingData.update { pagingData ->
                                pagingData.map {
                                    if(it.id == id){
                                        it.copy(
                                            liked = result,
                                            likeCount = if(it.liked) it.likeCount - 1 else it.likeCount + 1
                                        )
                                    }else{
                                        it
                                    }
                                }
                            }
                            uiState.tryEmit(PostUiState.Idle)
                        }
                    }
                    is Resource.Error -> {
                        uiState.tryEmit(PostUiState.Error(it.message))
                        _event.emit(UiEvent.ShowSnackbar(it.message ?: "Unknown error"))
                    }
                    is Resource.Loading -> {
                        uiState.tryEmit(PostUiState.Loading)
                    }
                }
            }
        }
    }

    fun getLocation(){
        viewModelScope.launch {
            getLocationUseCase().collectLatest {
                when(it){
                    is Resource.Success -> {
                        if (it.data == null){
                            uiState.tryEmit(PostUiState.NoLocation)
                        } else {
                            fetchPosts()
                        }
                    }
                    is Resource.Error -> {
                        uiState.tryEmit(PostUiState.Error(it.message))
                    }
                    is Resource.Loading -> {
                        uiState.tryEmit(PostUiState.Loading)
                    }
                }
            }
        }
    }
}

interface PostUiState {
    data object Idle : PostUiState
    data object Success : PostUiState
    data object Loading : PostUiState
    data object NoLocation: PostUiState
    data class Error(val message: String?) : PostUiState
}