package com.example.untitled_capstone.domain.use_case.my

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.repository.MyRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class UploadProfileImage @Inject constructor(
    private val myRepository: MyRepository
) {
    suspend operator fun invoke(file: File): Resource<String> {
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("profileImage", file.name, requestFile)
        return myRepository.uploadProfileImage(body)
    }
}