package com.example.untitled_capstone.domain.use_case.home

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.repository.HomeRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class UploadRecipeImage @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(id: Long, file: File): Resource<String> {
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("recipeImage", file.name, requestFile)
        return repository.uploadImage(id, body)
    }
}