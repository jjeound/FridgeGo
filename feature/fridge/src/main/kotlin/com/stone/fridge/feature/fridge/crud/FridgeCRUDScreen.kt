package com.stone.fridge.feature.fridge.crud

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stone.fridge.core.designsystem.R
import com.stone.fridge.core.designsystem.theme.CustomTheme
import com.stone.fridge.core.designsystem.Dimens
import com.stone.fridge.core.model.Fridge
import com.stone.fridge.core.model.ModifyFridgeReq
import com.stone.fridge.core.model.NewFridge
import com.stone.fridge.core.navigation.currentComposeNavigator
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FridgeCRUDScreen(
    viewModel: FridgeCRUDViewModel = hiltViewModel(),
    onShowSnackbar: suspend (String, String?) -> Unit,
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val fridge by viewModel.fridge.collectAsStateWithLifecycle()
    val scannedDate by viewModel.scannedDate.collectAsStateWithLifecycle()
    FridgeCRUDScreenContent(
        uiState = uiState,
        fridge = fridge,
        scannedDate = scannedDate,
        addFridgeItem = viewModel::addItem,
        modifyFridgeItem = viewModel::modifyItem,
        onShowSnackbar = onShowSnackbar,
    )
}

@Composable
private fun FridgeCRUDScreenContent(
    uiState: FridgeCRUDUiState,
    fridge: Fridge?,
    scannedDate: String?,
    addFridgeItem: (NewFridge, File?) -> Unit,
    modifyFridgeItem: (Long, ModifyFridgeReq, File?) -> Unit,
    onShowSnackbar: suspend (String, String?) -> Unit
){
    val composeNavigator = currentComposeNavigator
    Scaffold(
        containerColor = CustomTheme.colors.onSurface,
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.padding(horizontal = Dimens.topBarPadding),
                title = {
                    Text(
                        text = "등록하기",
                        style = CustomTheme.typography.title1,
                        color = CustomTheme.colors.textPrimary,
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            composeNavigator.navControllerFlow.value?.currentBackStackEntry?.savedStateHandle?.remove<String?>(
                                "date"
                            )
                            composeNavigator.navigateUp()
                        }
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.close),
                            tint = CustomTheme.colors.iconSelected,
                            contentDescription = "back",
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CustomTheme.colors.onSurface
                )
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
                .padding(horizontal = Dimens.surfaceHorizontalPadding,
                    vertical = Dimens.surfaceVerticalPadding)
        ){
            NewFridgeItemForm(
                fridge = fridge,
                uiState = uiState,
                scannedDate = scannedDate,
                addFridgeItem = addFridgeItem,
                modifyFridgeItem = modifyFridgeItem,
                onShowSnackbar = onShowSnackbar,
            )
        }
    }
}