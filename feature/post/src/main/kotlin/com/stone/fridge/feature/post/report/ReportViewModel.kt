package com.stone.fridge.feature.post.report

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stone.fridge.core.data.post.PostRepository
import com.stone.fridge.core.data.util.Resource
import com.stone.fridge.core.data.util.asResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val postRepository: PostRepository
): ViewModel() {

    val uiState: MutableStateFlow<ReportUiState> =
        MutableStateFlow(ReportUiState.Idle)

    fun reportPost(id: Long, reportType: String, content: String) {
        viewModelScope.launch {
            postRepository.reportPost(id, reportType, content)
                .asResult()
                .collectLatest{ result ->
                    when(result){
                        is Resource.Success -> uiState.emit(ReportUiState.Success(result.data))
                        is Resource.Error ->  uiState.emit(ReportUiState.Error(result.message))
                        is Resource.Loading -> uiState.emit(ReportUiState.Loading)
                    }
                }
        }
    }

    fun reportUser(id: Long, reportType: String, content: String) {
        viewModelScope.launch {
            postRepository.reportPost(id, reportType, content)
                .asResult()
                .collectLatest{ result ->
                    when(result){
                        is Resource.Success -> uiState.emit(ReportUiState.Success(result.data))
                        is Resource.Error ->  uiState.emit(ReportUiState.Error(result.message))
                        is Resource.Loading -> uiState.emit(ReportUiState.Loading)
                    }
                }
        }
    }
}

@Stable
sealed interface ReportUiState {
    data object Idle: ReportUiState
    data class Success(val message: String): ReportUiState
    data object Loading: ReportUiState
    data class Error(val message: String): ReportUiState
}

enum class ReportType(val value: String) {
    INAPPROPRIATE_LANGUAGE("욕설"),
    SCAM("사기"),
    HARASSMENT("혐오"),
    OTHER("기타");

    companion object {
        fun fromKor(value: String): String? {
            return ReportType.entries.find { it.value == value }?.name
        }
    }
}