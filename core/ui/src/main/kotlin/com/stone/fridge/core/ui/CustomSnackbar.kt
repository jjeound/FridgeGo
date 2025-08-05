package com.stone.fridge.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.stone.fridge.core.designsystem.Dimens
import com.stone.fridge.core.designsystem.theme.CustomTheme
import com.stone.fridge.core.designsystem.R

@Composable
fun CustomSnackbar(data: SnackbarData) {
    Card(
        modifier = Modifier
            .padding(horizontal = Dimens.mediumPadding)
            .wrapContentWidth(),
        shape = RoundedCornerShape(Dimens.cornerRadius),
        colors = CardDefaults.cardColors(containerColor = CustomTheme.colors.onSurface),
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        Row(
           modifier =  Modifier.wrapContentWidth()
               .padding(horizontal = Dimens.surfaceHorizontalPadding,
                   vertical = Dimens.surfaceVerticalPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.mediumPadding)
        ) {
            Image(
                painter = painterResource(R.drawable.thumbnail),
                contentDescription = "snackbar icon",
                alignment = Alignment.Center,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(Dimens.cornerRadius)),
            )
            Text(
                text = data.visuals.message,
                color = CustomTheme.colors.textPrimary,
                style = CustomTheme.typography.body1,
            )
        }
    }
}