package com.example.untitled_capstone.presentation.feature.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.model.NewPost
import com.example.untitled_capstone.domain.model.PostRaw
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

    fun onEvent(action: PostEvent){
        when(action){
            is PostEvent.LoadItems -> loadItems()
            is PostEvent.AddNewPost -> addNewPost(action.post)
            is PostEvent.ToggleLike -> toggleLike(action.id)
        }
    }

    init {
        onEvent(PostEvent.LoadItems)
    }

    private fun loadItems(){
        viewModelScope.launch {
            postUseCases.getPostItems()
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
//        _state.update { currentState ->
//            val updatedPosts = currentState.posts.map { post ->
//                if (post.id == id) {
//                    post.copy(
//                        isLiked = !post.isLiked,
//                        likes = if(post.isLiked) post.likes - 1 else post.likes + 1
//                    )
//                } else post
//            }
//            currentState.copy(posts = updatedPosts)
//        }
    }
}