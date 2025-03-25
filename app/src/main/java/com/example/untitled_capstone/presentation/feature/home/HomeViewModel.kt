package com.example.untitled_capstone.presentation.feature.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.model.RecipeRaw
import com.example.untitled_capstone.domain.model.TastePreference
import com.example.untitled_capstone.domain.use_case.home.HomeUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeUseCases: HomeUseCases
): ViewModel(){
    private val _tastePrefState = MutableStateFlow(TastePrefState())
    val tastePrefState: StateFlow<TastePrefState> = _tastePrefState.asStateFlow()

    private val _recipeState = MutableStateFlow(RecipeState())
    val recipeState: StateFlow<RecipeState> = _recipeState.asStateFlow()

    private val _aiState = MutableStateFlow("")
    val aiState: StateFlow<String> = _aiState.asStateFlow()

    private val _recipeItemsState: MutableStateFlow<PagingData<RecipeRaw>> = MutableStateFlow(PagingData.empty())
    val recipeItemsState = _recipeItemsState.asStateFlow()

    init {
        getTastePreference()
        getRecipes()
    }

    fun onAction(action: HomeEvent){
        when(action){
            is HomeEvent.ToggleLike -> toggleLike(action.id)
            is HomeEvent.GetRecipes -> getRecipes()
            is HomeEvent.GetFirstRecipeByAI -> getFirstRecipeByAI()
            is HomeEvent.GetAnotherRecipeByAI -> getAnotherRecipeByAI()
            is HomeEvent.AddRecipe -> addRecipe(action.title, action.instructions)
            is HomeEvent.SetTastePreference -> setTastePreference(action.tastePreference)
            is HomeEvent.GetTastePreference -> getTastePreference()
        }
    }

    private fun getTastePreference() {
        viewModelScope.launch {
            val result = homeUseCases.getTastePreference()
            Log.d("get", result.data.toString())
            when(result){
                is Resource.Success -> {
                    result.data?.let{
                        _tastePrefState.update {
                            it.copy(
                                tastePref = result.data.tastePreference,
                                loading = false,
                                error = null
                            )
                        }
                    }
                }
                is Resource.Error -> {
                    _tastePrefState.update { it.copy(loading = false, error = result.message ?: "An unexpected error occurred") }
                }
                is Resource.Loading -> {
                    _tastePrefState.update { it.copy(loading = true) }
                }
            }
        }
    }

    private fun setTastePreference(tastePreference: String) {
        viewModelScope.launch {
            val result = homeUseCases.setTastePreference(TastePreference(tastePreference))
            Log.d("set", result.data.toString())
            when(result){
                is Resource.Success -> {
                    result.data?.let{
                        _tastePrefState.update {
                            it.copy(
                                loading = false,
                                error = null
                            )
                        }
                    }
                }
                is Resource.Error -> {
                    _tastePrefState.update { it.copy(loading = false, error = result.message ?: "An unexpected error occurred") }
                }
                is Resource.Loading -> {
                    _tastePrefState.update { it.copy(loading = true) }
                }
            }
        }
    }

    private fun addRecipe(title: String, instructions: String) {
        viewModelScope.launch {
            val result = homeUseCases.addRecipe(title, instructions)
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

    private fun getAnotherRecipeByAI() {
        viewModelScope.launch {
            val result = homeUseCases.getAnotherRecommendation()
            when(result){
                is Resource.Success -> {
                    result.data?.let{
                        _aiState.update {
                            it
                        }
                    }
                }
                is Resource.Error -> {
                    _aiState.update { "" }
                    Log.d("error", result.message.toString())
                }
                is Resource.Loading -> {
                    _aiState.update { "" }
                }
            }
        }
    }

    private fun getFirstRecipeByAI() {
        viewModelScope.launch {
            val result = homeUseCases.getFirstRecommendation()
            when(result){
                is Resource.Success -> {
                    result.data?.let{
                        _aiState.update {
                            it
                        }
                    }
                }
                is Resource.Error -> {
                    _aiState.update { "" }
                    Log.d("error", result.message.toString())
                }
                is Resource.Loading -> {
                    _aiState.update { "" }
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

    private fun toggleLike(id: Long){
//        _state.update { currentState ->
//            val updatedPosts = currentState.recipeItems.map { recipe ->
//                if (recipe.id == id) {
//                    recipe.copy(
//                        isLiked = !recipe.isLiked,
//                    )
//                } else recipe
//            }
//            currentState.copy(recipeItems = updatedPosts)
//        }
    }
}