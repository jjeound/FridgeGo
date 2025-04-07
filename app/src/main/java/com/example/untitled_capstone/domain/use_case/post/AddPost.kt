package com.example.untitled_capstone.domain.use_case.post

import android.util.Log
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.remote.dto.ApiResponse
import com.example.untitled_capstone.domain.model.NewPost
import com.example.untitled_capstone.domain.repository.PostRepository
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject


class AddPost @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(post: NewPost, images: List<File>?): Resource<Long> {
        val gson = Gson()
        val json = gson.toJson(post.toNewPostDto())
        val jsonBody = json.toRequestBody("application/json; charset=utf-8".toMediaType())
        val requestFile = images?.map { it.asRequestBody("image/*".toMediaTypeOrNull())}
        val body = requestFile?.mapIndexed { index, file ->
            MultipartBody.Part.createFormData("postImages", images[index].name, file)
        }
        return repository.post(jsonBody, body)
    }
}