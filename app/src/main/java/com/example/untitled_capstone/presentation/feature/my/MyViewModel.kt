package com.example.untitled_capstone.presentation.feature.my

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.use_case.my.MyUseCases
import com.example.untitled_capstone.domain.use_case.post.GetNickname
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    private val myUseCases: MyUseCases,
    private val getNickname: GetNickname
): ViewModel(){

    private val _state = MutableStateFlow(MyState())
    val state = _state.asStateFlow()

    private val _nickname = MutableStateFlow<String?>(null)
    val nickname = _nickname.asStateFlow()

    var loginState by mutableStateOf(true)
        private set

    init {
        getMyName()
    }

    fun onEvent(event: MyEvent){
        when(event){
            is MyEvent.Logout -> logout()
            is MyEvent.GetMyProfile -> getMyProfile()
            is MyEvent.GetOtherProfile -> getOtherProfile(event.nickname)
            is MyEvent.UploadProfileImage -> uploadProfileImage(event.file)
        }
    }

    private fun uploadProfileImage(file: File) {
        viewModelScope.launch {
            val result = myUseCases.uploadProfileImage(file)
            when(result){
                is Resource.Success -> {
                    result.data?.let{
                        _state.update { it.copy(
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

    private fun logout() {
        viewModelScope.launch {
            val result = myUseCases.logout()
            when(result){
                is Resource.Success -> {
                    result.data?.let{
                        loginState = false
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

    private fun getOtherProfile(nickname: String){
        viewModelScope.launch {
            val result = myUseCases.getOtherProfile(nickname)
            when(result){
                is Resource.Success -> {
                    result.data?.let{
                        _state.update { it.copy(
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

    private fun getMyName(){
        viewModelScope.launch {
            _nickname.value = getNickname()
        }
    }
}