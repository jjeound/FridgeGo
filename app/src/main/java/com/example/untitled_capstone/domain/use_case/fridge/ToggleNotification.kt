package com.example.untitled_capstone.domain.use_case.fridge

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.remote.dto.ApiResponse
import com.example.untitled_capstone.domain.model.FridgeItem
import com.example.untitled_capstone.domain.repository.FridgeRepository
import javax.inject.Inject

class ToggleNotification @Inject constructor(
    private val fridgeRepository: FridgeRepository
) {
    suspend operator fun invoke(id: Long, alarmStatus: Boolean): Resource<ApiResponse> {
        return fridgeRepository.toggleNotification(id, alarmStatus)
    }
}