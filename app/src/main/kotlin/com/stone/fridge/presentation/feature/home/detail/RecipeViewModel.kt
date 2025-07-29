package com.stone.fridge.presentation.feature.home.detail

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stone.fridge.core.util.Resource
import com.stone.fridge.domain.model.Recipe
import com.stone.fridge.domain.use_case.home.DeleteRecipeUseCase
import com.stone.fridge.domain.use_case.home.GetRecipeByIdUseCase
import com.stone.fridge.domain.use_case.home.ModifyRecipeUseCase
import com.stone.fridge.domain.use_case.home.RecipeToggleLikeUseCase
import com.stone.fridge.domain.use_case.home.UploadRecipeImageUseCase
import com.stone.fridge.presentation.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val getRecipeByIdUseCase: GetRecipeByIdUseCase,
    private val recipeToggleLikeUseCase: RecipeToggleLikeUseCase,
    private val deleteRecipeUseCase: DeleteRecipeUseCase,
    private val uploadRecipeImageUseCase: UploadRecipeImageUseCase,
    private val modifyRecipeUseCase: ModifyRecipeUseCase,
): ViewModel() {
    val uiState: MutableStateFlow<RecipeUiState> =
        MutableStateFlow(RecipeUiState.Loading)

    private val _recipe = MutableStateFlow<Recipe?>(null)
    val recipe: StateFlow<Recipe?> = _recipe.asStateFlow()

    private val _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

    fun getRecipeById(id: Long){
        viewModelScope.launch {
            getRecipeByIdUseCase(id).collectLatest {
                when(it){
                    is Resource.Success -> {
                        _recipe.value = it.data
                        uiState.tryEmit(RecipeUiState.Idle)
                    }
                    is Resource.Error -> {
                        uiState.tryEmit(RecipeUiState.Error(it.message))
                        _event.emit(UiEvent.ShowSnackbar(it.message ?: "Unknown error"))
                    }
                    is Resource.Loading -> {
                        uiState.tryEmit(RecipeUiState.Loading)
                    }
                }
            }
        }
    }

    fun toggleLike(id: Long, liked: Boolean){
        viewModelScope.launch {
            recipeToggleLikeUseCase(id, liked).collectLatest {
                when(it){
                    is Resource.Success -> {
                        _recipe.value = _recipe.value?.copy(liked = it.data!!)
                        uiState.tryEmit(RecipeUiState.Idle)
                    }
                    is Resource.Error -> {
                        uiState.tryEmit(RecipeUiState.Error(it.message))
                        _event.emit(UiEvent.ShowSnackbar(it.message ?: "Unknown error"))
                    }
                    is Resource.Loading -> {
                        uiState.tryEmit(RecipeUiState.Loading)
                    }
                }
            }
        }
    }

    fun deleteRecipe(id: Long){
        viewModelScope.launch {
            deleteRecipeUseCase(id).collectLatest {
                when(it){
                    is Resource.Success -> {
                        uiState.tryEmit(RecipeUiState.Success)
                    }
                    is Resource.Error -> {
                        uiState.tryEmit(RecipeUiState.Error(it.message))
                        _event.emit(UiEvent.ShowSnackbar(it.message ?: "Unknown error"))
                    }
                    is Resource.Loading -> {
                        uiState.tryEmit(RecipeUiState.Loading)
                    }
                }
            }
        }
    }

    fun uploadImageThenModify(recipe: Recipe, imageFile: File?){
        viewModelScope.launch {
            uiState.tryEmit(RecipeUiState.Loading)

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
                        uiState.tryEmit(RecipeUiState.Success)
                    }
                    is Resource.Error -> {
                        uiState.tryEmit(RecipeUiState.Error(it.message))
                        _event.emit(UiEvent.ShowSnackbar(it.message ?: "Unknown error"))
                    }
                    is Resource.Loading -> {
                        uiState.tryEmit(RecipeUiState.Loading)
                    }
                }
            }
        }
    }
}

@Stable
sealed interface RecipeUiState{
    data object Idle: RecipeUiState
    data object Success: RecipeUiState
    data object Loading: RecipeUiState
    data class Error(val message: String?): RecipeUiState
}