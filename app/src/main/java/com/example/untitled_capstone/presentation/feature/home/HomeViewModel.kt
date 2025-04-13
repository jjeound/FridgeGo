package com.example.untitled_capstone.presentation.feature.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import com.example.untitled_capstone.navigation.Screen
import com.example.untitled_capstone.presentation.feature.home.state.AiState
import com.example.untitled_capstone.presentation.feature.home.state.ModifyState
import com.example.untitled_capstone.presentation.feature.home.state.RecipeState
import com.example.untitled_capstone.presentation.feature.home.state.TastePrefState
import com.example.untitled_capstone.presentation.util.UIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeUseCases: HomeUseCases,
): ViewModel(){
    var tastePrefState by mutableStateOf(TastePrefState())
        private set

    var recipeState by mutableStateOf(RecipeState())
        private set

    var aiState by mutableStateOf(AiState())
        private set

    private val _modifyState = MutableStateFlow(ModifyState())
    val modifyState: StateFlow<ModifyState> = _modifyState

    private val _recipePagingData: MutableStateFlow<PagingData<RecipeRaw>> = MutableStateFlow(PagingData.empty<RecipeRaw>())
    val recipePagingData = _recipePagingData.asStateFlow()

    private val _event = MutableSharedFlow<UIEvent>()
    val event = _event.asSharedFlow()

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
        recipeState = RecipeState()
        _modifyState.update {
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
                        tastePrefState.apply{
                            data = it.tastePreference
                            isLoading = false
                        }
                    }
                }
                is Resource.Error -> {
                    tastePrefState.isLoading = false
                    _event.emit(UIEvent.ShowSnackbar(result.message ?: "Unknown error"))
                }
                is Resource.Loading -> {
                    tastePrefState.isLoading = true
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
                        tastePrefState.apply{
                            isLoading = false
                        }
                        _event.emit(UIEvent.ShowSnackbar(result.data.result!!))
                    }
                }
                is Resource.Error -> {
                    tastePrefState.isLoading = false
                    _event.emit(UIEvent.ShowSnackbar(result.message ?: "Unknown error"))
                }
                is Resource.Loading -> {
                    tastePrefState.isLoading = true
                }
            }
        }
    }

    private fun addRecipe(recipe: String) {
        viewModelScope.launch {
            val result = homeUseCases.addRecipe(recipe)
            when(result){
                is Resource.Success -> {
                    result.data?.let {
                        recipeState.isLoading = false
                        //_event.emit(UIEvent.ShowSnackbar(result.data.result!!))
                        getRecipes()
                    }
                }
                is Resource.Error -> {
                    recipeState.isLoading = false
                    _event.emit(UIEvent.ShowSnackbar(result.message ?: "Unknown error"))
                }
                is Resource.Loading -> {
                    recipeState.isLoading = true
                }
            }
        }
    }

    private fun getRecipeByAI() {
        viewModelScope.launch {
            val isFirstSelection = homeUseCases.getIsFirstSelection()
            val result = if (isFirstSelection) {
                homeUseCases.getFirstRecommendation()
            } else {
                homeUseCases.getAnotherRecommendation()
            }
            when (result) {
                is Resource.Success -> {
                    result.data?.let {
                        aiState.apply {
                            response += it
                            isLoading = false
                        }
                        homeUseCases.setIsFirstSelection(false) // 첫 선택 완료 처리
                    }
                }
                is Resource.Error -> {
                    aiState.isLoading = false
                    _event.emit(UIEvent.ShowSnackbar(result.message ?: "다시 시도해주세요."))
                }
                is Resource.Loading -> {
                    aiState.isLoading = true
                }
            }
        }
    }

    private fun getRecipes(){
        viewModelScope.launch {
            homeUseCases.getRecipeItems()
                .cachedIn(viewModelScope)
                .collectLatest { pagingData ->
                    _recipePagingData.value = pagingData
                }
        }
    }

    private fun getRecipeById(id: Long){
        viewModelScope.launch {
            val result = homeUseCases.getRecipeById(id)
            when(result){
                is Resource.Success -> {
                    result.data?.let{
                        recipeState.apply {
                            recipe = it
                            isLoading = false
                        }
                    }
                }
                is Resource.Error -> {
                    recipeState.isLoading = false
                    _event.emit(UIEvent.ShowSnackbar(result.message ?: "Unknown error"))
                }
                is Resource.Loading -> {
                    recipeState.isLoading = true
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
                        recipeState.isLoading = false
                        _recipePagingData.update { pagingData ->
                            pagingData.map {
                                if(it.id == id){
                                    it.copy(liked = result.data.liked)
                                }else{
                                    it
                                }
                            }
                        }
                        recipeState.recipe = recipeState.recipe?.copy(
                            liked = result.data.liked
                        )
                    }
                }
                is Resource.Error -> {
                    recipeState.isLoading = false
                    _event.emit(UIEvent.ShowSnackbar(result.message ?: "Unknown error"))
                }
                is Resource.Loading -> {
                    recipeState.isLoading = true
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
                        recipeState.isLoading = false
                        getRecipes()
                    }
                }
                is Resource.Error -> {
                    recipeState.isLoading = false
                    _event.emit(UIEvent.ShowSnackbar(result.message ?: "Unknown error"))
                }
                is Resource.Loading -> {
                    recipeState.isLoading = true
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
                        recipeState.isLoading = false
                        _recipePagingData.update { pagingData ->
                            pagingData.map {
                                if(it.id == recipe.id) {
                                    it.copy(
                                        title = recipe.title,
                                        imageUrl = recipe.imageUrl
                                    )
                                }else{
                                    it
                                }
                            }
                        }
                    }
                }
                is Resource.Error -> {
                    recipeState.isLoading = false
                    _event.emit(UIEvent.ShowSnackbar(result.message ?: "Unknown error"))
                }
                is Resource.Loading -> {
                    recipeState.isLoading = true
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
                        recipeState.isLoading = false
                    }
                }
                is Resource.Error -> {
                    recipeState.isLoading = false
                    _event.emit(UIEvent.ShowSnackbar(result.message ?: "Unknown error"))
                }
                is Resource.Loading -> {
                    recipeState.isLoading = true
                }
            }
        }
    }

    fun navigateUp(route: Screen) {
        viewModelScope.launch {
            _event.emit(UIEvent.Navigate(route))
        }
    }
}