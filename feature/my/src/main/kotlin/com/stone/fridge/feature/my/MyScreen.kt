package com.stone.fridge.feature.my

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stone.fridge.core.designsystem.Dimens
import com.stone.fridge.core.designsystem.R
import com.stone.fridge.core.designsystem.theme.CustomTheme
import com.stone.fridge.core.model.Profile
import com.stone.fridge.core.navigation.currentComposeNavigator
import com.stone.fridge.core.ui.GoPreviewTheme
import com.stone.fridge.feature.my.navigation.AppInfoRoute
import com.stone.fridge.feature.my.navigation.MyPostsRoute

@Composable
fun MyScreen(
    viewModel: MyViewModel = hiltViewModel(),
    onShowSnackbar: suspend (String, String?) -> Unit,
    navigateToLocation: (Boolean) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val profile by viewModel.profile.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        MyTopBar()
        if(uiState == MyUiState.Loading){
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator(
                    color = CustomTheme.colors.primary,
                )
            }
        } else if (profile != null) {
            MyScreenContent(
                uiState = uiState,
                profile = profile!!,
                onShowSnackbar = onShowSnackbar,
                navigateToLocation = navigateToLocation
            )
        }
    }
}

@Composable
private fun MyScreenContent(
    uiState: MyUiState,
    profile: Profile,
    onShowSnackbar: suspend (String, String?) -> Unit,
    navigateToLocation: (Boolean) -> Unit
){
    val composeNavigator = currentComposeNavigator
    LaunchedEffect(uiState) {
        if(uiState is MyUiState.Error){
            onShowSnackbar(uiState.message, null)
        }
    }
    Column(
        modifier = Modifier.padding(horizontal = Dimens.surfaceHorizontalPadding,
            vertical = Dimens.surfaceVerticalPadding),
        verticalArrangement = Arrangement.spacedBy(Dimens.mediumPadding),
    ) {
        MyAccountContainer(
            nickname = profile.nickname,
            image = profile.imageUrl
        )
        Card(
            shape = RoundedCornerShape(Dimens.cornerRadius),
            colors = CardDefaults.cardColors(
                containerColor = CustomTheme.colors.onSurface
            )
        ) {
            Column(
                modifier = Modifier.padding(Dimens.mediumPadding),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(Dimens.mediumPadding)
            ) {
                Text(
                    text = "나의 활동",
                    style = CustomTheme.typography.title2,
                    color = CustomTheme.colors.textPrimary,
                )
                MyContainer("좋아요한 글", R.drawable.heart){
                    composeNavigator.navigate(MyPostsRoute(true))
                }
                MyContainer("나의 게시물", R.drawable.article){
                    composeNavigator.navigate(MyPostsRoute(false))
                }
            }
        }
        Card(
            shape = RoundedCornerShape(Dimens.cornerRadius),
            colors = CardDefaults.cardColors(
                containerColor = CustomTheme.colors.onSurface
            )
        ) {
            Column(
                modifier = Modifier.padding(Dimens.mediumPadding),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(Dimens.mediumPadding)
            ) {
                Text(
                    text = "설정",
                    style = CustomTheme.typography.title2,
                    color = CustomTheme.colors.textPrimary,
                )
                MyContainer("내 동네 설정", R.drawable.location, { navigateToLocation(true) })
                MyContainer("앱 정보", R.drawable.info){
                    composeNavigator.navigate(AppInfoRoute)
                }
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MyScreenContentPreview() {
    GoPreviewTheme {
        MyScreenContent(
            uiState = MyUiState.Idle,
            profile = Profile(
                id = 0,
                nickname = "User",
                email = "das@dsaji.com",
                imageUrl = null,
                trustLevel = null,
                trustLevelImageUrl = null
            ),
            onShowSnackbar = { _, _ -> },
            navigateToLocation = {}
        )
    }
}