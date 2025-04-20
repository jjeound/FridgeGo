package com.example.untitled_capstone.domain.use_case.post

import com.example.untitled_capstone.core.util.Resource
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
    suspend operator fun invoke(post: NewPost, images: List<File>?): Resource<String> {
        return repository.post(post, images)
    }
}