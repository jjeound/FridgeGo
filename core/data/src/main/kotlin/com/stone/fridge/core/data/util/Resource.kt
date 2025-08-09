package com.stone.fridge.core.data.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

sealed interface Resource<out T> {
    data class Success<T>(val data: T) : Resource<T>
    data class Error(val message: String) : Resource<Nothing>
    data object Loading : Resource<Nothing>
}

fun <T> Flow<T>.asResult(): Flow<Resource<T>> = map<T, Resource<T>> { Resource.Success(it) }
    .onStart { emit(Resource.Loading) }
    .catch { e ->
        val message = when (e) {
            is IOException -> "네트워크 오류가 발생했어요."
            is HttpException -> {
                try {
                    val errorJson = e.response()?.errorBody()?.string()
                    val errorObj = JSONObject(errorJson ?: "")
                    errorObj.getString("message")
                } catch (_: Exception) {
                    "알 수 없는 오류가 발생했어요."
                }
            }
            else -> e.message ?: "알 수 없는 오류"
        }
        emit(Resource.Error(message))
    }