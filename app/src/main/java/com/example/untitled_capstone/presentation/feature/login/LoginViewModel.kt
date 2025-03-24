package com.example.untitled_capstone.presentation.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.remote.dto.KakaoAccessTokenRequest
import com.example.untitled_capstone.domain.use_case.login.LoginUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCases: LoginUseCases,
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()


    fun onEvent(event: LoginEvent){
        when(event){
            is LoginEvent.KakaoLogin -> login(event.accessToken)
            is LoginEvent.SetNickname -> setNickname(event.nickname)
        }
    }

    private fun login(accessToken: KakaoAccessTokenRequest) {
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
                        _state.update { it.copy(
                            validate = true,
                            loading = false,
                            error = null) }
                    }
                }
                is Resource.Error -> {
                    _state.update { it.copy(
                        validate = false,
                        loading = false, error = result.message ?: "An unexpected error occurred") }
                }
                is Resource.Loading -> {
                    _state.update { it.copy(
                        validate = false,
                        loading = true) }
                }
            }
        }
    }
}