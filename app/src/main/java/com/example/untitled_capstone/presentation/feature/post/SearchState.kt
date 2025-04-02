package com.example.untitled_capstone.presentation.feature.post

import androidx.compose.runtime.mutableStateOf
import androidx.paging.PagingData
import com.example.untitled_capstone.domain.model.PostRaw
import kotlinx.coroutines.flow.MutableStateFlow

class SearchState{
    val searchResult: MutableStateFlow<PagingData<PostRaw>> = MutableStateFlow(PagingData.empty())
    val isLoading = mutableStateOf(false)
    val error = mutableStateOf("")
}