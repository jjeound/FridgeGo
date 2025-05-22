package com.example.untitled_capstone.presentation.feature.my

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.model.Profile
import com.example.untitled_capstone.domain.use_case.my.GetMyNicknameUseCase
import com.example.untitled_capstone.domain.use_case.my.GetMyProfileUseCase
import com.example.untitled_capstone.navigation.Screen
import com.example.untitled_capstone.presentation.feature.my.profile.ProfileEvent
import com.example.untitled_capstone.presentation.feature.my.profile.ProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
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