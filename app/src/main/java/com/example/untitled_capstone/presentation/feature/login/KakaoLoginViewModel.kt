package com.example.untitled_capstone.presentation.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.remote.dto.KakaoAccessTokenRequest
import com.example.untitled_capstone.domain.use_case.kakao.LoginCallback
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KakaoLoginViewModel @Inject constructor(
    private val loginCallback: LoginCallback,
) : ViewModel() {

    private val _state = MutableStateFlow(KakakLoginState())
    val state = _state.asStateFlow()


    fun login(accessToken: KakaoAccessTokenRequest) {
        viewModelScope.launch {
            val result = loginCallback(accessToken)
            when(result){
                is Resource.Success -> {
                    result.data?.let{
                        _state.update { it.copy(
                            response = result.data,
                            loading = false) }
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
}