package com.example.untitled_capstone.presentation.feature.shopping

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.untitled_capstone.R
import com.example.untitled_capstone.domain.model.Post
import com.example.untitled_capstone.presentation.feature.shopping.event.PostAction
import com.example.untitled_capstone.presentation.feature.shopping.state.PostState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PostViewModel: ViewModel() {

    private val _state = MutableStateFlow(PostState())
    val state: StateFlow<PostState> = _state.asStateFlow()

    fun onAction(action: PostAction){
        when(action){
            is PostAction.LoadItems -> loadItems()
            is PostAction.AddNewPost -> addNewPost(action.post)
            is PostAction.ToggleLike -> toggleLike(action.id)
        }
    }

    init {
        onAction(PostAction.LoadItems)
    }

    private fun loadItems(){
        viewModelScope.launch {
//            _state.update {
//                it.copy(loading = true)
//            }
//            delay(1000)
            _state.update {
                it.copy(
                    posts = listOf(
                        Post(
                            id = 1,
                            title = "title",
                            content = "caption",
                            image = emptyList(),
                            location = "무거동",
                            time = "2시간 전",
                            totalNumbOfPeople = 5,
                            currentNumOfPeople = 1,
                            likes = 0,
                            isLiked = false,
                            price = 2000,
                            views = 0,
                            category = "식료품"
                        ),
                        Post(
                            id = 2,
                            title = "title",
                            content = "caption",
                            image = emptyList(),
                            location = "무거동",
                            time = "2시간 전",
                            totalNumbOfPeople = 5,
                            currentNumOfPeople = 1,
                            likes = 0,
                            isLiked = false,
                            price = 2000,
                            views = 0,
                            category = "식료품"
                        ),
                        Post(
                            id = 3,
                            title = "title",
                            content = "caption",
                            image = emptyList(),
                            location = "무거동",
                            time = "2시간 전",
                            totalNumbOfPeople = 5,
                            currentNumOfPeople = 1,
                            likes = 0,
                            isLiked = false,
                            price = 2000,
                            views = 0,
                            category = "식료품"
                        ),
                        Post(
                            id = 4,
                            title = "title",
                            content = "caption",
                            image = emptyList(),
                            location = "무거동",
                            time = "2시간 전",
                            totalNumbOfPeople = 5,
                            currentNumOfPeople = 1,
                            likes = 0,
                            isLiked = false,
                            price = 2000,
                            views = 0,
                            category = "식료품"
                        )
                    ),
                    loading = false
                )
            }
        }
    }

    private fun addNewPost(post: Post){
        _state.update {
            it.copy(
                posts = it.posts + post
            )
        }
    }

    private fun toggleLike(id: Int){
        _state.update { currentState ->
            val updatedPosts = currentState.posts.map { post ->
                if (post.id == id) {
                    post.copy(
                        isLiked = !post.isLiked,
                        likes = if(post.isLiked) post.likes - 1 else post.likes + 1
                    )
                } else post
            }
            currentState.copy(posts = updatedPosts)
        }
    }
}