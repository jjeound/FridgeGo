package com.stone.fridge.presentation.feature.post.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.stone.fridge.core.util.Resource
import com.stone.fridge.domain.model.Keyword
import com.stone.fridge.domain.model.PostRaw
import com.stone.fridge.domain.use_case.post.DeleteAllSearchHistoryUseCase
import com.stone.fridge.domain.use_case.post.DeleteSearchHistoryUseCase
import com.stone.fridge.domain.use_case.post.GetSearchHistoryUseCase
import com.stone.fridge.domain.use_case.post.SearchPostsUseCase
import com.stone.fridge.domain.use_case.post.ToggleLikePostUseCase
import com.stone.fridge.presentation.util.UiEvent
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
class PostSearchViewModel @Inject constructor(
    private val searchPostsUseCase: SearchPostsUseCase,
    private val getSearchHistoryUseCase: GetSearchHistoryUseCase,
    private val deleteSearchHistoryUseCase: DeleteSearchHistoryUseCase,
    private val deleteAllSearchHistoryUseCase: DeleteAllSearchHistoryUseCase,
    private val toggleLikePostUseCase: ToggleLikePostUseCase
): ViewModel() {
    val uiState: MutableStateFlow<SearchUiState> = MutableStateFlow<SearchUiState>(SearchUiState.Loading)

    private val _searchPagingData: MutableStateFlow<PagingData<PostRaw>> = MutableStateFlow(PagingData.empty())
    val searchPagingData = _searchPagingData.asStateFlow()

    private val _keywords = MutableStateFlow<List<Keyword>>(emptyList())
    val keywords = _keywords.asStateFlow()

    private val _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()


    fun searchPost(keyword: String){
        addSearchHistory(keyword)
        viewModelScope.launch {
            searchPostsUseCase(keyword)
                .distinctUntilChanged()
                .cachedIn(viewModelScope)
                .collectLatest { pagingData ->
                    _searchPagingData.value = pagingData
                }
        }
    }

    fun getSearchHistory(){
        viewModelScope.launch {
            getSearchHistoryUseCase().collectLatest{
                when(it){
                    is Resource.Success -> {
                        it.data?.let{
                            _keywords.value = it
                            uiState.tryEmit(SearchUiState.Idle)
                        }
                    }
                    is Resource.Error -> {
                        uiState.tryEmit(SearchUiState.Error(it.message))
                        _event.emit(UiEvent.ShowSnackbar(it.message ?: "Unknown error"))
                    }
                    is Resource.Loading -> {
                        uiState.tryEmit(SearchUiState.Loading)
                    }
                }
            }
        }
    }

    fun deleteSearchHistory(word: String){
        viewModelScope.launch {
            deleteSearchHistoryUseCase(word).collectLatest{
                when(it){
                    is Resource.Success -> {
                        it.data?.let{
                            _keywords.value = _keywords.value.filter { it.keyword != word }
                            uiState.tryEmit(SearchUiState.Idle)
                        }
                    }
                    is Resource.Error -> {
                        uiState.tryEmit(SearchUiState.Error(it.message))
                        _event.emit(UiEvent.ShowSnackbar(it.message ?: "Unknown error"))
                    }
                    is Resource.Loading -> {
                        uiState.tryEmit(SearchUiState.Loading)
                    }
                }
            }
        }
    }

    fun deleteAllSearchHistory(){
        viewModelScope.launch {
            deleteAllSearchHistoryUseCase().collectLatest{
                when(it){
                    is Resource.Success -> {
                        it.data?.let{
                            _keywords.value = emptyList()
                            uiState.tryEmit(SearchUiState.Idle)
                        }
                    }
                    is Resource.Error -> {
                        uiState.tryEmit(SearchUiState.Error(it.message))
                        _event.emit(UiEvent.ShowSnackbar(it.message ?: "Unknown error"))
                    }
                    is Resource.Loading -> {
                        uiState.tryEmit(SearchUiState.Loading)
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
                        it.data?.let{ result ->
                            _searchPagingData.update { pagingData ->
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
                            uiState.tryEmit(SearchUiState.Idle)
                        }
                    }
                    is Resource.Error -> {
                        uiState.tryEmit(SearchUiState.Error(it.message))
                        _event.emit(UiEvent.ShowSnackbar(it.message ?: "Unknown error"))
                    }
                    is Resource.Loading -> {
                        uiState.tryEmit(SearchUiState.Loading)
                    }
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

interface SearchUiState {
    data object Idle : SearchUiState
    data object Success: SearchUiState
    data object Loading : SearchUiState
    data class Error(val message: String?) : SearchUiState
}