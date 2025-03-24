package com.example.untitled_capstone.presentation.feature.fridge

import androidx.paging.PagingData
import com.example.untitled_capstone.domain.model.FridgeItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class FridgeState(
    val item : FridgeItem? = null,
    val loading: Boolean = false,
    val error: String? = null
)