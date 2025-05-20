package com.example.untitled_capstone.presentation.feature.home.modify

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.model.Recipe
import com.example.untitled_capstone.domain.use_case.home.ModifyRecipeUseCase
import com.example.untitled_capstone.domain.use_case.home.UploadRecipeImageUseCase
import com.example.untitled_capstone.presentation.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class RecipeModifyViewModel @Inject constructor(
    private val uploadRecipeImageUseCase: UploadRecipeImageUseCase,
    private val modifyRecipeUseCase: ModifyRecipeUseCase,
): ViewModel()  {
    val uiState: MutableStateFlow<RecipeModifyUiState> =
        MutableStateFlow(RecipeModifyUiState.Idle)

    private val _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

    fun uploadImageThenModify(recipe: Recipe, imageFile: File?){
        viewModelScope.launch {
            uiState.tryEmit(RecipeModifyUiState.Loading)

            imageFile?.let {
                uploadRecipeImageUseCase(recipe.id, it).collectLatest {
                    if (it is Resource.Error) {
                        _event.emit(UiEvent.ShowSnackbar(it.message ?: "이미지 업로드 실패했지만 계속 진행합니다."))
                    }
                }
            }
            modifyRecipeUseCase(recipe).collectLatest {
                when(it){
                    is Resource.Success -> {
                        uiState.tryEmit(RecipeModifyUiState.Success)
                        _event.emit(UiEvent.PopBackStack)
                    }
                    is Resource.Error -> {
                        uiState.tryEmit(RecipeModifyUiState.Error(it.message))
                        _event.emit(UiEvent.ShowSnackbar(it.message ?: "Unknown error"))
                    }
                    is Resource.Loading -> {
                        uiState.tryEmit(RecipeModifyUiState.Loading)
                    }
                }
            }
        }
    }

    fun popBackStack(){
        viewModelScope.launch {
            _event.emit(UiEvent.PopBackStack)
        }
    }

}

@Stable
interface RecipeModifyUiState{
    data object Idle: RecipeModifyUiState
    data object Success: RecipeModifyUiState
    data object Loading: RecipeModifyUiState
    data class Error(val message: String?): RecipeModifyUiState
}