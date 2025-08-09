package com.stone.fridge.feature.home.detail

import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stone.fridge.core.data.home.HomeRepository
import com.stone.fridge.core.data.util.Resource
import com.stone.fridge.core.data.util.asResult
import com.stone.fridge.core.model.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    savedStateHandle: SavedStateHandle,
): ViewModel() {
    val uiState: MutableStateFlow<RecipeUiState> =
        MutableStateFlow(RecipeUiState.Loading)

    val recipeId = savedStateHandle.getStateFlow<Long?>("id", null)

    private val _recipe = MutableStateFlow<Recipe?>(null)
    val recipe: StateFlow<Recipe?> = _recipe.asStateFlow()

    init {
        recipeId.filterNotNull()
            .flatMapLatest { id ->
                homeRepository.getRecipeById(id)
            }
            .onStart { uiState.value = RecipeUiState.Loading }
            .onCompletion { uiState.value = RecipeUiState.Idle }
            .onEach { _recipe.value = it }
            .catch { uiState.value = RecipeUiState.Error(it.message ?: "알 수 없는 오류가 발생했어요.") }
            .launchIn(viewModelScope)
    }

    fun toggleLike(id: Long, liked: Boolean){
        viewModelScope.launch {
            homeRepository.toggleLike(id, liked)
                .asResult()
                .collectLatest {
                    when(it){
                        is Resource.Success -> {
                            _recipe.value = _recipe.value?.copy(liked = it.data)
                            uiState.tryEmit(RecipeUiState.Idle)
                        }
                        is Resource.Error -> {
                            uiState.tryEmit(RecipeUiState.Error(it.message))
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
            homeRepository.deleteRecipe(id)
                .asResult()
                .collectLatest {
                    when(it){
                        is Resource.Success -> {
                            uiState.tryEmit(RecipeUiState.Success)
                        }
                        is Resource.Error -> {
                            uiState.tryEmit(RecipeUiState.Error(it.message))
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
            imageFile?.let { image ->
                homeRepository.uploadImage(recipe.id, image).combine(
                    homeRepository.modifyRecipe(recipe), ::Pair
                )
                    .asResult()
                    .collectLatest { result ->
                        when(result){
                            is Resource.Success -> {
                                uiState.emit(RecipeUiState.Success)
                            }
                            is Resource.Error -> {
                                uiState.emit(RecipeUiState.Error(result.message))
                            }
                            is Resource.Loading -> {
                                uiState.emit(RecipeUiState.Loading)
                            }
                        }
                    }
            } ?: run {
                homeRepository.modifyRecipe(recipe)
                    .asResult()
                    .collectLatest { result ->
                        when(result){
                            is Resource.Success -> {
                                uiState.emit(RecipeUiState.Success)
                            }
                            is Resource.Error -> {
                                uiState.emit(RecipeUiState.Error(result.message))
                            }
                            is Resource.Loading -> {
                                uiState.emit(RecipeUiState.Loading)
                            }
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
    data class Error(val message: String): RecipeUiState
}