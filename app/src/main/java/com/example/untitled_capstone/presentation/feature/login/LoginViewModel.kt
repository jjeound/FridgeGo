package com.example.untitled_capstone.presentation.feature.login

import android.util.Log
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.model.AccountInfo
import com.example.untitled_capstone.domain.model.Address
import com.example.untitled_capstone.domain.use_case.app_entry.SaveAppEntry
import com.example.untitled_capstone.domain.use_case.login.GetAddressByCoordUseCase
import com.example.untitled_capstone.domain.use_case.login.KakaoLoginUseCase
import com.example.untitled_capstone.domain.use_case.login.ModifyNicknameUseCase
import com.example.untitled_capstone.domain.use_case.login.SaveFCMTokenUseCase
import com.example.untitled_capstone.domain.use_case.login.SetLocationUseCase
import com.example.untitled_capstone.domain.use_case.login.SetNicknameUseCase
import com.example.untitled_capstone.presentation.util.UiEvent
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val saveAppEntry: SaveAppEntry,
    private val getAddressByCoordUseCase: GetAddressByCoordUseCase,
    private val kakaoLoginUseCase: KakaoLoginUseCase,
    private val setNicknameUseCase: SetNicknameUseCase,
    private val setLocationUseCase: SetLocationUseCase,
    private val modifyNicknameUseCase: ModifyNicknameUseCase,
    private val saveFCMTokenUseCase: SaveFCMTokenUseCase
) : ViewModel() {

    val uiState: MutableStateFlow<LoginUiState> =
        MutableStateFlow<LoginUiState>(LoginUiState.Idle)

    private val _accountInfo = MutableStateFlow<AccountInfo?>(null)
    val accountInfo = _accountInfo.asStateFlow()

    private val _address = MutableStateFlow<Address?>(null)
    val address = _address.asStateFlow()

    private val _which = MutableStateFlow<Boolean>(false)
    val which = _which.asStateFlow()

    private val _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

    fun getAddressByCoord(x: String, y: String) {
        viewModelScope.launch {
            getAddressByCoordUseCase(x, y).collectLatest{
                when(it){
                    is Resource.Success -> {
                        it.data?.let{ result ->
                            _address.value = result
                            _which.value = false
                            uiState.tryEmit(LoginUiState.Idle)
                        }
                    }
                    is Resource.Error -> {
                        uiState.tryEmit(LoginUiState.Error(it.message))
                        _event.emit(UiEvent.ShowSnackbar(it.message ?: "An unexpected error occurred"))
                    }
                    is Resource.Loading -> {
                        uiState.tryEmit(LoginUiState.Loading)
                    }
                }
            }
        }
    }

    fun login(accessToken: String) {
        viewModelScope.launch {
            kakaoLoginUseCase(accessToken).collectLatest{
                when(it){
                    is Resource.Success -> {
                        it.data?.let{ result ->
                            _accountInfo.value = result
                            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                                if (!task. isSuccessful) {
                                    Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                                    return@OnCompleteListener
                                }
                                val token = task.result
                                saveFCMToken(token)
                            })
                            uiState.tryEmit(LoginUiState.Success)
                        }
                    }
                    is Resource.Error -> {
                        uiState.emit(LoginUiState.Error(it.message))
                        _event.tryEmit(UiEvent.ShowSnackbar(it.message ?: "An unexpected error occurred"))
                    }
                    is Resource.Loading -> {
                        uiState.tryEmit(LoginUiState.Loading)
                    }
                }
            }
        }
    }

    fun saveFCMToken(token: String){
        viewModelScope.launch {
            saveFCMTokenUseCase(token)
        }
    }

    fun setNickname(nickname: String){
        viewModelScope.launch {
            setNicknameUseCase(nickname).collectLatest{
                when(it){
                    is Resource.Success -> {
                        it.data?.let{ result ->
                            saveAppEntry()
                            _event.emit(UiEvent.ShowSnackbar(it.message ?: "닉네임이 설정되었습니다."))
                            uiState.tryEmit(LoginUiState.Success)
                        }
                    }
                    is Resource.Error -> {
                        _event.emit(UiEvent.ShowSnackbar(it.message ?: "An unexpected error occurred"))
                        uiState.tryEmit(LoginUiState.Error(it.message))
                    }
                    is Resource.Loading -> {
                        uiState.tryEmit(LoginUiState.Loading)
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
                            uiState.tryEmit(LoginUiState.Success)
                        }
                    }
                    is Resource.Error -> {
                        _event.emit(UiEvent.ShowSnackbar(it.message ?: "An unexpected error occurred"))
                        uiState.tryEmit(LoginUiState.Error(it.message))
                    }
                    is Resource.Loading -> {
                        uiState.tryEmit(LoginUiState.Loading)
                    }
                }
            }
        }
    }

    fun setLocation(district: String, neighborhood: String){
        viewModelScope.launch {
            setLocationUseCase(district, neighborhood).collectLatest{
                when(it){
                    is Resource.Success -> {
                        it.data?.let{ result ->
                            _address.value = Address(district, neighborhood)
                            _which.value = true
                            _event.emit(UiEvent.ShowSnackbar(it.message ?: "위치 설정이 완료되었습니다."))
                            uiState.tryEmit(LoginUiState.Success)
                        }
                    }
                    is Resource.Error -> {
                        _event.emit(UiEvent.ShowSnackbar(it.message ?: "An unexpected error occurred"))
                        uiState.tryEmit(LoginUiState.Error(it.message))
                    }
                    is Resource.Loading -> {
                        uiState.tryEmit(LoginUiState.Loading)
                    }
                }
            }
        }
    }
}

@Stable
interface LoginUiState {
    data object Idle: LoginUiState
    data object Success: LoginUiState
    data object Loading: LoginUiState
    data class Error(val message: String?): LoginUiState
}