package com.stone.fridge.presentation.feature.post.crud

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stone.fridge.core.util.Resource
import com.stone.fridge.domain.model.NewPost
import com.stone.fridge.domain.use_case.post.AddPostUseCase
import com.stone.fridge.domain.use_case.post.DeletePostImageUseCase
import com.stone.fridge.domain.use_case.post.ModifyPostUseCase
import com.stone.fridge.domain.use_case.post.UploadPostImagesUseCase
import com.stone.fridge.presentation.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PostCRUDViewModel @Inject constructor(
    private val addPostUseCase: AddPostUseCase,
    private val modifyPostUseCase: ModifyPostUseCase,
    private val uploadPostImagesUseCase: UploadPostImagesUseCase,
    private val deletePostImageUseCase: DeletePostImageUseCase
): ViewModel()  {
    val uiState: MutableStateFlow<PostCRUDUiState> = MutableStateFlow<PostCRUDUiState>(PostCRUDUiState.Idle)

    private val _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()


    fun addNewPost(post: NewPost, images: List<File>? = null){
        viewModelScope.launch {
            addPostUseCase(post, images).collectLatest{
                when(it){
                    is Resource.Success -> {
                        uiState.tryEmit(PostCRUDUiState.Success)
                    }
                    is Resource.Error -> {
                        uiState.tryEmit(PostCRUDUiState.Error(it.message))
                        _event.emit(UiEvent.ShowSnackbar(it.message ?: "Unknown error"))
                    }
                    is Resource.Loading -> {
                        uiState.tryEmit(PostCRUDUiState.Loading)
                    }
                }
            }
        }
    }

    fun modifyPost(id: Long, newPost: NewPost, images: List<File>) {
        viewModelScope.launch {
            if (images.isNotEmpty()) {
                uploadPostImagesUseCase(id, images).collectLatest{
                    if (it is Resource.Error) {
                        _event.emit(UiEvent.ShowSnackbar(it.message ?: "이미지 업로드 실패했지만 계속 진행합니다."))
                    }
                }
            }
            // 이미지 업로드가 성공했거나 없었으면 수정 요청
            modifyPostUseCase(id, newPost).collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        uiState.tryEmit(PostCRUDUiState.Success)
                    }
                    is Resource.Error -> {
                        uiState.tryEmit(PostCRUDUiState.Error(result.message))
                        _event.emit(UiEvent.ShowSnackbar(result.message ?: "Unknown error"))
                    }
                    is Resource.Loading -> {
                        uiState.tryEmit(PostCRUDUiState.Loading)
                    }
                }
            }
        }
    }

    fun deletePostImage(id: Long, imageId: Long){
        viewModelScope.launch {
            deletePostImageUseCase(id, imageId).collectLatest{
                when(it){
                    is Resource.Success -> {
                        uiState.tryEmit(PostCRUDUiState.Idle)
                    }
                    is Resource.Error -> {
                        uiState.tryEmit(PostCRUDUiState.Error(it.message))
                        _event.emit(UiEvent.ShowSnackbar(it.message ?: "Unknown error"))
                    }
                    is Resource.Loading -> {
                        uiState.tryEmit(PostCRUDUiState.Loading)
                    }
                }
            }
        }
    }
}

@Stable
interface PostCRUDUiState{
    data object Idle: PostCRUDUiState
    data object Success: PostCRUDUiState
    data object Loading: PostCRUDUiState
    data class Error(val message: String?): PostCRUDUiState
}