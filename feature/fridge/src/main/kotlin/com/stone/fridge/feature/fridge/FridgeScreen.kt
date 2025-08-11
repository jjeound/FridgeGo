package com.stone.fridge.feature.fridge

import android.content.Intent
import android.provider.Settings
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.stone.fridge.core.designsystem.Dimens
import com.stone.fridge.core.designsystem.R
import com.stone.fridge.core.designsystem.theme.CustomTheme
import com.stone.fridge.core.model.Fridge
import com.stone.fridge.core.paging.FridgeFetchType
import com.stone.fridge.core.ui.PermissionDialog

@Composable
fun FridgeScreen(
    viewModel: FridgeViewModel = hiltViewModel(),
    isUnread: Boolean,
    navigateToNotification: () -> Unit,
    onShowSnackbar: suspend (String, String?) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val fridgeItems = viewModel.fridgeItemPaged.collectAsLazyPagingItems()
    val freezerItems = viewModel.freezerItemPaged.collectAsLazyPagingItems()
    val topSelector by viewModel.topSelector.collectAsStateWithLifecycle()
    val items = if (topSelector) fridgeItems else freezerItems
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        FridgeTopBar(
            topSelector = topSelector,
            updateTopSelector = viewModel::updateTopSelector,
            isUnread = isUnread,
            navigateToNotification = navigateToNotification,
        )
        FridgeScreenContent(
            uiState = uiState,
            items = items,
            getItems = viewModel::getItems,
            deleteItem = viewModel::deleteItem,
            toggleNotification = viewModel::toggleNotification,
            onShowSnackbar = onShowSnackbar
        )
    }
}

@Composable
private fun FridgeScreenContent(
    uiState: FridgeUiState,
    items: LazyPagingItems<Fridge>,
    getItems: (FridgeFetchType) -> Unit,
    deleteItem: (Long) -> Unit,
    toggleNotification: (Long, Boolean) -> Unit,
    onShowSnackbar: suspend (String, String?) -> Unit
){
    val context = LocalContext.current
    val showDialog = remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    val menuItemData = listOf("등록 순", "유통기한 순")
    var alignMenu by remember { mutableStateOf("등록 순") }
    LaunchedEffect(uiState) {
        if(uiState is FridgeUiState.Error){
            onShowSnackbar(uiState.message, null)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = Dimens.surfaceHorizontalPadding,
                vertical = Dimens.surfaceVerticalPadding
            ),
        horizontalAlignment = Alignment.End
    ) {
        Column {
            Row(
                modifier = Modifier.clickable {
                    expanded = !expanded
                },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
            ) {
                Text(
                    text = alignMenu,
                    style = CustomTheme.typography.button2,
                    color = CustomTheme.colors.textPrimary,
                )
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.dropdown),
                    contentDescription = "open dropdown menu",
                    tint = CustomTheme.colors.iconSelected,
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                containerColor = CustomTheme.colors.textTertiary,
                shape = RoundedCornerShape(Dimens.cornerRadius),
            ) {
                menuItemData.forEach { option ->
                    DropdownMenuItem(
                        modifier = Modifier.height(30.dp),
                        text = {
                            Text(
                                text = option,
                                style = CustomTheme.typography.caption1,
                                color = CustomTheme.colors.textPrimary,
                            )
                        },
                        onClick = {
                            expanded = false
                            alignMenu = option
                            if(option == menuItemData[0]){
                                getItems(FridgeFetchType.OrderByCreated)
                            } else {
                                getItems(FridgeFetchType.OrderByDate)
                            }
                        },
                    )
                }
            }
        }
        HorizontalDivider(
            thickness = 1.dp,
            color = CustomTheme.colors.border
        )
        Spacer(modifier = Modifier.height(Dimens.mediumPadding))
        FridgeBody(
            items = items,
            toggleNotification = toggleNotification,
            onShowDialog = { showDialog.value = true },
            deleteItem = deleteItem
        )
    }
    PermissionDialog(
        showDialog = showDialog.value,
        message = "알람 권한이 필요합니다.",
        onEvent = { showDialog.value = false },
        context = context,
        intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
    )
}

@Composable
private fun FridgeBody(
    items: LazyPagingItems<Fridge>,
    toggleNotification: (Long, Boolean) -> Unit,
    onShowDialog: () -> Unit,
    deleteItem: (Long) -> Unit,
){
    LaunchedEffect(key1 = items.loadState) {
        if(items.loadState.refresh is LoadState.Error) {
            Log.d("error", (items.loadState.refresh as LoadState.Error).error.message.toString())
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        if(items.loadState.refresh is LoadState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = CustomTheme.colors.primary
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(Dimens.mediumPadding)
            ) {
                items(count = items.itemCount) { index ->
                    val item = items[index]
                    if (item != null) {
                        FridgeItemContainer(
                            item = item,
                            toggleNotification = toggleNotification,
                            onShowDialog = onShowDialog,
                            deleteItem = deleteItem
                        )
                    }
                }
                item {
                    if (items.loadState.append is LoadState.Loading && items.itemCount > 10) {
                        CircularProgressIndicator(modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.CenterHorizontally))
                    }
                }
            }
        }
    }
}