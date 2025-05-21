package com.example.untitled_capstone.domain.use_case.post

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class UploadPostImagesUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(id: Long, images: List<File>): Flow<Resource<String>> {
        val requestFile = images.map { it.asRequestBody("image/*".toMediaTypeOrNull())}
        val body = requestFile.mapIndexed { index, file ->
            MultipartBody.Part.createFormData("postImage", images[index].name, file)
        }
        return repository.uploadImages(id, body)
    }
}