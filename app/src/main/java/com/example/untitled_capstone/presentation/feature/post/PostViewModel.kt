package com.example.untitled_capstone.presentation.feature.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.util.PostFetchType
import com.example.untitled_capstone.domain.model.NewPost
import com.example.untitled_capstone.domain.model.PostRaw
import com.example.untitled_capstone.domain.use_case.post.GetPostById
import com.example.untitled_capstone.domain.use_case.post.PostUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postUseCases: PostUseCases
): ViewModel() {

    private val _state = MutableStateFlow(PostState())
    val state: StateFlow<PostState> = _state.asStateFlow()

    private val _postItemState: MutableStateFlow<PagingData<PostRaw>> = MutableStateFlow(PagingData.empty())
    val postItemState = _postItemState.asStateFlow()

    val nickname = postUseCases.getNickname()

    fun onEvent(action: PostEvent){
        when(action){
            is PostEvent.LoadItems -> loadItems()
            is PostEvent.AddNewPost -> addNewPost(action.post)
            is PostEvent.ToggleLike -> toggleLike(action.id)
            is PostEvent.DeletePost -> deletePost(action.id)
            is PostEvent.GetLikedPosts -> getLikedPosts()
            is PostEvent.GetMyPosts -> getMyPosts()
            is PostEvent.GetPostById -> getPostById(action.id)
            is PostEvent.ModifyPost -> modifyPost(action.id, action.newPost)
            is PostEvent.SearchPost -> searchPost(action.keyword)
        }
    }

    init {
        onEvent(PostEvent.LoadItems)
    }



    private fun deletePost(id: Long) {
        viewModelScope.launch {
            val result = postUseCases.deletePost(id)
            when(result){
                is Resource.Success -> {
                    result.data?.let{
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = null
                            )
                        }
                    }
                }
                is Resource.Error -> {
                    _state.update { it.copy(isLoading = false, error = result.message ?: "An unexpected error occurred") }
                }
                is Resource.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    private fun getPostById(id: Long){
        viewModelScope.launch {
            val result = postUseCases.getPostById(id)
            when(result){
                is Resource.Success -> {
                    result.data?.let{
                        _state.update {
                            it.copy(
                                post = result.data,
                                isLoading = false,
                                error = null
                            )
                        }
                    }
                }
                is Resource.Error -> {
                    _state.update { it.copy(isLoading = false, error = result.message ?: "An unexpected error occurred") }
                }
                is Resource.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    private fun modifyPost(id: Long, newPost: NewPost){
        viewModelScope.launch {
            val result = postUseCases.modifyPost(id, newPost)
            when(result){
                is Resource.Success -> {
                    result.data?.let{
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = null
                            )
                        }
                    }
                }
                is Resource.Error -> {
                    _state.update { it.copy(isLoading = false, error = result.message ?: "An unexpected error occurred") }
                }
                is Resource.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    private fun getMyPosts() {
        viewModelScope.launch {
            postUseCases.getMyPosts(PostFetchType.MyPosts)
                .distinctUntilChanged()
                .cachedIn(viewModelScope)
                .collect { pagingData ->
                    _postItemState.value = pagingData
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }

    private fun getLikedPosts() {
        viewModelScope.launch {
            postUseCases.searchPosts(PostFetchType.LikedPosts)
                .distinctUntilChanged()
                .cachedIn(viewModelScope)
                .collect { pagingData ->
                    _postItemState.value = pagingData
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }

    private fun searchPost(keyword: String){
        viewModelScope.launch {
            postUseCases.searchPosts(PostFetchType.Search(keyword))
                .distinctUntilChanged()
                .cachedIn(viewModelScope)
                .collect { pagingData ->
                    _postItemState.value = pagingData
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }

    private fun loadItems(){
        viewModelScope.launch {
            postUseCases.searchPosts(PostFetchType.Search(null))
                .distinctUntilChanged()
                .cachedIn(viewModelScope)
                .collect { pagingData ->
                    _postItemState.value = pagingData
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }

    private fun addNewPost(post: NewPost){
        viewModelScope.launch {
            val result = postUseCases.addPost(post)
            when(result){
                is Resource.Success -> {
                    result.data?.let{
                        loadItems()
                    }
                }
                is Resource.Error -> {
                    _state.update { it.copy(isLoading = false, error = result.message ?: "An unexpected error occurred") }
                }
                is Resource.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    private fun toggleLike(id: Long){
        viewModelScope.launch {
            val result = postUseCases.toggleLike(id)
            when(result){
                is Resource.Success -> {
                    result.data?.let{
//                        _postItemState.update { pagingData ->
//                            pagingData.map {
//                                if(it.id == id){
//                                    it.copy( liked = liked)
//                                }else{
//                                    it
//                                }
//                            }
//                        }
                    }
                    _state.update {
                        it.copy(
                            //post = it.post?.copy(liked = liked),
                            isLoading = false,
                            error = null
                        )
                    }
                }
                is Resource.Error -> {
                    _state.update { it.copy(isLoading = false, error = result.message ?: "An unexpected error occurred") }
                }
                is Resource.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }
            }
        }
    }
}