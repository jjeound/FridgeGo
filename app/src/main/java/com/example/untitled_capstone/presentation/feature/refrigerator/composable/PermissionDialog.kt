package com.example.untitled_capstone.presentation.feature.refrigerator.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.ui.theme.CustomTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionDialog(
    showDialog: MutableState<Boolean>,
    message: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    if (showDialog.value) {
        BasicAlertDialog(
            onDismissRequest = { showDialog.value = false }
        ) {
            Surface(
                modifier = Modifier.wrapContentWidth().wrapContentHeight(),
                shape = RoundedCornerShape(Dimens.cornerRadius),
                color = CustomTheme.colors.surface,
                tonalElevation = AlertDialogDefaults.TonalElevation
            ) {
                Column(
                    modifier = Modifier.padding(Dimens.largePadding),
                ) {
                    Text(
                        modifier = Modifier.padding(Dimens.largePadding),
                        text = message,
                        style = CustomTheme.typography.headline3,
                        color = CustomTheme.colors.textPrimary,
                    )
                    HorizontalDivider()
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text(
                                text = "취소",
                                style = CustomTheme.typography.button1,
                                color = CustomTheme.colors.textSecondary,
                            )
                        }
                        TextButton(onClick = onConfirm) {
                            Text(
                                text = "설정",
                                style = CustomTheme.typography.button1,
                                color = CustomTheme.colors.textPrimary,
                            )
                        }
                    }
                }
            }
        }
    }
}