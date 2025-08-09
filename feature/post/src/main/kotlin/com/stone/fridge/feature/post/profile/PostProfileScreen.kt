package com.stone.fridge.feature.post.profile

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stone.fridge.core.designsystem.Dimens
import com.stone.fridge.core.designsystem.R
import com.stone.fridge.core.designsystem.theme.CustomTheme
import com.stone.fridge.core.navigation.currentComposeNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostProfileScreen(
    viewModel: PostProfileViewModel = hiltViewModel(),
    onShowSnackbar: suspend (String, String?) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val userName by viewModel.userName.collectAsStateWithLifecycle()
    val profile by viewModel.profile.collectAsStateWithLifecycle()
    val composeNavigator = currentComposeNavigator
    Scaffold(
        containerColor = CustomTheme.colors.surface,
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.padding(Dimens.topBarPadding),
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
                            composeNavigator.navigateUp()
                        }
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
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = CustomTheme.colors.borderLight
            )
            if(uiState == PostProfileUiState.Loading){
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
                    modifier = Modifier.padding(
                        horizontal = Dimens.surfaceHorizontalPadding,
                        vertical = Dimens.surfaceVerticalPadding
                    )
                ) {
                    if (profile != null) {
                        OtherProfile(
                            uiState = uiState,
                            profile = profile!!,
                            isMe = userName == null,
                            onShowSnackbar = onShowSnackbar,
                        )
                    }
                }
            }
        }
    }
}