package com.stone.fridge.presentation.feature.login

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stone.fridge.core.util.Resource
import com.stone.fridge.domain.model.AccountInfo
import com.stone.fridge.domain.model.Address
import com.stone.fridge.domain.use_case.login.GetAddressByCoordUseCase
import com.stone.fridge.domain.use_case.login.KakaoLoginUseCase
import com.stone.fridge.domain.use_case.login.SetLocationUseCase
import com.stone.fridge.domain.use_case.login.SetNicknameUseCase
import com.stone.fridge.presentation.util.UiEvent
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
    private val getAddressByCoordUseCase: GetAddressByCoordUseCase,
    private val kakaoLoginUseCase: KakaoLoginUseCase,
    private val setNicknameUseCase: SetNicknameUseCase,
    private val setLocationUseCase: SetLocationUseCase,
) : ViewModel() {

    val uiState: MutableStateFlow<LoginUiState> =
        MutableStateFlow<LoginUiState>(LoginUiState.Idle)

    private val _accountInfo = MutableStateFlow<AccountInfo?>(null)
    val accountInfo = _accountInfo.asStateFlow()

    private val _address = MutableStateFlow<Address?>(null)
    val address = _address.asStateFlow()


    private val _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

    fun getAddressByCoord(x: String, y: String) {
        viewModelScope.launch {
            getAddressByCoordUseCase(x, y).collectLatest{
                when(it){
                    is Resource.Success -> {
                        it.data?.let{ result ->
                            _address.value = result
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

    fun setNickname(nickname: String){
        viewModelScope.launch {
            setNicknameUseCase(nickname).collectLatest{
                when(it){
                    is Resource.Success -> {
                        it.data?.let{ result ->
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

    fun setLocation(district: String, neighborhood: String){
        viewModelScope.launch {
            setLocationUseCase(district, neighborhood).collectLatest{
                when(it){
                    is Resource.Success -> {
                        it.data?.let{ result ->
                            _address.value = Address(district, neighborhood)
                            _event.emit(UiEvent.ShowSnackbar(it.message ?: "위치 설정이 완료되었습니다."))
                            uiState.tryEmit(LoginUiState.LocationSet)
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
    data object LocationSet: LoginUiState
    data object Loading: LoginUiState
    data class Error(val message: String?): LoginUiState
}