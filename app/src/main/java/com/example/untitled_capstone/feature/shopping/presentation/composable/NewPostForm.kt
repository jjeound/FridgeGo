package com.example.untitled_capstone.feature.shopping.presentation.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.ui.theme.CustomTheme

@Composable
fun NewPostForm() {
    var expanded by remember { mutableStateOf(false) }
    val menuItemData = List(10) { "${it + 1}" }
    var price by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    HorizontalDivider(
        modifier = Modifier.fillMaxWidth().height(1.dp),
        color = CustomTheme.colors.textTertiary,
    )
    Column(
        modifier = Modifier.fillMaxSize().padding(Dimens.surfacePadding)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(Dimens.onSurfacePadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.people),
                contentDescription = "people",
                tint = CustomTheme.colors.iconDefault,
            )
            Text(
                text = "인원 수",
                fontWeight = CustomTheme.typography.caption2.fontWeight,
                fontFamily = CustomTheme.typography.caption2.fontFamily,
                fontSize = CustomTheme.typography.caption2.fontSize,
                color = CustomTheme.colors.textSecondary,
            )
            Spacer(
                modifier = Modifier.padding(6.dp)
            )
            Box(){
                Card(
                    modifier = Modifier.width(54.dp).height(24.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = CustomTheme.colors.onSurface,
                    ),
                    border = BorderStroke(width = 1.dp, color = CustomTheme.colors.textSecondary),
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize().clickable {
                            expanded = !expanded
                        },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            modifier = Modifier.padding(start = 16.dp),
                            text = "2",
                            fontWeight = CustomTheme.typography.caption2.fontWeight,
                            fontFamily = CustomTheme.typography.caption2.fontFamily,
                            fontSize = CustomTheme.typography.caption2.fontSize,
                            color = CustomTheme.colors.textPrimary,
                        )
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.dropdown),
                            contentDescription = "select number of people",
                            tint = CustomTheme.colors.iconSelected,
                        )
                    }
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    containerColor = CustomTheme.colors.textTertiary,
                    shadowElevation = 0.dp,
                    tonalElevation = 0.dp,
                    shape = RoundedCornerShape(12.dp),
                ) {
                    menuItemData.forEach { option ->
                        DropdownMenuItem(
                            modifier = Modifier.height(30.dp),
                            text = {
                                Text(
                                    text = option,
                                    fontWeight = CustomTheme.typography.caption2.fontWeight,
                                    fontFamily = CustomTheme.typography.caption2.fontFamily,
                                    fontSize = CustomTheme.typography.caption2.fontSize,
                                    color = CustomTheme.colors.textPrimary,
                                )},
                            onClick = { /* Do something... */ },
                        )
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = CustomTheme.colors.textSecondary
                        )
                    }
                }
            }
        }
        OutlinedTextField(
            // add validator
            value = price,
            onValueChange = {price = it},
            placeholder = {
                Text(
                    text = "가격",
                    fontFamily = CustomTheme.typography.body1.fontFamily,
                    fontWeight = CustomTheme.typography.body1.fontWeight,
                    fontSize = CustomTheme.typography.body1.fontSize,
                    color = CustomTheme.colors.textSecondary
                )
            },
            leadingIcon = {
                Text(
                    text = "₩",
                    fontFamily = CustomTheme.typography.body1.fontFamily,
                    fontWeight = CustomTheme.typography.body1.fontWeight,
                    fontSize = CustomTheme.typography.body1.fontSize,
                    color = CustomTheme.colors.textPrimary
                )
            },
            trailingIcon = {
                if(price.isNotBlank()){
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.delete),
                        contentDescription = "delete",
                        modifier = Modifier.clickable(
                            onClick = { price = "" }
                        )
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            textStyle = CustomTheme.typography.body1,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = CustomTheme.colors.textSecondary,
                unfocusedBorderColor = CustomTheme.colors.textSecondary,
                focusedTextColor = CustomTheme.colors.textPrimary,
                unfocusedTextColor = CustomTheme.colors.textPrimary,
                focusedContainerColor = CustomTheme.colors.onSurface,
                unfocusedContainerColor = CustomTheme.colors.onSurface,
                cursorColor = CustomTheme.colors.textPrimary,
                errorCursorColor = CustomTheme.colors.error,
                focusedTrailingIconColor = CustomTheme.colors.iconDefault,
                unfocusedTrailingIconColor = Color.Transparent,
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
        )
        TextField(
            value = title,
            onValueChange = {title = it},
            placeholder = {
                Text(
                    text = "제목",
                    fontFamily = CustomTheme.typography.body1.fontFamily,
                    fontWeight = CustomTheme.typography.body1.fontWeight,
                    fontSize = CustomTheme.typography.body1.fontSize,
                    color = CustomTheme.colors.textSecondary
                )
            },
            modifier = Modifier.fillMaxWidth(),
            textStyle = CustomTheme.typography.body1,
            colors = TextFieldDefaults.colors(
                focusedTextColor = CustomTheme.colors.textPrimary,
                unfocusedTextColor = CustomTheme.colors.textPrimary,
                focusedContainerColor = CustomTheme.colors.onSurface,
                unfocusedContainerColor = CustomTheme.colors.onSurface,
                cursorColor = CustomTheme.colors.textPrimary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTrailingIconColor = CustomTheme.colors.iconDefault,
                unfocusedTrailingIconColor = Color.Transparent,
            ),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
        )
        HorizontalDivider(
            thickness = 1.dp,
            color = CustomTheme.colors.textSecondary
        )
        TextField(
            value = content,
            onValueChange = {content = it},
            placeholder = {
                Text(
                    text = "내용을 입력하세요.",
                    fontFamily = CustomTheme.typography.body3.fontFamily,
                    fontWeight = CustomTheme.typography.body3.fontWeight,
                    fontSize = CustomTheme.typography.body3.fontSize,
                    color = CustomTheme.colors.textSecondary
                )
            },
            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
            textStyle = CustomTheme.typography.body3,
            colors = TextFieldDefaults.colors(
                focusedTextColor = CustomTheme.colors.textPrimary,
                unfocusedTextColor = CustomTheme.colors.textPrimary,
                focusedContainerColor = CustomTheme.colors.onSurface,
                unfocusedContainerColor = CustomTheme.colors.onSurface,
                cursorColor = CustomTheme.colors.textPrimary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTrailingIconColor = CustomTheme.colors.iconDefault,
                unfocusedTrailingIconColor = Color.Transparent,
            ),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
        )
    }
}