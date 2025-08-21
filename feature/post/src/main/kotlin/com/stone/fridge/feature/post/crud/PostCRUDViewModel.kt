package com.stone.fridge.feature.post.crud

import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stone.fridge.core.data.post.PostRepository
import com.stone.fridge.core.data.util.Resource
import com.stone.fridge.core.data.util.asResult
import com.stone.fridge.core.model.ModifyPost
import com.stone.fridge.core.model.NewPost
import com.stone.fridge.core.model.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PostCRUDViewModel @Inject constructor(
    private val postRepository: PostRepository,
    savedStateHandle: SavedStateHandle
): ViewModel()  {
    internal val uiState: MutableStateFlow<PostCRUDUiState> = MutableStateFlow(PostCRUDUiState.Idle)

    val post = savedStateHandle.getStateFlow<Post?>("post", null)

    fun addNewPost(post: NewPost, images: List<File>? = null){
        viewModelScope.launch {
            postRepository.post(post, images)
                .asResult()
                .collectLatest{ result ->
                    when(result){
                        is Resource.Success -> uiState.emit(PostCRUDUiState.Success)
                        is Resource.Error -> uiState.emit(PostCRUDUiState.Error(result.message))
                        is Resource.Loading -> uiState.emit(PostCRUDUiState.Loading)
                    }
                }
        }
    }

    fun modifyPost(id: Long, modifyPost: ModifyPost, images: List<File>) {
        viewModelScope.launch {
            postRepository.uploadImages(id, images).asResult()
                .zip(postRepository.modifyPost(id, modifyPost).asResult()) { uploadResult, modifyResult ->
                    when {
                        uploadResult is Resource.Loading || modifyResult is Resource.Loading -> {
                            Resource.Loading
                        }
                        uploadResult is Resource.Error -> {
                            Resource.Error(uploadResult.message)
                        }
                        modifyResult is Resource.Error -> {
                            Resource.Error(modifyResult.message)
                        }
                        uploadResult is Resource.Success && modifyResult is Resource.Success -> {
                            Resource.Success(Unit)
                        }
                        else -> {
                            Resource.Error("알 수 없는 에러")
                        }
                    }
                }
                .collectLatest { result ->
                    when (result) {
                        is Resource.Success -> uiState.emit(PostCRUDUiState.Success)
                        is Resource.Error -> uiState.emit(PostCRUDUiState.Error(result.message))
                        is Resource.Loading -> uiState.emit(PostCRUDUiState.Loading)
                    }
                }
        }
    }

    fun deletePostImage(id: Long, imageId: Long){
        viewModelScope.launch {
            postRepository.deleteImage(id, imageId)
                .asResult()
                .collectLatest { result ->
                    when(result){
                        is Resource.Success -> uiState.emit(PostCRUDUiState.Idle)
                        is Resource.Error -> uiState.emit(PostCRUDUiState.Error(result.message))
                        is Resource.Loading -> uiState.emit(PostCRUDUiState.Loading)
                    }
                }
        }
    }
}

@Stable
internal sealed interface PostCRUDUiState{
    data object Idle: PostCRUDUiState
    data object Success: PostCRUDUiState
    data object Loading: PostCRUDUiState
    data class Error(val message: String): PostCRUDUiState
}