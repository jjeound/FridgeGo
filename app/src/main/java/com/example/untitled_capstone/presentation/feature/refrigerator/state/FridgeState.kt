package com.example.untitled_capstone.presentation.feature.refrigerator.state

import androidx.paging.PagingData
import com.example.untitled_capstone.domain.model.FridgeItem
import kotlinx.coroutines.flow.Flow

data class FridgeState(
    val fridgeItems: Flow<PagingData<FridgeItem>>? = null,
    val loading: Boolean = false
)
