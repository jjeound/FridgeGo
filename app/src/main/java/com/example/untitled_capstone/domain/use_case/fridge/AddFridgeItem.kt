package com.example.untitled_capstone.domain.use_case.fridge

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.model.FridgeItem
import com.example.untitled_capstone.domain.repository.FridgeRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class AddFridgeItem @Inject constructor(
    private val fridgeRepository: FridgeRepository
) {
    suspend operator fun invoke(item: FridgeItem, image: File?): Resource<String> {
        return fridgeRepository.addItem(item, image)
    }
}