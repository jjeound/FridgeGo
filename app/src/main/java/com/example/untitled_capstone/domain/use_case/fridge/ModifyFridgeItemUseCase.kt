package com.example.untitled_capstone.domain.use_case.fridge

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.model.FridgeItem
import com.example.untitled_capstone.domain.repository.FridgeRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class ModifyFridgeItemUseCase @Inject constructor(
    private val fridgeRepository: FridgeRepository
) {
    suspend operator fun invoke(item: FridgeItem, image: File?): Flow<Resource<String>> {
        if (image == null) {
            return fridgeRepository.modifyItem(item)
        }
        val requestFile = image.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("recipeImage", image.name, requestFile)
        return fridgeRepository.modifyItem(item, body)
    }
}