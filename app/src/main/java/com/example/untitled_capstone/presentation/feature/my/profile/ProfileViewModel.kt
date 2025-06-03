package com.example.untitled_capstone.presentation.feature.my.profile

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.model.Profile
import com.example.untitled_capstone.domain.use_case.login.ModifyNicknameUseCase
import com.example.untitled_capstone.domain.use_case.my.DeleteProfileImageUseCase
import com.example.untitled_capstone.domain.use_case.my.GetMyProfileUseCase
import com.example.untitled_capstone.domain.use_case.my.GetOtherProfileUseCase
import com.example.untitled_capstone.domain.use_case.my.LogoutUseCase
import com.example.untitled_capstone.domain.use_case.my.UploadProfileImageUseCase
import com.example.untitled_capstone.presentation.util.AuthEvent
import com.example.untitled_capstone.presentation.util.AuthEventBus
import com.example.untitled_capstone.presentation.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getMyProfileUseCase: GetMyProfileUseCase,
    private val getOtherProfileUseCase: GetOtherProfileUseCase,
    private val uploadProfileImageUseCase: UploadProfileImageUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val modifyNicknameUseCase: ModifyNicknameUseCase,
    private val deleteProfileImageUseCase: DeleteProfileImageUseCase
): ViewModel() {

    val uiState: MutableStateFlow<ProfileUiState> =
        MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)

    private val _profile = MutableStateFlow<Profile?>(null)
    val profile = _profile.asStateFlow()

    private val _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

    fun getMyProfile(){
        viewModelScope.launch {
            getMyProfileUseCase().collectLatest{
                when(it){
                    is Resource.Success -> {
                        it.data?.let{
                            _profile.value = it
                            uiState.tryEmit(ProfileUiState.Idle)
                        }
                    }
                    is Resource.Error -> {
                        uiState.tryEmit(ProfileUiState.Error(it.message))
                        _event.emit(UiEvent.ShowSnackbar(it.message ?: "Unknown error"))
                    }
                    is Resource.Loading -> {
                        uiState.tryEmit(ProfileUiState.Loading)
                    }
                }
            }
        }
    }

    fun getOtherProfile(nickname: String){
        viewModelScope.launch {
            getOtherProfileUseCase(nickname).collectLatest{
                when(it){
                    is Resource.Success -> {
                        it.data?.let{
                            _profile.value = it
                            uiState.tryEmit(ProfileUiState.Idle)
                        }
                    }
                    is Resource.Error -> {
                        uiState.tryEmit(ProfileUiState.Error(it.message))
                        _event.emit(UiEvent.ShowSnackbar(it.message ?: "Unknown error"))
                    }
                    is Resource.Loading -> {
                        uiState.tryEmit(ProfileUiState.Loading)
                    }
                }
            }
        }
    }

    fun uploadProfileImage(file: File) {
        viewModelScope.launch {
            uploadProfileImageUseCase(file).collectLatest{
                when(it){
                    is Resource.Success -> {
                        it.data?.let{
                            uiState.emit(ProfileUiState.ImageChanged)
                            getMyProfile()
                        }
                    }
                    is Resource.Error -> {
                        uiState.tryEmit(ProfileUiState.Error(it.message))
                        _event.emit(UiEvent.ShowSnackbar(it.message ?: "Unknown error"))
                    }
                    is Resource.Loading -> {
                        uiState.tryEmit(ProfileUiState.Loading)
                    }
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase().collectLatest{
                when(it){
                    is Resource.Success -> {
                        it.data?.let{
                            AuthEventBus.send(AuthEvent.Logout)
                            uiState.tryEmit(ProfileUiState.Logout)
                        }
                    }
                    is Resource.Error -> {
                        uiState.tryEmit(ProfileUiState.Error(it.message))
                        _event.emit(UiEvent.ShowSnackbar(it.message ?: "Unknown error"))
                    }
                    is Resource.Loading -> {
                        uiState.tryEmit(ProfileUiState.Loading)
                    }
                }
            }
        }
    }

    fun modifyNickname(nickname: String){
        viewModelScope.launch {
            modifyNicknameUseCase(nickname).collectLatest{
                when(it){
                    is Resource.Success -> {
                        it.data?.let{ result ->
                            _event.emit(UiEvent.ShowSnackbar(it.message ?: "닉네임이 변경되었습니다."))
                            uiState.tryEmit(ProfileUiState.Success)
                        }
                    }
                    is Resource.Error -> {
                        _event.emit(UiEvent.ShowSnackbar(it.message ?: "An unexpected error occurred"))
                        uiState.tryEmit(ProfileUiState.Error(it.message))
                    }
                    is Resource.Loading -> {
                        uiState.tryEmit(ProfileUiState.Loading)
                    }
                }
            }
        }
    }

    fun deleteProfileImage() {
        viewModelScope.launch {
            deleteProfileImageUseCase().collectLatest {
                when (it) {
                    is Resource.Success -> {
                        it.data?.let {
                            uiState.emit(ProfileUiState.ImageChanged)
                            getMyProfile()
                        }
                    }
                    is Resource.Error -> {
                        uiState.tryEmit(ProfileUiState.Error(it.message))
                        _event.emit(UiEvent.ShowSnackbar(it.message ?: "Unknown error"))
                    }
                    is Resource.Loading -> {
                        uiState.tryEmit(ProfileUiState.Loading)
                    }
                }
            }
        }
    }
}

@Stable
interface ProfileUiState {
    data object Idle: ProfileUiState
    data object Success: ProfileUiState
    data object Loading: ProfileUiState
    data object Logout: ProfileUiState
    data object ImageChanged: ProfileUiState
    data class Error(val message: String?): ProfileUiState
}