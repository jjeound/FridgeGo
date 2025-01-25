package com.example.untitled_capstone.feature.my.presentation.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.ui.theme.CustomTheme

@Composable
fun MyContainer(title: String, content: List<String>, icons: List<Int>){
    Card(
        shape = RoundedCornerShape(Dimens.cornerRadius),
        colors = CardDefaults.cardColors(
            containerColor = CustomTheme.colors.onSurface
        )
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp).padding(top = 10.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = title,
                fontFamily = CustomTheme.typography.title2.fontFamily,
                fontWeight = CustomTheme.typography.title2.fontWeight,
                fontSize = CustomTheme.typography.title2.fontSize,
                color = CustomTheme.colors.textPrimary,
            )
            Spacer(
                modifier = Modifier.height(10.dp)
            )
            for(i in 0 until 2){
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row {
                        Icon(
                            imageVector = ImageVector.vectorResource(icons[i]),
                            contentDescription = "icon",
                            tint = CustomTheme.colors.iconSelected
                        )
                        Spacer(
                            modifier = Modifier.padding(6.dp)
                        )
                        Text(
                            text = content[i],
                            fontFamily = CustomTheme.typography.body1.fontFamily,
                            fontWeight = CustomTheme.typography.body1.fontWeight,
                            fontSize = CustomTheme.typography.body1.fontSize,
                            color = CustomTheme.colors.textPrimary,
                        )
                    }
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.chevron_right),
                        contentDescription = "navigate",
                        tint = CustomTheme.colors.iconDefault
                    )
                }
                Spacer(
                    modifier = Modifier.height(10.dp)
                )
            }
        }
    }
}