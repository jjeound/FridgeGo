package com.example.untitled_capstone.presentation.feature.refrigerator.screen

import android.content.Intent
import android.provider.Settings
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.presentation.feature.main.MainViewModel
import com.example.untitled_capstone.presentation.feature.refrigerator.FridgeViewModel
import com.example.untitled_capstone.presentation.feature.refrigerator.composable.FridgeItemContainer
import com.example.untitled_capstone.presentation.feature.refrigerator.composable.PermissionDialog
import com.example.untitled_capstone.ui.theme.CustomTheme


@Composable
fun RefrigeratorScreen(fridgeViewModel: FridgeViewModel, viewModel: MainViewModel) {
    var expanded by remember { mutableStateOf(false) }
    val menuItemData = listOf("최신 순", "오래된 순")
    var alignMenu by remember { mutableStateOf("최신 순") }
    val showDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val state by fridgeViewModel.state.collectAsState()
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
                    style = CustomTheme.typography.caption2,
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
                shadowElevation = 0.dp,
                tonalElevation = 0.dp,
                shape = RoundedCornerShape(Dimens.cornerRadius),
            ) {
                menuItemData.forEach { option ->
                    DropdownMenuItem(
                        modifier = Modifier.height(30.dp),
                        text = {
                            Text(
                                text = option,
                                style = CustomTheme.typography.caption2,
                                color = CustomTheme.colors.textPrimary,
                            )
                        },
                        onClick = {
                            expanded = false
                            alignMenu = option
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
        if (state.loading){
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .height(30.dp)
                        .align(Alignment.Center)
                )
            }
        }else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(Dimens.mediumPadding)
            ) {
                items(state.fridgeItems.filter { it.isFridge == viewModel.topSelector }) { item ->
                    FridgeItemContainer(item, fridgeViewModel::onAction, onShowDialog = { showDialog.value = true })
                }
            }
        }
    }
    PermissionDialog(
        showDialog = showDialog,
        message = "알람 권한이 필요합니다.",
        onDismiss = { showDialog.value = false },
        onConfirm = {
            showDialog.value = false
            val intent = Intent()
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            context.startActivity(intent)
        }
    )
}