package com.example.untitled_capstone.presentation.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.untitled_capstone.R
import com.example.untitled_capstone.domain.model.Recipe
import com.example.untitled_capstone.presentation.feature.home.event.HomeAction
import com.example.untitled_capstone.presentation.feature.home.state.MyRecipeState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel(){

    private val _state = MutableStateFlow(MyRecipeState())
    val state: StateFlow<MyRecipeState> = _state.asStateFlow()

    fun onAction(action: HomeAction){
        when(action){
            is HomeAction.LoadItems -> loadItems()
            is HomeAction.AddNewRecipe -> addNewRecipe(action.recipe)
            is HomeAction.ToggleLike -> toggleLike(action.id)
        }
    }

    init {
        onAction(HomeAction.LoadItems)
    }

    private fun loadItems() {}
    private fun addNewRecipe(recipe: Recipe){
        _state.update {
            it.copy(
                recipeItems = it.recipeItems + recipe
            )
        }
    }

    private fun toggleLike(id: Long){
        _state.update { currentState ->
            val updatedPosts = currentState.recipeItems.map { recipe ->
                if (recipe.id == id) {
                    recipe.copy(
                        isLiked = !recipe.isLiked,
                    )
                } else recipe
            }
            currentState.copy(recipeItems = updatedPosts)
        }
    }
}