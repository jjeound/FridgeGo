package com.example.untitled_capstone.presentation.feature.post

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.util.PostFetchType
import com.example.untitled_capstone.domain.model.Keyword
import com.example.untitled_capstone.domain.model.NewPost
import com.example.untitled_capstone.domain.model.Post
import com.example.untitled_capstone.domain.model.PostRaw
import com.example.untitled_capstone.domain.use_case.post.PostUseCases
import com.example.untitled_capstone.navigation.Screen
import com.example.untitled_capstone.presentation.util.UiEvent
import com.example.untitled_capstone.presentation.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postUseCases: PostUseCases,
): ViewModel() {

    private val _state = MutableStateFlow<UiState>(UiState.Loading)
    val state = _state.asStateFlow()

    var post by mutableStateOf<Post?>(null)
        private set

    private val _postPagingData: MutableStateFlow<PagingData<PostRaw>> = MutableStateFlow(PagingData.empty())
    val postPagingData = _postPagingData.asStateFlow()

    private val _searchPagingData: MutableStateFlow<PagingData<PostRaw>> = MutableStateFlow(PagingData.empty())
    val searchPagingData = _searchPagingData.asStateFlow()

    var nickname by mutableStateOf("")
        private set

    var keywords by mutableStateOf<List<Keyword>>(emptyList())
        private set

    private val _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

    fun onEvent(action: PostEvent){
        when(action){
            is PostEvent.LoadItems -> loadItems()
            is PostEvent.AddNewPost -> addNewPost(action.post, action.images)
            is PostEvent.ToggleLike -> toggleLike(action.id)
            is PostEvent.DeletePost -> deletePost(action.id)
            is PostEvent.GetLikedPosts -> getLikedPosts()
            is PostEvent.GetMyPosts -> getMyPosts()
            is PostEvent.GetPostById -> getPostById(action.id)
            is PostEvent.ModifyPost -> modifyPost(action.id, action.newPost)
            is PostEvent.SearchPost -> searchPost(action.keyword)
            is PostEvent.InitState -> initState()
            is PostEvent.UploadPostImages -> uploadPostImages(action.id, action.images)
            is PostEvent.DeletePostImage -> deletePostImage(action.id, action.imageId)
            is PostEvent.GetSearchHistory -> getSearchHistory()
            is PostEvent.DeleteSearchHistory -> deleteSearchHistory(action.keyword)
            is PostEvent.DeleteAllSearchHistory -> deleteAllSearchHistory()
            is PostEvent.AddSearchHistory -> addSearchHistory(action.word)
            is PostEvent.ReportPost -> reportPost(action.postId, action.reportType, action.content)
            is PostEvent.NavigateUp -> navigateUp(action.route)
            is PostEvent.PopBackStack -> popBackStack()
            is PostEvent.ShowSnackBar -> showSnackbar(action.message)
        }
    }

    init {
        onEvent(PostEvent.LoadItems)
        getNickname()
    }

    private fun getNickname(){
        viewModelScope.launch {
            val result = postUseCases.getNickname()
            nickname = result!!
        }
    }



    private fun deletePost(id: Long) {
        viewModelScope.launch {
            val result = postUseCases.deletePost(id)
            when(result){
                is Resource.Success -> {
                    result.data?.let{
                        _state.update { UiState.Success }
                        _postPagingData.update { pagingData ->
                            pagingData.filter { it.id != id }
                        }
                    }
                }
                is Resource.Error -> {
                    _state.update { UiState.Error(result.message) }
                    _event.emit(UiEvent.ShowSnackbar(result.message ?: "Unknown error"))
                }
                is Resource.Loading -> {
                    _state.update { UiState.Success }
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
                        post = it
                        _state.update { UiState.Success }
                    }
                }
                is Resource.Error -> {
                    _state.update { UiState.Error(result.message) }
                    _event.emit(UiEvent.ShowSnackbar(result.message ?: "Unknown error"))
                }
                is Resource.Loading -> {
                    _state.update { UiState.Success }
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
                        _postPagingData.update { pagingData->
                            pagingData.map {
                                if(it.id == id){
                                    it.copy(
                                        title = newPost.title,
                                        memberCount = newPost.memberCount,
                                        price = newPost.price,
                                    )
                                }else{
                                    it
                                }
                            }
                        }
                        _state.update { UiState.Success }
                    }
                }
                is Resource.Error -> {
                    _state.update { UiState.Error(result.message) }
                    _event.emit(UiEvent.ShowSnackbar(result.message ?: "Unknown error"))
                }
                is Resource.Loading -> {
                    _state.update { UiState.Loading }
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
                    _postPagingData.value = pagingData
                }
        }
    }

    private fun getLikedPosts() {
        viewModelScope.launch {
            postUseCases.searchPosts(PostFetchType.LikedPosts)
                .distinctUntilChanged()
                .cachedIn(viewModelScope)
                .collect { pagingData ->
                    _postPagingData.value = pagingData
                }
        }
    }

    private fun searchPost(keyword: String){
        viewModelScope.launch {
            postUseCases.searchPosts(PostFetchType.Search(keyword))
                .distinctUntilChanged()
                .cachedIn(viewModelScope)
                .collectLatest { pagingData ->
                    _searchPagingData.value = pagingData
                }
        }
    }

    private fun loadItems(){
        viewModelScope.launch {
            postUseCases.searchPosts(PostFetchType.Search(null))
                .distinctUntilChanged()
                .cachedIn(viewModelScope)
                .collect { pagingData ->
                    _postPagingData.value = pagingData
                }
        }
    }

    private fun addNewPost(post: NewPost, images: List<File>? = null){
        viewModelScope.launch {
            val result = postUseCases.addPost(post, images)
            when(result){
                is Resource.Success -> {
                    _state.update { UiState.Success}
                    loadItems()
                }
                is Resource.Error -> {
                    _state.update { UiState.Error(result.message) }
                    _event.emit(UiEvent.ShowSnackbar(result.message ?: "Unknown error"))
                }
                is Resource.Loading -> {
                    _state.update { UiState.Loading }
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
                        _state.update { UiState.Success }
                        _postPagingData.update { pagingData ->
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
                        post = post?.copy(
                            liked = result.data,
                        )
                    }
                }
                is Resource.Error -> {
                    _state.update { UiState.Error(result.message) }
                    _event.emit(UiEvent.ShowSnackbar(result.message ?: "Unknown error"))
                }
                is Resource.Loading -> {
                    _state.update { UiState.Loading }
                }
            }
        }
    }

    private fun initState(){
        post = null
    }

    private fun uploadPostImages(id: Long, images: List<File>){
        viewModelScope.launch {
            val result = postUseCases.uploadPostImages(id, images)
            when(result){
                is Resource.Success -> {
                    result.data?.let {
                        _state.update { UiState.Success }
                        _event.emit(UiEvent.ShowSnackbar(result.data))
                    }
                }
                is Resource.Error -> {
                    _state.update { UiState.Error(result.message) }
                    _event.emit(UiEvent.ShowSnackbar(result.message ?: "Unknown error"))
                }
                is Resource.Loading -> {
                    _state.update { UiState.Loading }
                }
            }
        }
    }

    private fun deletePostImage(id: Long, imageId: Long) {
        viewModelScope.launch {
            val result = postUseCases.deletePostImage(id, imageId)
            when(result){
                is Resource.Success -> {
                    result.data?.let {
                        _state.update { UiState.Success }
                        _event.emit(UiEvent.ShowSnackbar(result.data))
                    }
                }
                is Resource.Error -> {
                    _state.update { UiState.Error(result.message) }
                    _event.emit(UiEvent.ShowSnackbar(result.message ?: "Unknown error"))
                }
                is Resource.Loading -> {
                    _state.update { UiState.Loading }
                }
            }
        }
    }

    private fun getSearchHistory(){
        viewModelScope.launch {
            val result = postUseCases.getSearchHistory()
            when(result){
                is Resource.Success -> {
                    result.data?.let{
                        _state.update { UiState.Success }
                        keywords = it
                    }
                }
                is Resource.Error -> {
                    _state.update { UiState.Error(result.message) }
                    _event.emit(UiEvent.ShowSnackbar(result.message ?: "Unknown error"))
                }
                is Resource.Loading -> {
                    _state.update { UiState.Loading }
                }
            }
        }
    }

    private fun deleteSearchHistory(word: String){
        viewModelScope.launch {
            val result = postUseCases.deleteSearchHistory(word)
            when(result){
                is Resource.Success -> {
                    result.data?.let{
                        keywords = keywords.filter { it.keyword != word }
                        _state.update { UiState.Success }
                    }
                }
                is Resource.Error -> {
                    _state.update { UiState.Error(result.message) }
                    _event.emit(UiEvent.ShowSnackbar(result.message ?: "Unknown error"))
                }
                is Resource.Loading -> {
                    _state.update { UiState.Loading }
                }
            }
        }
    }
    private fun deleteAllSearchHistory(){
        viewModelScope.launch {
            val result = postUseCases.deleteAllSearchHistory()
            when(result){
                is Resource.Success -> {
                    result.data?.let{
                        keywords = emptyList()
                        _state.update { UiState.Success }
                    }
                }
                is Resource.Error -> {
                    _state.update { UiState.Error(result.message) }
                    _event.emit(UiEvent.ShowSnackbar(result.message ?: "Unknown error"))
                }
                is Resource.Loading -> {
                    _state.update { UiState.Loading }
                }
            }
        }
    }

    private fun addSearchHistory(word: String){
        if(keywords.any { it.keyword == word }){
            keywords = keywords.filter { it.keyword != word }
        }
        keywords = listOf(Keyword(word)) + keywords
    }

    private fun reportPost(id: Long, reportType: String, content: String) {
        viewModelScope.launch {
            val result = postUseCases.reportPost(id, reportType, content)
            when(result){
                is Resource.Success -> {
                    result.data?.let{
                        _state.update { UiState.Success }
                        _event.emit(UiEvent.ShowSnackbar(result.data))
                    }
                }
                is Resource.Error -> {
                    _state.update { UiState.Error(result.message) }
                    _event.emit(UiEvent.ShowSnackbar(result.message ?: "Unknown error"))
                }
                is Resource.Loading -> {
                    _state.update { UiState.Loading }
                }
            }
        }
    }

    private fun navigateUp(route: Screen) {
        viewModelScope.launch {
            _event.emit(UiEvent.Navigate(route))
        }
    }

    private fun popBackStack() {
        viewModelScope.launch {
            _event.emit(UiEvent.PopBackStack)
        }
    }

    private fun showSnackbar(message: String) {
        viewModelScope.launch {
            _event.emit(UiEvent.ShowSnackbar(message))
        }
    }
}