package com.stone.fridge.presentation.feature.fridge.crud

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.stone.fridge.R
import com.stone.fridge.core.util.Dimens
import com.stone.fridge.domain.model.FridgeItem
import com.stone.fridge.domain.model.NewFridge
import com.stone.fridge.navigation.Screen
import com.stone.fridge.ui.theme.CustomTheme
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFridgeItemScreen(
    fridgeItem: FridgeItem?,
    uiState: FridgeCRUDUiState,
    isFridge: Boolean,
    initSavedDate: () -> Unit,
    getSavedDate: () -> String?,
    navigate: (Screen) -> Unit,
    popBackStack: () -> Unit,
    addFridgeItem: (NewFridge, File?) -> Unit,
    modifyFridgeItem: (FridgeItem, File?) -> Unit,
){
    Scaffold(
        containerColor = CustomTheme.colors.onSurface,
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.padding(horizontal = Dimens.topBarPadding),
                title = {
                    Text(
                        text = if(isFridge) "냉장고" else "냉동고",
                        style = CustomTheme.typography.title1,
                        color = CustomTheme.colors.textPrimary,
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            initSavedDate()
                            popBackStack()
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
               fridgeItem = fridgeItem,
               uiState = uiState,
               isFridge = isFridge,
               initSavedDate = initSavedDate,
               getSavedDate = getSavedDate,
               navigate = navigate,
               popBackStack = popBackStack,
               addFridgeItem = addFridgeItem,
               modifyFridgeItem = modifyFridgeItem,
           )
        }
    }
}