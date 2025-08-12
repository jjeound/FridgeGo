package com.stone.fridge.feature.login

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stone.fridge.core.data.login.LoginRepository
import com.stone.fridge.core.data.util.Resource
import com.stone.fridge.core.data.util.asResult
import com.stone.fridge.core.model.AccountInfo
import com.stone.fridge.core.model.AddressInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {

    internal val uiState: MutableStateFlow<LoginUiState> =
        MutableStateFlow(LoginUiState.Idle)

    private val _accountInfo = MutableStateFlow<AccountInfo?>(null)
    val accountInfo = _accountInfo.asStateFlow()

    private val _address = MutableStateFlow<AddressInfo?>(null)
    val address = _address.asStateFlow()

    fun getAddressByCoord(x: String, y: String) {
        viewModelScope.launch {
            loginRepository.getAddressByCoord(x, y)
                .asResult()
                .collectLatest{ result ->
                    when(result){
                        is Resource.Success -> {
                            _address.value = result.data
                            uiState.emit(LoginUiState.Idle)
                        }
                        is Resource.Error -> uiState.emit(LoginUiState.Error(result.message))
                        is Resource.Loading -> uiState.emit(LoginUiState.Loading)
                    }
                }
        }
    }

    fun login(accessToken: String) {
        viewModelScope.launch {
            loginRepository.kakaoLogin(accessToken)
                .asResult()
                .collectLatest{ result ->
                    when(result){
                        is Resource.Success -> {
                            _accountInfo.value = result.data
                            uiState.emit(LoginUiState.Success)
                        }
                        is Resource.Error -> uiState.emit(LoginUiState.Error(result.message))
                        is Resource.Loading -> uiState.emit(LoginUiState.Loading)
                    }
                }
        }
    }

    fun setNickname(nickname: String){
        viewModelScope.launch {
            loginRepository.setNickname(nickname)
                .asResult()
                .collectLatest{ result ->
                    when(result){
                        is Resource.Success -> uiState.emit(LoginUiState.Success)
                        is Resource.Error -> uiState.emit(LoginUiState.Error(result.message))
                        is Resource.Loading -> uiState.emit(LoginUiState.Loading)
                    }
                }
        }
    }

    fun setLocation(district: String, neighborhood: String){
        viewModelScope.launch {
            loginRepository.setLocation(district, neighborhood)
                .asResult()
                .collectLatest{ result ->
                when(result){
                    is Resource.Success -> {
                        _address.value = AddressInfo(district, neighborhood)
                        uiState.emit(LoginUiState.LocationSet)
                    }
                    is Resource.Error -> uiState.emit(LoginUiState.Error(result.message))
                    is Resource.Loading -> uiState.emit(LoginUiState.Loading)
                }
            }
        }
    }
}

@Stable
internal sealed interface LoginUiState {
    data object Idle: LoginUiState
    data object Success: LoginUiState
    data object LocationSet: LoginUiState
    data object Loading: LoginUiState
    data class Error(val message: String): LoginUiState
}