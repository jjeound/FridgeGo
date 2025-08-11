package com.stone.fridge.feature.home

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.stone.fridge.core.data.home.HomeRepository
import com.stone.fridge.core.data.util.Resource
import com.stone.fridge.core.data.util.asResult
import com.stone.fridge.core.model.RecipeRaw
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
): ViewModel(){

    internal val homeUIState = MutableStateFlow<HomeUiState>(HomeUiState.Idle)
    internal val aiUIState = MutableStateFlow<AIUIState>(AIUIState.Idle)

    private val _tastePref = MutableStateFlow<String?>(null)
    val tastePref = _tastePref.asStateFlow()

    private val _aiResponse = MutableStateFlow<List<String>>(emptyList())
    val aiResponse = _aiResponse.asStateFlow()

    private val _recipePagingData: MutableStateFlow<PagingData<RecipeRaw>> = MutableStateFlow(PagingData.empty())
    val recipePagingData = _recipePagingData.asStateFlow()

    init {
        getTastePreference()
        getRecipes()
    }


    fun getTastePreference() {
        viewModelScope.launch {
            homeRepository.getTastePreference()
                .asResult()
                .collectLatest { result ->
                    when(result){
                        is Resource.Loading -> homeUIState.emit(HomeUiState.Loading)
                        is Resource.Success -> {
                            _tastePref.value = result.data.tastePreference
                            homeUIState.emit(HomeUiState.Idle)
                        }
                        is Resource.Error -> {
                            homeUIState.emit(HomeUiState.Error(result.message))
                        }
                    }
                }
        }
    }

    fun setTastePreference(tastePreference: String) {
        viewModelScope.launch {
            homeRepository.setTastePreference(tastePreference)
                .asResult()
                .collectLatest { result ->
                    when(result){
                        is Resource.Loading -> homeUIState.emit(HomeUiState.Loading)
                        is Resource.Success -> {
                            homeUIState.emit(HomeUiState.Success(result.data))
                        }
                        is Resource.Error -> {
                            homeUIState.emit(HomeUiState.Error(result.message))
                        }
                    }
                }
        }
    }

    fun addRecipe(recipe: String) {
        viewModelScope.launch {
            homeRepository.addRecipe(recipe)
                .asResult()
                .collectLatest { result ->
                    when(result){
                        is Resource.Loading -> aiUIState.emit(AIUIState.Loading)
                        is Resource.Success -> {
                            aiUIState.emit(AIUIState.Success)
                            getRecipes()
                        }
                        is Resource.Error -> {
                            aiUIState.emit(AIUIState.Error(result.message))
                        }
                    }
                }
        }
    }

    fun getAIRecipe() {
        viewModelScope.launch {
            homeRepository.getAIRecipe()
                .asResult()
                .collectLatest { result ->
                    when (result) {
                        is Resource.Loading -> aiUIState.emit(AIUIState.Loading)
                        is Resource.Success -> {
                            _aiResponse.value += result.data
                            aiUIState.emit(AIUIState.Idle)
                        }
                        is Resource.Error -> {
                            aiUIState.emit(AIUIState.Error(result.message))
                        }
                    }
                }
        }
    }

    fun getRecipes(){
        viewModelScope.launch {
            homeRepository.getRecipes()
                .cachedIn(viewModelScope)
                .collectLatest { pagingData ->
                    _recipePagingData.value = pagingData
                }
        }
    }

    fun toggleLike(id: Long, liked: Boolean){
        viewModelScope.launch {
            homeRepository.toggleLike(id, liked)
                .asResult()
                .collectLatest { result ->
                    when(result){
                        is Resource.Loading -> homeUIState.emit(HomeUiState.Loading)
                        is Resource.Success -> {
                            _recipePagingData.update { pagingData ->
                                pagingData.map {
                                    if(it.id == id){
                                        it.copy(liked = result.data)
                                    }else{
                                        it
                                    }
                                }
                            }
                            homeUIState.emit(HomeUiState.Idle)
                        }
                        is Resource.Error -> {
                            homeUIState.emit(HomeUiState.Error(result.message))
                        }
                    }
                }
        }
    }
}

@Stable
internal sealed interface HomeUiState{
    data object Idle: HomeUiState
    data object Loading: HomeUiState
    data class Success(val message: String): HomeUiState
    data class Error(val message: String): HomeUiState
}

@Stable
internal sealed interface AIUIState{
    data object Idle: AIUIState
    data object Loading: AIUIState
    data object Success: AIUIState
    data class Error(val message: String): AIUIState
}