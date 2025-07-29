package com.stone.fridge.presentation.feature.home

import android.util.Log
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.stone.fridge.core.util.Resource
import com.stone.fridge.domain.model.RecipeRaw
import com.stone.fridge.domain.model.TastePreference
import com.stone.fridge.domain.use_case.home.AddRecipeUseCase
import com.stone.fridge.domain.use_case.home.GetAnotherRecommendationUseCase
import com.stone.fridge.domain.use_case.home.GetFirstRecommendationUseCase
import com.stone.fridge.domain.use_case.home.GetIsFirstSelectionUseCase
import com.stone.fridge.domain.use_case.home.GetRecipeItemsUseCase
import com.stone.fridge.domain.use_case.home.GetTastePreferenceUseCase
import com.stone.fridge.domain.use_case.home.RecipeToggleLikeUseCase
import com.stone.fridge.domain.use_case.home.SetIsFirstSelectionUseCase
import com.stone.fridge.domain.use_case.home.SetTastePreferenceUseCase
import com.stone.fridge.presentation.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTastePreferenceUseCase: GetTastePreferenceUseCase,
    private val setTastePreferenceUseCase: SetTastePreferenceUseCase,
    private val addRecipeUseCase: AddRecipeUseCase,
    private val getRecipeItemsUseCase: GetRecipeItemsUseCase,
    private val getFirstRecommendationUseCase: GetFirstRecommendationUseCase,
    private val getAnotherRecommendationUseCase: GetAnotherRecommendationUseCase,
    private val getIsFirstSelectionUseCase: GetIsFirstSelectionUseCase,
    private val setIsFirstSelectionUseCase: SetIsFirstSelectionUseCase,
    private val recipeToggleLikeUseCase: RecipeToggleLikeUseCase,
): ViewModel(){

    val uiState = MutableStateFlow<HomeUiState>(HomeUiState.Idle)

    private val _tastePref = MutableStateFlow<String?>(null)
    val tastePref = _tastePref.asStateFlow()

    private val _aiResponse = MutableStateFlow<List<String>>(emptyList())
    val aiResponse = _aiResponse.asStateFlow()

    private val _recipePagingData: MutableStateFlow<PagingData<RecipeRaw>> = MutableStateFlow(PagingData.empty<RecipeRaw>())
    val recipePagingData = _recipePagingData.asStateFlow()

    private val _event = MutableSharedFlow<UiEvent>()
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
            is HomeEvent.AddRecipe -> addRecipe(event.recipe)
            is HomeEvent.SetTastePreference -> setTastePreference(event.tastePreference)
            is HomeEvent.GetTastePreference -> getTastePreference()
        }
    }


    private fun getTastePreference() {
        viewModelScope.launch {
            getTastePreferenceUseCase().collectLatest {
                when(it){
                    is Resource.Success -> {
                        it.data?.let {
                            _tastePref.value = it.tastePreference
                        }
                        uiState.tryEmit(HomeUiState.Idle)
                    }
                    is Resource.Error -> {
                        uiState.tryEmit(HomeUiState.Error(it.message))
                        _event.emit(UiEvent.ShowSnackbar(it.message ?: "Unknown error"))
                    }
                    is Resource.Loading -> {
                        uiState.tryEmit(HomeUiState.Loading)
                    }
                }
            }
        }
    }

    private fun setTastePreference(tastePreference: String) {
        viewModelScope.launch {
            setTastePreferenceUseCase(TastePreference(tastePreference)).collectLatest {
                when(it){
                    is Resource.Success -> {
                        it.data?.let{
                            uiState.tryEmit(HomeUiState.Idle)
                            _event.emit(UiEvent.ShowSnackbar(it))
                        }
                    }
                    is Resource.Error -> {
                        uiState.tryEmit(HomeUiState.Error(it.message))
                        _event.emit(UiEvent.ShowSnackbar(it.message ?: "Unknown error"))
                    }
                    is Resource.Loading -> {
                        uiState.tryEmit(HomeUiState.Loading)
                    }
                }
            }
        }
    }

    private fun addRecipe(recipe: String) {
        Log.d("recipe", recipe)
        viewModelScope.launch {
            addRecipeUseCase(recipe).collectLatest {
                when(it){
                    is Resource.Success -> {
                        it.data?.let{
                            uiState.tryEmit(HomeUiState.Saved)
                            getRecipes()
                        }
                    }
                    is Resource.Error -> {
                        uiState.tryEmit(HomeUiState.Error(it.message))
                        _event.emit(UiEvent.ShowSnackbar(it.message ?: "Unknown error"))
                    }
                    is Resource.Loading -> {
                        uiState.tryEmit(HomeUiState.Loading)
                    }
                }
            }
        }
    }

    private fun getRecipeByAI() {
        viewModelScope.launch {
            val isFirstSelection = getIsFirstSelectionUseCase()
            if (isFirstSelection) {
                getFirstRecommendationUseCase()
            } else {
                getAnotherRecommendationUseCase()
            }.collectLatest {
                when (it) {
                    is Resource.Success -> {
                        it.data?.let { response ->
                            uiState.tryEmit(HomeUiState.Idle)
                            _aiResponse.update { it + response }
                            setIsFirstSelectionUseCase(false)
                        }
                    }
                    is Resource.Error -> {
                        uiState.tryEmit(HomeUiState.Error(it.message))
                        _event.emit(UiEvent.ShowSnackbar(it.message ?: "Unknown error"))
                    }
                    is Resource.Loading -> {
                        uiState.tryEmit(HomeUiState.AILoading)
                    }
                }
            }
        }
    }

    private fun getRecipes(){
        viewModelScope.launch {
            getRecipeItemsUseCase()
                .cachedIn(viewModelScope)
                .collectLatest { pagingData ->
                    _recipePagingData.value = pagingData
                }
        }
    }

    private fun toggleLike(id: Long, liked: Boolean){
        viewModelScope.launch {
            recipeToggleLikeUseCase(id, liked).collectLatest {
                when(it){
                    is Resource.Success -> {
                        it.data?.let { result ->
                            _recipePagingData.update { pagingData ->
                                pagingData.map {
                                    if(it.id == id){
                                        it.copy(liked = result)
                                    }else{
                                        it
                                    }
                                }
                            }
                        }
                        uiState.tryEmit(HomeUiState.Idle)
                    }
                    is Resource.Error -> {
                        uiState.tryEmit(HomeUiState.Error(it.message))
                        _event.emit(UiEvent.ShowSnackbar(it.message ?: "Unknown error"))
                    }
                    is Resource.Loading -> {
                        uiState.tryEmit(HomeUiState.Loading)
                    }
                }
            }
        }
    }
}

@Stable
interface HomeUiState{
    data object Idle: HomeUiState
    data object Loading: HomeUiState
    data object AILoading: HomeUiState
    data object Saved: HomeUiState
    data class Error(val message: String?): HomeUiState
}