package com.example.untitled_capstone.domain.use_case.post

import android.content.Context
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.AppDispatchers
import com.example.untitled_capstone.data.Dispatcher
import com.example.untitled_capstone.domain.repository.PostRepository
import com.example.untitled_capstone.domain.util.ImageCompressor
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
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

class UploadPostImagesUseCase @Inject constructor(
    private val repository: PostRepository,
    @ApplicationContext private val context: Context,
    @Dispatcher(AppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) {
    operator fun invoke(id: Long, images: List<File>): Flow<Resource<String>> = flow {
        emit(Resource.Loading())

        val compressedImages = images.map {
            ImageCompressor.compressImage(context, it)
        }

        val requestFile = compressedImages.map { it.asRequestBody("image/*".toMediaTypeOrNull())}
        val body = requestFile.mapIndexed { index, file ->
            MultipartBody.Part.createFormData("postImage", compressedImages[index].name, file)
        }
        emitAll(repository.uploadImages(id, body))
    }.catch { e ->
        emit(Resource.Error("게시글 업로드 실패: ${e.message}"))
    }.flowOn(ioDispatcher)
}