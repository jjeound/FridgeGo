package com.stone.fridge.feature.my.profile

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stone.fridge.core.data.my.MyRepository
import com.stone.fridge.core.data.util.Resource
import com.stone.fridge.core.data.util.asResult
import com.stone.fridge.core.model.Profile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val myRepository: MyRepository,
): ViewModel() {

    internal val uiState: MutableStateFlow<ProfileUiState> =
        MutableStateFlow(ProfileUiState.Loading)

    private val _profile = MutableStateFlow<Profile?>(null)
    val profile = _profile.asStateFlow()

    init {
        getMyProfile()
    }

    fun getMyProfile(){
        viewModelScope.launch {
            myRepository.getMyProfile()
                .asResult()
                .collectLatest{ result ->
                    when(result){
                        is Resource.Success -> {
                            _profile.value = result.data
                            uiState.emit(ProfileUiState.Idle)
                        }
                        is Resource.Error -> uiState.emit(ProfileUiState.Error(result.message))
                        is Resource.Loading -> uiState.emit(ProfileUiState.Loading)
                    }
                }
        }
    }

    fun uploadProfileImage(file: File) {
        viewModelScope.launch {
            myRepository.uploadProfileImage(file)
                .asResult()
                .collectLatest{ result ->
                    when(result){
                        is Resource.Success -> {
                            uiState.emit(ProfileUiState.Modified)
                            getMyProfile()
                        }
                        is Resource.Error -> uiState.emit(ProfileUiState.Error(result.message))
                        is Resource.Loading -> uiState.emit(ProfileUiState.Loading)
                    }
                }
        }
    }

    fun logout() {
        viewModelScope.launch {
            myRepository.logout()
                .asResult()
                .collectLatest{ result ->
                    when(result){
                        is Resource.Success -> {
                            //AuthEventBus.send(AuthEvent.Logout)
                            uiState.emit(ProfileUiState.Logout)
                        }
                        is Resource.Error -> uiState.emit(ProfileUiState.Error(result.message))
                        is Resource.Loading -> uiState.emit(ProfileUiState.Loading)
                    }
                }
        }
    }

    fun modifyNickname(nickname: String){
        viewModelScope.launch {
            myRepository.modifyNickname(nickname)
                .asResult()
                .collectLatest{ result ->
                    when(result){
                        is Resource.Success -> uiState.emit(ProfileUiState.Idle)
                        is Resource.Error -> uiState.emit(ProfileUiState.Error(result.message))
                        is Resource.Loading -> uiState.emit(ProfileUiState.Loading)
                    }
                }
        }
    }

    fun deleteProfileImage() {
        viewModelScope.launch {
            myRepository.deleteProfileImage()
                .asResult()
                .collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        uiState.emit(ProfileUiState.Modified)
                        getMyProfile()
                    }
                    is Resource.Error -> uiState.emit(ProfileUiState.Error(result.message))
                    is Resource.Loading -> uiState.emit(ProfileUiState.Loading)
                }
            }
        }
    }

    fun deleteUser() {
        viewModelScope.launch {
            myRepository.deleteUser()
                .asResult()
                .collectLatest { result ->
                when (result) {
                    is Resource.Success -> uiState.emit(ProfileUiState.Logout)
                    is Resource.Error -> uiState.emit(ProfileUiState.Error(result.message))
                    is Resource.Loading -> uiState.emit(ProfileUiState.Loading)
                }
            }
        }
    }
}

@Stable
internal sealed interface ProfileUiState {
    data object Idle: ProfileUiState
    data object Loading: ProfileUiState
    data object Logout: ProfileUiState
    data object Modified: ProfileUiState
    data class Error(val message: String): ProfileUiState
}