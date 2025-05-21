package com.example.untitled_capstone.presentation.feature.fridge.crud

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.domain.model.FridgeItem
import com.example.untitled_capstone.navigation.Screen
import com.example.untitled_capstone.ui.theme.CustomTheme
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFridgeItemScreen(
    fridgeItem: FridgeItem?,
    uiState: FridgeCRUDUiState,
    initSavedDate: () -> Unit,
    getSavedDate: () -> String?,
    onNavigate: (Screen) -> Unit,
    popBackStack: () -> Unit,
    showSnackbar: (String) -> Unit,
    addFridgeItem: (FridgeItem, File?) -> Unit,
    modifyFridgeItem: (FridgeItem, File?) -> Unit,
){
    Scaffold(
        containerColor = CustomTheme.colors.onSurface,
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.padding(horizontal = Dimens.topBarPadding),
                title = {
                    Text(
                        text = "냉장고",
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
               initSavedDate = initSavedDate,
               getSavedDate = getSavedDate,
               navigateUp = onNavigate,
               popBackStack = popBackStack,
               showSnackbar = showSnackbar,
               addFridgeItem = addFridgeItem,
               modifyFridgeItem = modifyFridgeItem,
           )
        }
    }
}