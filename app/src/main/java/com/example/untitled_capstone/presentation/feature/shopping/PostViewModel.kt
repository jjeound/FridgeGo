package com.example.untitled_capstone.presentation.feature.shopping

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.untitled_capstone.R
import com.example.untitled_capstone.domain.model.Post
import com.example.untitled_capstone.presentation.feature.shopping.state.PostState

class PostViewModel: ViewModel() {
    var state by mutableStateOf(PostState())
        private set

    init {
        state = state.copy(
            posts = listOf(
                Post(
                    title = "title",
                    content = "caption",
                    image = R.drawable.ic_launcher_background,
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
                    title = "title",
                    content = "caption",
                    image = R.drawable.ic_launcher_background,
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
                    title = "title",
                    content = "caption",
                    image = null,
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
                    title = "title",
                    content = "caption",
                    image = null,
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
            )
        )
    }
}