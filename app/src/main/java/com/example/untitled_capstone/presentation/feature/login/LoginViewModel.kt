package com.example.untitled_capstone.presentation.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.model.Address
import com.example.untitled_capstone.domain.use_case.app_entry.SaveAppEntry
import com.example.untitled_capstone.domain.use_case.login.LoginUseCases
import com.example.untitled_capstone.presentation.feature.login.state.AddressState
import com.example.untitled_capstone.presentation.feature.login.state.LoginState
import com.example.untitled_capstone.presentation.feature.login.state.ValidateState
import com.example.untitled_capstone.presentation.util.AuthEvent
import com.example.untitled_capstone.presentation.util.AuthEventBus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCases: LoginUseCases,
    private val saveAppEntry: SaveAppEntry
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val _validateState = MutableStateFlow(ValidateState())
    val validateState = _validateState.asStateFlow()

    private val _addressState = MutableStateFlow(AddressState())
    val addressState = _addressState.asStateFlow()


    fun onEvent(event: LoginEvent){
        when(event){
            is LoginEvent.KakaoLogin -> login(event.accessToken)
            is LoginEvent.SetNickname -> setNickname(event.nickname)
            is LoginEvent.GetAddressByCoord -> getAddressByCoord(event.x, event.y)
            is LoginEvent.ModifyNickname -> modifyNickname(event.nickname)
            is LoginEvent.SetAddress -> setLocation(event.district, event.neighborhood)
        }
    }

    private fun getAddressByCoord(x: String, y: String) {
        viewModelScope.launch {
            val result = loginUseCases.getAddressByCoord(x, y)
            when(result){
                is Resource.Success -> {
                    result.data?.let{
                        _addressState.update {
                            it.copy(
                                address = Address(result.data.regionGu, result.data.regionDong),
                                which = false,
                                loading = false,
                                error = null
                            )
                        }
                    }
                }
                is Resource.Error -> {
                    _addressState.update { it.copy(loading = false, error = result.message ?: "An unexpected error occurred") }
                }
                is Resource.Loading -> {
                    _addressState.update { it.copy(loading = true) }
                }
            }
        }
    }

    private fun login(accessToken: String) {
        viewModelScope.launch {
            val result = loginUseCases.kakaoLogin(accessToken)
            when(result){
                is Resource.Success -> {
                    result.data?.let{
                        _state.update { it.copy(
                            response = result.data,
                            loading = false,
                            error = null) }
                    }
                    AuthEventBus.send(AuthEvent.Login)
                    saveAppEntry()
                }
                is Resource.Error -> {
                    _state.update { it.copy(loading = false, error = result.message ?: "An unexpected error occurred") }
                }
                is Resource.Loading -> {
                    _state.update { it.copy(loading = true) }
                }
            }
        }
    }

    private fun setNickname(nickname: String){
        viewModelScope.launch {
            val result = loginUseCases.setNickname(nickname)
            when(result){
                is Resource.Success -> {
                    result.data?.let{
                        _validateState.update {
                            it.copy(
                                validate = true,
                                loading = false,
                                error = null
                            )
                        }
                    }
                }
                is Resource.Error -> {
                    _validateState.update { it.copy(
                        validate = false,
                        loading = false, error = result.message ?: "An unexpected error occurred") }
                }
                is Resource.Loading -> {
                    _validateState.update { it.copy(
                        validate = false,
                        loading = true) }
                }
            }
        }
    }

    private fun modifyNickname(nickname: String){
        viewModelScope.launch {
            val result = loginUseCases.modifyNickname(nickname)
            when(result){
                is Resource.Success -> {
                    result.data?.let{
                        _validateState.update {
                            it.copy(
                                validate = true,
                                loading = false,
                                error = null
                            )
                        }
                    }
                }
                is Resource.Error -> {
                    _validateState.update { it.copy(
                        validate = false,
                        loading = false, error = result.message ?: "An unexpected error occurred") }
                }
                is Resource.Loading -> {
                    _validateState.update { it.copy(
                        validate = false,
                        loading = true) }
                }
            }
        }
    }

    private fun setLocation(district: String, neighborhood: String){
        viewModelScope.launch {
            val result = loginUseCases.setLocation(district, neighborhood)
            when(result){
                is Resource.Success -> {
                    result.data?.let{
                        _addressState.update {
                            it.copy(
                                address = Address(district, neighborhood),
                                which = true,
                                loading = false,
                                error = null
                            )
                        }
                        saveAppEntry()
                    }
                }
                is Resource.Error -> {
                    _validateState.update { it.copy(
                        validate = false,
                        loading = false, error = result.message ?: "An unexpected error occurred") }
                }
                is Resource.Loading -> {
                    _validateState.update { it.copy(
                        validate = false,
                        loading = true) }
                }
            }
        }
    }
}