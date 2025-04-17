package com.example.untitled_capstone.presentation.feature.post

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.util.PostFetchType
import com.example.untitled_capstone.domain.model.Keyword
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
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postUseCases: PostUseCases,
): ViewModel() {

    private val _state = MutableStateFlow(PostState())
    val state: StateFlow<PostState> = _state.asStateFlow()

    private val _postItemState: MutableStateFlow<PagingData<PostRaw>> = MutableStateFlow(PagingData.empty())
    val postItemState = _postItemState.asStateFlow()

    private val _searchState = mutableStateOf(SearchState())
    val searchState: State<SearchState> = _searchState

    private val _uploadState = MutableStateFlow(UploadState())
    val uploadState: StateFlow<UploadState> = _uploadState.asStateFlow()

    val nickname = mutableStateOf("")
    val keyword = mutableStateOf<List<Keyword>>(emptyList())

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
        }
    }

    init {
        onEvent(PostEvent.LoadItems)
        getNickname()
    }

    private fun getNickname(){
        viewModelScope.launch {
            val result = postUseCases.getNickname()
            nickname.value = result!!
        }
    }



    private fun deletePost(id: Long) {
        viewModelScope.launch {
            val result = postUseCases.deletePost(id)
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
                    _postItemState.update { pagingData->
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
                    _uploadState.update {
                        it.copy(
                            id = id
                        )
                    }
                }
                is Resource.Error -> {
                    _uploadState.update { it.copy(isLoading = false, error = result.message ?: "An unexpected error occurred") }
                }
                is Resource.Loading -> {
                    _uploadState.update { it.copy(isLoading = true) }
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
            _searchState.value.isLoading.value = true
            postUseCases.searchPosts(PostFetchType.Search(keyword))
                .distinctUntilChanged()
                .cachedIn(viewModelScope)
                .collect { pagingData ->
                    _searchState.value.searchResult.value = pagingData
                }
            _searchState.value.isLoading.value = false
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

    private fun addNewPost(post: NewPost, images: List<File>? = null){
        viewModelScope.launch {
            val result = postUseCases.addPost(post, images)
            when(result){
                is Resource.Success -> {
                    loadItems()
                    _uploadState.update {
                        it.copy(
                            isSuccess = true,
                            isLoading = false,
                            error = null
                        )
                    }
                }
                is Resource.Error -> {
                    _uploadState.update { it.copy(isLoading = false, error = result.message ?: "An unexpected error occurred") }
                }
                is Resource.Loading -> {
                    _uploadState.update { it.copy(isLoading = true) }
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
                        _postItemState.update { pagingData ->
                            pagingData.map {
                                if(it.id == id){
                                    it.copy( likeCount = if(result.data) it.likeCount + 1 else it.likeCount - 1, liked = result.data)
                                }else{
                                    it
                                }
                            }
                        }
                        _state.update {
                            it.copy(
                                post = it.post?.copy(
                                    likeCount = if(result.data) it.post.likeCount + 1 else it.post.likeCount - 1,
                                    liked = result.data
                                ),
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

    private fun initState(){
        _state.update {
            it.copy(
                post = null,
                isLoading = false,
                error = null
            )
        }
        _uploadState.update {
            it.copy(
                id = null,
                isSuccess = false,
                isLoading = false,
                error = null
            )
        }
    }

    private fun uploadPostImages(id: Long, images: List<File>){
        viewModelScope.launch {
            val result = postUseCases.uploadPostImages(id, images)
            when(result){
                is Resource.Success -> {
                    result.data.let{
                        _uploadState.update {
                            it.copy(
                                isSuccess = true,
                                isLoading = false,
                                error = null
                            )
                        }
                    }
                }
                is Resource.Error -> {
                    _uploadState.update { it.copy(isLoading = false, error = result.message ?: "An unexpected error occurred") }
                }
                is Resource.Loading -> {
                    _uploadState.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    private fun deletePostImage(id: Long, imageId: Long) {
        viewModelScope.launch {
            val result = postUseCases.deletePostImage(id, imageId)
            when(result){
                is Resource.Success -> {
                    result.data?.let{
                        _state.update {
                            it.copy(
                                isSuccess = true,
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

    private fun getSearchHistory(){
        viewModelScope.launch {
            val result = postUseCases.getSearchHistory()
            when(result){
                is Resource.Success -> {
                    result.data?.let{
                        keyword.value = it
                        _state.update {
                            it.copy(
                                isSuccess = true,
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

    private fun deleteSearchHistory(word: String){
        viewModelScope.launch {
            val result = postUseCases.deleteSearchHistory(word)
            when(result){
                is Resource.Success -> {
                    result.data?.let{
                        keyword.value = keyword.value.filter { it.keyword != word }
                        _state.update {
                            it.copy(
                                isSuccess = true,
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
    private fun deleteAllSearchHistory(){
        viewModelScope.launch {
            val result = postUseCases.deleteAllSearchHistory()
            when(result){
                is Resource.Success -> {
                    result.data?.let{
                        keyword.value = emptyList()
                        _state.update {
                            it.copy(
                                isSuccess = true,
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

    private fun addSearchHistory(word: Keyword){
        keyword.value = listOf(word) + keyword.value
    }

    private fun reportPost(id: Long, reportType: String, content: String) {
        viewModelScope.launch {
            val result = postUseCases.reportPost(id, reportType, content)
            when(result){
                is Resource.Success -> {
                    result.data?.let{
                        _state.update { it.copy(isLoading = false, error = null) }
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