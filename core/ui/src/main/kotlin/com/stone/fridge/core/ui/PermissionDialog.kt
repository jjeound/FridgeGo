package com.stone.fridge.core.ui

import android.content.Context
import android.content.Intent
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
import com.stone.fridge.core.designsystem.Dimens
import com.stone.fridge.core.designsystem.theme.CustomTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionDialog(
    showDialog: Boolean,
    message: String,
    onEvent: () -> Unit,
    context: Context,
    intent: Intent
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onEvent,
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
                    onClick = onEvent
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
                        onEvent()
                        context.startActivity(intent)
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