package com.example.untitled_capstone.presentation.feature.home

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.model.Recipe
import com.example.untitled_capstone.domain.model.RecipeRaw
import com.example.untitled_capstone.domain.model.TastePreference
import com.example.untitled_capstone.domain.use_case.home.HomeUseCases
import com.example.untitled_capstone.presentation.feature.home.state.AiState
import com.example.untitled_capstone.presentation.feature.home.state.ModifyState
import com.example.untitled_capstone.presentation.feature.home.state.RecipeState
import com.example.untitled_capstone.presentation.feature.home.state.TastePrefState
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
class HomeViewModel @Inject constructor(
    private val homeUseCases: HomeUseCases
): ViewModel(){
    private val _tastePrefState = MutableStateFlow(TastePrefState())
    val tastePrefState: StateFlow<TastePrefState> = _tastePrefState.asStateFlow()

    private val _recipeState = MutableStateFlow(RecipeState())
    val recipeState: StateFlow<RecipeState> = _recipeState.asStateFlow()

    private val _aiState = mutableStateOf(AiState())
    val aiState: State<AiState> = _aiState

    private val _modifyState = MutableStateFlow(ModifyState())
    val modifyState: StateFlow<ModifyState> = _modifyState

    private val _recipeItemsState: MutableStateFlow<PagingData<RecipeRaw>> = MutableStateFlow(PagingData.empty())
    val recipeItemsState = _recipeItemsState.asStateFlow()

    init {
        getTastePreference()
        getRecipes()
    }

    fun onEvent(event: HomeEvent){
        when(event){
            is HomeEvent.ToggleLike -> toggleLike(event.id, event.liked)
            is HomeEvent.GetRecipes -> getRecipes()
            is HomeEvent.GetRecipeByAi -> getRecipeByAI()
            is HomeEvent.GetRecipeById -> getRecipeById(event.id)
            is HomeEvent.AddRecipe -> addRecipe(event.recipe)
            is HomeEvent.SetTastePreference -> setTastePreference(event.tastePreference)
            is HomeEvent.GetTastePreference -> getTastePreference()
            is HomeEvent.InitState -> initState()
            is HomeEvent.DeleteRecipe -> deleteRecipe(event.id)
            is HomeEvent.ModifyRecipe -> modifyRecipe(event.recipe)
            is HomeEvent.UploadImage -> uploadImage(event.id, event.file)
        }
    }

    private fun initState(){
        _recipeState.update {
            it.copy(
                recipe = null,
                loading = false,
                error = null
            )
        }
        _modifyState.update {
            it.copy(
                isSuccess = false,
                loading = false,
                error = null
            )
        }
        _tastePrefState.update {
            it.copy(
                isSuccess = false,
                loading = false,
                error = null
            )
        }
    }

    private fun getTastePreference() {
        viewModelScope.launch {
            val result = homeUseCases.getTastePreference()
            when(result){
                is Resource.Success -> {
                    result.data?.let{
                        _tastePrefState.update {
                            it.copy(
                                tastePref = result.data.tastePreference,
                                isSuccess = false,
                                loading = false,
                                error = null
                            )
                        }
                    }
                }
                is Resource.Error -> {
                    _tastePrefState.update { it.copy(isSuccess = false, loading = false, error = result.message ?: "An unexpected error occurred") }
                }
                is Resource.Loading -> {
                    _tastePrefState.update { it.copy(isSuccess = false, loading = true) }
                }
            }
        }
    }

    private fun setTastePreference(tastePreference: String) {
        viewModelScope.launch {
            val result = homeUseCases.setTastePreference(TastePreference(tastePreference))
            when(result){
                is Resource.Success -> {
                    result.data?.let{
                        _tastePrefState.update {
                            it.copy(
                                isSuccess = true,
                                loading = false,
                                error = null
                            )
                        }
                    }
                }
                is Resource.Error -> {
                    _tastePrefState.update { it.copy(isSuccess = false, loading = false, error = result.message ?: "An unexpected error occurred") }
                }
                is Resource.Loading -> {
                    _tastePrefState.update { it.copy(isSuccess = false, loading = true) }
                }
            }
        }
    }

    private fun addRecipe(recipe: String) {
        viewModelScope.launch {
            val result = homeUseCases.addRecipe(recipe)
            when(result){
                is Resource.Success -> {
                    result.data?.let{
                        getRecipes()
                    }
                }
                is Resource.Error -> {
                    _recipeState.update { it.copy(loading = false, error = result.message ?: "An unexpected error occurred") }
                }
                is Resource.Loading -> {
                    _recipeState.update { it.copy(loading = true) }
                }
            }
        }
    }

    private fun getRecipeByAI() {
        viewModelScope.launch {
            aiState.value.isLoading.value = true
            val isFirstSelection = homeUseCases.getIsFirstSelection()
            val result = if (isFirstSelection) {
                homeUseCases.getFirstRecommendation()
            } else {
                homeUseCases.getAnotherRecommendation()
            }

            when (result) {
                is Resource.Success -> {
                    result.data?.let {
                        _aiState.value.response.value = _aiState.value.response.value + it
                        _aiState.value.error.value = ""
                        _aiState.value.isLoading.value = false
                        homeUseCases.setIsFirstSelection(false) // 첫 선택 완료 처리
                    }
                }
                is Resource.Error -> {
                    _aiState.value.error.value = result.message.toString()
                    _aiState.value.isLoading.value = false
                }
                is Resource.Loading -> {
                    _aiState.value.error.value = ""
                    _aiState.value.isLoading.value = true
                }
            }
        }
    }

    private fun getRecipes() {
        viewModelScope.launch {
            homeUseCases.getRecipeItems()
                .distinctUntilChanged()
                .cachedIn(viewModelScope)
                .collect { pagingData ->
                    _recipeItemsState.value = pagingData
                    _recipeState.update {
                        it.copy(
                            loading = false,
                            error = null
                        )
                    }
                }
        }
    }

    private fun getRecipeById(id: Long){
        viewModelScope.launch {
            val result = homeUseCases.getRecipeById(id)
            when(result){
                is Resource.Success -> {
                    result.data?.let{
                        _recipeState.update { it.copy(
                            recipe = result.data,
                            loading = false,
                            error = null
                        ) }
                    }
                }
                is Resource.Error -> {
                    _recipeState.update { it.copy(loading = false, error = result.message ?: "An unexpected error occurred") }
                }
                is Resource.Loading -> {
                    _recipeState.update { it.copy(loading = true) }
                }
            }
        }
    }

    private fun toggleLike(id: Long, liked: Boolean){
        viewModelScope.launch {
            val result = homeUseCases.toggleLike(id, liked)
            when(result){
                is Resource.Success -> {
                    result.data?.let{
                        _recipeItemsState.update { pagingData ->
                            pagingData.map {
                                if(it.id == id){
                                    it.copy(liked = result.data.liked)
                                }else{
                                    it
                                }
                            }
                        }
                        _recipeState.update {
                            it.copy(
                                recipe = it.recipe?.copy(liked = result.data.liked),
                                loading = false,
                                error = null
                            )
                        }
                    }
                }
                is Resource.Error -> {
                    _recipeState.update { it.copy(loading = false, error = result.message ?: "An unexpected error occurred") }
                }
                is Resource.Loading -> {
                    _recipeState.update { it.copy(loading = true) }
                }
            }
        }
    }

    private fun deleteRecipe(id: Long){
        viewModelScope.launch {
            val result = homeUseCases.deleteRecipe(id)
            when(result){
                is Resource.Success -> {
                    result.data?.let{
                        getRecipes()
                    }
                }
                is Resource.Error -> {
                    _recipeState.update { it.copy(loading = false, error = result.message ?: "An unexpected error occurred") }
                }
                is Resource.Loading -> {
                    _recipeState.update { it.copy(loading = true) }
                }
            }
        }
    }

    private fun modifyRecipe(recipe: Recipe){
        viewModelScope.launch {
            val result = homeUseCases.modifyRecipe(recipe)
            when(result){
                is Resource.Success -> {
                    result.data?.let {
                        _modifyState.update {
                            it.copy(
                                isSuccess = true,
                                loading = false,
                                error = null
                            )
                        }
                        getRecipes()
                    }
                }
                is Resource.Error -> {
                    _recipeState.update { it.copy(loading = false, error = result.message ?: "An unexpected error occurred") }
                }
                is Resource.Loading -> {
                    _recipeState.update { it.copy(loading = true) }
                }
            }
        }
    }

    private fun uploadImage(id: Long, file: File) {
        viewModelScope.launch {
            val result = homeUseCases.uploadImage(id, file)
            when(result){
                is Resource.Success -> {
                    result.data?.let{
                        _recipeState.update {
                            it.copy(
                                loading = false,
                                error = null
                            )
                        }
                    }
                }
                is Resource.Error -> {
                    _recipeState.update { it.copy(loading = false, error = result.message ?: "An unexpected error occurred") }
                }
                is Resource.Loading -> {
                    _recipeState.update { it.copy(loading = true) }
                }
            }
        }
    }
}