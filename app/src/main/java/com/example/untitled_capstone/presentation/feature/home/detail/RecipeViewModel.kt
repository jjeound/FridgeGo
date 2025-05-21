package com.example.untitled_capstone.presentation.feature.home.detail

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.model.Recipe
import com.example.untitled_capstone.domain.use_case.home.DeleteRecipeUseCase
import com.example.untitled_capstone.domain.use_case.home.GetRecipeByIdUseCase
import com.example.untitled_capstone.domain.use_case.home.RecipeToggleLikeUseCase
import com.example.untitled_capstone.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val getRecipeByIdUseCase: GetRecipeByIdUseCase,
    private val recipeToggleLikeUseCase: RecipeToggleLikeUseCase,
    private val deleteRecipeUseCase: DeleteRecipeUseCase,
): ViewModel() {
    val uiState: MutableStateFlow<RecipeUiState> =
        MutableStateFlow(RecipeUiState.Loading)

    private val _recipe = MutableStateFlow<Recipe?>(null)
    val recipe: StateFlow<Recipe?> = _recipe.asStateFlow()

    private val _event = MutableSharedFlow<RecipeEvent>()
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
                        _event.emit(RecipeEvent.ShowSnackbar(it.message ?: "Unknown error"))
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
                        _event.emit(RecipeEvent.ShowSnackbar(it.message ?: "Unknown error"))
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
                        uiState.tryEmit(RecipeUiState.Idle)
                        _event.emit(RecipeEvent.ClearBackStack)
                    }
                    is Resource.Error -> {
                        uiState.tryEmit(RecipeUiState.Error(it.message))
                        _event.emit(RecipeEvent.ShowSnackbar(it.message ?: "Unknown error"))
                    }
                    is Resource.Loading -> {
                        uiState.tryEmit(RecipeUiState.Loading)
                    }
                }
            }
        }
    }

    fun popBackStack(){
        viewModelScope.launch {
            _event.emit(RecipeEvent.PopBackStack)
        }
    }

    fun showSnackbar(message: String){
        viewModelScope.launch {
            _event.emit(RecipeEvent.ShowSnackbar(message))
        }
    }

    fun navigateUp(route: Screen){
        viewModelScope.launch {
            _event.emit(RecipeEvent.Navigate(route))
        }
    }
}

@Stable
interface RecipeUiState{
    data object Idle: RecipeUiState
    data object Loading: RecipeUiState
    data class Error(val message: String?): RecipeUiState
}

interface RecipeEvent{
    data class ShowSnackbar(val message: String) : RecipeEvent
    data class Navigate(val route: Screen) : RecipeEvent
    object PopBackStack : RecipeEvent
    object ClearBackStack : RecipeEvent
}