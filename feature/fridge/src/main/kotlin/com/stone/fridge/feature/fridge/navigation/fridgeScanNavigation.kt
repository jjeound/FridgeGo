package com.stone.fridge.feature.fridge.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.stone.fridge.core.navigation.GoScreen
import com.stone.fridge.feature.fridge.crud.ScanExpirationDate
import kotlinx.serialization.Serializable

@Serializable data class FridgeScanRoute(val id: Long? = null): GoScreen

fun NavGraphBuilder.fridgeScanNavigation(
) {
    composable<FridgeScanRoute>{
        val args = it.toRoute<FridgeScanRoute>()
        ScanExpirationDate(id = args.id)
    }
}
