package com.stone.fridge.presentation.util

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.stone.fridge.core.util.Dimens
import com.stone.fridge.ui.theme.CustomTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionDialog(
    showDialog: MutableState<Boolean>,
    message: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            text = {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(
                        Dimens.largePadding
                    ),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                )  {
                    Text(
                        text = message,
                        style = CustomTheme.typography.title1,
                        color = CustomTheme.colors.textPrimary,
                    )
                }
            },
            containerColor = CustomTheme.colors.onSurface,
            dismissButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CustomTheme.colors.onSurface,
                        contentColor = CustomTheme.colors.textPrimary,
                    ),
                    onClick = {
                        onDismiss()
                    }
                ) {
                    Text(
                        text = "닫기",
                        style = CustomTheme.typography.button1,
                    )
                }
            },
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CustomTheme.colors.primary,
                        contentColor = CustomTheme.colors.onPrimary,
                    ),
                    onClick = {
                        onConfirm()
                    }
                ) {
                    Text(
                        text = "설정",
                        style = CustomTheme.typography.button1,
                    )
                }
            }
        )
    }
}