package com.example.untitled_capstone.feature.refrigerator.presentation.screen

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.feature.refrigerator.presentation.composable.FridgeItemContainer
import com.example.untitled_capstone.feature.refrigerator.presentation.state.FridgeState
import com.example.untitled_capstone.ui.theme.CustomTheme

@Composable
fun RefrigeratorScreen(state: FridgeState) {
    var expanded by remember { mutableStateOf(false) }
    val menuItemData = listOf("최신 순", "오래된 순")
    var menu by remember { mutableStateOf(menuItemData[0]) }
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = Dimens.surfacePadding),
        horizontalAlignment = Alignment.End
    ) {
        Box{
            Card(
                modifier = Modifier.width(54.dp).height(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent,
                ),
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
                        text = menu,
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
                            )
                        },
                        onClick = {
                            expanded = false
                            menu = option
                        },
                    )
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = CustomTheme.colors.border
                    )
                }
            }
        }
        HorizontalDivider(
            thickness = 1.dp,
            color = CustomTheme.colors.border
        )
        Spacer(modifier = Modifier.height(Dimens.onSurfacePadding))
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(Dimens.onSurfacePadding)
        ) {
            items(state.fridgeItems) { item ->
                FridgeItemContainer(item)
            }
        }
    }
}