package com.stone.fridge.feature.my.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.stone.fridge.feature.my.navigation.MyRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onLogout: () -> Unit,
    onShowSnackbar: suspend (String, String?) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val profile by viewModel.profile.collectAsStateWithLifecycle()
    var isImageChanged by remember { mutableStateOf(false) }
    LaunchedEffect(uiState == ProfileUiState.Modified) {
        isImageChanged = true
    }
    val composeNavigator = currentComposeNavigator
    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
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
                            if(isImageChanged){
                                composeNavigator.navigateAndClearBackStack(MyRoute)
                            } else {
                                composeNavigator.navigateUp()
                            }
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
                        ProfileDetail(
                            profile = profile!!,
                            logout = viewModel::logout,
                            onLogout = onLogout,
                            uiState = uiState,
                            deleteUser = viewModel::deleteUser,
                            onShowSnackbar = onShowSnackbar,
                        )
                    }
                }
            }
        }
    }
}