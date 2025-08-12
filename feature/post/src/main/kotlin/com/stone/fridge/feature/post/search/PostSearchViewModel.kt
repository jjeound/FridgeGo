package com.stone.fridge.feature.post.search

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import com.stone.fridge.core.data.post.PostRepository
import com.stone.fridge.core.data.util.Resource
import com.stone.fridge.core.data.util.asResult
import com.stone.fridge.core.model.Keyword
import com.stone.fridge.core.model.PostRaw
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostSearchViewModel @Inject constructor(
    private val postRepository: PostRepository
): ViewModel() {
    internal val uiState: MutableStateFlow<SearchUiState> = MutableStateFlow(SearchUiState.Loading)

    private val _searchResult: MutableStateFlow<PagingData<PostRaw>> = MutableStateFlow(PagingData.empty())
    val searchResult = _searchResult.asStateFlow()

    private val _keywords = MutableStateFlow<List<Keyword>>(emptyList())
    val keywords = _keywords.asStateFlow()


    fun searchPost(keyword: String){
        addSearchHistory(keyword)
        viewModelScope.launch {
            postRepository.searchPosts(keyword)
                .collectLatest { pagingData ->
                    _searchResult.value = pagingData
                }
        }
    }

    fun getSearchHistory(){
        viewModelScope.launch {
            postRepository.getSearchHistory()
                .asResult()
                .collectLatest{ result ->
                    when(result){
                        is Resource.Success -> {
                            _keywords.value = result.data
                            uiState.emit(SearchUiState.Idle)
                        }
                        is Resource.Error -> uiState.emit(SearchUiState.Error(result.message))
                        is Resource.Loading -> uiState.emit(SearchUiState.Loading)
                    }
                }
        }
    }

    fun deleteSearchHistory(word: String){
        viewModelScope.launch {
            postRepository.deleteSearchHistory(word)
                .asResult()
                .collectLatest{ result ->
                    when(result){
                        is Resource.Success -> {
                            _keywords.value = _keywords.value.filter { it.keyword != word }
                            uiState.emit(SearchUiState.Idle)
                        }
                        is Resource.Error -> uiState.emit(SearchUiState.Error(result.message))
                        is Resource.Loading -> uiState.emit(SearchUiState.Loading)
                    }
                }
        }
    }

    fun deleteAllSearchHistory(){
        viewModelScope.launch {
            postRepository.deleteAllSearchHistory()
                .asResult()
                .collectLatest{ result ->
                    when(result){
                        is Resource.Success -> {
                            _keywords.value = emptyList()
                            uiState.tryEmit(SearchUiState.Idle)
                        }
                        is Resource.Error -> uiState.emit(SearchUiState.Error(result.message))
                        is Resource.Loading -> uiState.emit(SearchUiState.Loading)
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
                        _searchResult.update { pagingData ->
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
                        uiState.emit(SearchUiState.Idle)
                    }
                    is Resource.Error -> uiState.emit(SearchUiState.Error(result.message))
                    is Resource.Loading -> uiState.emit(SearchUiState.Loading)
                }
            }
        }
    }

    private fun addSearchHistory(word: String){
        if(_keywords.value.any { it.keyword == word }){
            _keywords.value = _keywords.value.filter { it.keyword != word }
        }
        _keywords.value = listOf(Keyword(word)) + _keywords.value
    }
}

@Stable
internal sealed interface SearchUiState {
    data object Idle : SearchUiState
    data object Success: SearchUiState
    data object Loading : SearchUiState
    data class Error(val message: String) : SearchUiState
}