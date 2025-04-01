package com.example.untitled_capstone.presentation.feature.my

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.use_case.my.MyUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    private val myUseCases: MyUseCases
): ViewModel(){

    private val _state = MutableStateFlow(MyState())
    val state = _state.asStateFlow()

    init {
        isLoggedIn()
    }

    fun onEvent(event: MyEvent){
        when(event){
            is MyEvent.Logout -> logout()
            is MyEvent.GetMyProfile -> getMyProfile()
        }
    }

    private fun isLoggedIn(){
        viewModelScope.launch {
            myUseCases.getAccessToken().collect{ token->
                if(!token.isNullOrEmpty()){ // null뿐만 아니라 빈 값도 확인
                    getMyProfile()
                } else {
                    _state.update { it.copy(isLoggedIn = false) }
                }
            }

        }
    }

    private fun logout() {
        viewModelScope.launch {
            val result = myUseCases.logout()
            when(result){
                is Resource.Success -> {
                    result.data?.let{
                        _state.update { it.copy(
                            isLoggedIn = false,
                            profile = null,
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

    private fun getMyProfile(){
        viewModelScope.launch {
            val result = myUseCases.getMyProfile()
            when(result){
                is Resource.Success -> {
                    result.data?.let{
                        _state.update { it.copy(
                            isLoggedIn = true,
                            profile = result.data,
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
}