package com.example.untitled_capstone.domain.use_case.fridge

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.repository.FridgeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ToggleNotificationUseCase @Inject constructor(
    private val fridgeRepository: FridgeRepository
) {
    suspend operator fun invoke(id: Long, alarmStatus: Boolean): Flow<Resource<String>> {
        return fridgeRepository.toggleNotification(id, alarmStatus)
    }
}