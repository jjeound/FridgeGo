package com.stone.fridge.feature.post.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.stone.fridge.core.navigation.GoScreen
import com.stone.fridge.feature.post.report.ReportScreen
import kotlinx.serialization.Serializable

@Serializable
data class ReportRoute(val id: Long, val isPost: Boolean): GoScreen

fun NavGraphBuilder.reportNavigation(
    onShowSnackbar: suspend (String, String?) -> Unit,
) {
    composable<ReportRoute>{
        val args = it.toRoute<ReportRoute>()
        ReportScreen(
            onShowSnackbar = onShowSnackbar,
            id = args.id,
            isPost = args.isPost,
        )
    }
}