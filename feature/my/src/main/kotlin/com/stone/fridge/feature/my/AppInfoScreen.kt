package com.stone.fridge.feature.my

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.core.net.toUri
import com.stone.fridge.core.designsystem.Dimens
import com.stone.fridge.core.designsystem.R
import com.stone.fridge.core.designsystem.theme.CustomTheme
import com.stone.fridge.core.navigation.currentComposeNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppInfoScreen() {
    val composeNavigator = currentComposeNavigator
    val context = LocalContext.current
    val link = "https://www.freeprivacypolicy.com/live/9f4839cc-d44d-4285-9297-f00254ea4eac"
    Scaffold(
        containerColor = CustomTheme.colors.surface,
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.padding(Dimens.topBarPadding),
                title = {
                    Text(
                        text = "앱 정보",
                        style = CustomTheme.typography.title1,
                        color = CustomTheme.colors.textPrimary,
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { composeNavigator.navigateUp() }
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.chevron_left),
                            tint = CustomTheme.colors.iconSelected,
                            contentDescription = "back",
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CustomTheme.colors.surface
                )
            )
        }
    ){ innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(vertical = Dimens.surfaceVerticalPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens.largePadding)
        ) {
            Text(
                modifier = Modifier.clickable{
                    val intent = Intent(Intent.ACTION_VIEW, link.toUri())
                    context.startActivity(intent)
                },
                text = "개인정보 처리 방침 및 이용약관 보기",
                style = CustomTheme.typography.button1,
                color = CustomTheme.colors.textPrimary,
            )
            Text(
                text = "Contact Us : wjdwlsdl32167@gmail.com",
                style = CustomTheme.typography.body1,
                color = CustomTheme.colors.textPrimary,
            )
        }
    }
}