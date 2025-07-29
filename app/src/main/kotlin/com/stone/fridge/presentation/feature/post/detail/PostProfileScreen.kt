package com.stone.fridge.presentation.feature.post.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.stone.fridge.R
import com.stone.fridge.core.util.Dimens
import com.stone.fridge.domain.model.Profile
import com.stone.fridge.navigation.Screen
import com.stone.fridge.presentation.feature.my.profile.ProfileUiState
import com.stone.fridge.ui.theme.CustomTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostProfileScreen(
    uiState: ProfileUiState,
    popBackStack: () -> Unit,
    profile: Profile?,
    isMe: Boolean,
    navigate: (Screen) -> Unit,
) {
    Scaffold(
        containerColor = CustomTheme.colors.surface,
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.Companion.padding(Dimens.topBarPadding),
                title = {
                    Text(
                        text = "프로필",
                        style = CustomTheme.typography.title1,
                        color = CustomTheme.colors.textPrimary,
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = ImageVector.Companion.vectorResource(R.drawable.chevron_left),
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
    ) { innerPadding ->
        Column(
            Modifier.Companion
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            HorizontalDivider(
                modifier = Modifier.Companion.fillMaxWidth(),
                thickness = 1.dp,
                color = CustomTheme.colors.borderLight
            )
            if(uiState == ProfileUiState.Loading){
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    CircularProgressIndicator(
                        color = CustomTheme.colors.primary
                    )
                }
            }else{
                Box(
                    modifier = Modifier.Companion.padding(
                        horizontal = Dimens.surfaceHorizontalPadding,
                        vertical = Dimens.surfaceVerticalPadding
                    )
                ) {
                    if (profile != null) {
                        OtherProfile(
                            profile = profile,
                            navigate = navigate,
                            isMe = isMe,
                        )
                    }
                }
            }
        }
    }
}