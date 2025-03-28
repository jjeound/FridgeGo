package com.example.untitled_capstone.presentation.feature.my

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.navigation.Graph
import com.example.untitled_capstone.navigation.Screen
import com.example.untitled_capstone.presentation.feature.my.composable.MyAccountContainer
import com.example.untitled_capstone.presentation.feature.my.composable.MyContainer
import com.example.untitled_capstone.ui.theme.CustomTheme

@Composable
fun MyScreen(navController: NavHostController, onEvent: (MyEvent) -> Unit, state: MyState) {
    Column(
        modifier = Modifier.padding(horizontal = Dimens.surfaceHorizontalPadding,
            vertical = Dimens.surfaceVerticalPadding),
        verticalArrangement = Arrangement.spacedBy(Dimens.mediumPadding),
    ) {
        if(state.isLoggedIn){
            MyAccountContainer({
                navController.navigate(route = Screen.Profile)
            }, state)
        }else{
            MyAccountContainer({
                navController.navigate(route = Graph.LoginGraph) {
                    popUpTo(route = Graph.LoginGraph) { inclusive = true }
                }
            }, state)
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
                    text = "나의 활동",
                    style = CustomTheme.typography.title2,
                    color = CustomTheme.colors.textPrimary,
                )
                MyContainer("좋아요한 글", R.drawable.heart, {navController.navigate(Screen.MyLikedPostNav)})
                MyContainer("나의 게시물", R.drawable.article, {navController.navigate(Screen.MyPostNav)})
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
                MyContainer("내 동네 설정", R.drawable.location, {navController.navigate(Screen.LocationNav)})
                MyContainer("앱 설정", R.drawable.info, {})
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
                    text = "고객 지원",
                    style = CustomTheme.typography.title2,
                    color = CustomTheme.colors.textPrimary,
                )
                MyContainer("고객센터", R.drawable.headset, {})
                MyContainer("약관 및 정책", R.drawable.setting, {})
            }
        }
    }
}