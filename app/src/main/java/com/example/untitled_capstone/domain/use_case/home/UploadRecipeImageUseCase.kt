package com.example.untitled_capstone.domain.use_case.home

import android.content.Context
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.AppDispatchers
import com.example.untitled_capstone.data.Dispatcher
import com.example.untitled_capstone.domain.repository.HomeRepository
import com.example.untitled_capstone.domain.util.ImageCompressor
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class UploadRecipeImageUseCase @Inject constructor(
    private val repository: HomeRepository,
    @ApplicationContext private val context: Context,
    @Dispatcher(AppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) {
    operator fun invoke(id: Long, file: File): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        val compressedFile = ImageCompressor.compressImage(context, file)
        val requestFile = compressedFile.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("recipeImage", compressedFile.name, requestFile)
        emitAll(repository.uploadImage(id, body))
    }.catch { e ->
        emit(Resource.Error("이미지 업로드 실패: ${e.message}"))
    }.flowOn(ioDispatcher)
}