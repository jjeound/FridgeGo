package com.stone.fridge.presentation.feature.my

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stone.fridge.core.util.Resource
import com.stone.fridge.domain.model.Profile
import com.stone.fridge.domain.use_case.my.GetMyProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    private val getMyProfileUseCase: GetMyProfileUseCase,
): ViewModel(){

    val uiState: MutableStateFlow<MyUiState> = MutableStateFlow<MyUiState>(MyUiState.Loading)

    private val _profile = MutableStateFlow<Profile?>(null)
    val profile = _profile.asStateFlow()

    init {
        getMyProfile()
    }

    private fun getMyProfile(){
        viewModelScope.launch {
            getMyProfileUseCase().collectLatest{
                when(it){
                    is Resource.Success -> {
                        it.data?.let{
                            _profile.value = it
                            uiState.tryEmit(MyUiState.Idle)
                        }
                    }
                    is Resource.Error -> {
                        uiState.tryEmit(MyUiState.Error(it.message))
                    }
                    is Resource.Loading -> {
                        uiState.tryEmit(MyUiState.Loading)
                    }
                }
            }
        }
    }
}

@Stable
interface MyUiState {
    data object Idle: MyUiState
    data object Loading: MyUiState
    data class Error(val message: String?): MyUiState
}