package com.example.untitled_capstone.presentation.feature.my

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.navigation.Graph
import com.example.untitled_capstone.navigation.Screen
import com.example.untitled_capstone.presentation.feature.my.composable.MyAccountContainer
import com.example.untitled_capstone.presentation.feature.my.composable.MyContainer

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
            }, state, onEvent)
        }else{
            MyAccountContainer({
                navController.navigate(route = Graph.LoginGraph) {
                    popUpTo(route = Graph.LoginGraph) { inclusive = true }
                }
            }, state, onEvent)
        }
        MyContainer(
            title = "나의 활동",
            content = listOf("좋아요 한 글", "나의 게시물"),
            icons = listOf(
                R.drawable.heart,
                R.drawable.article
            )
        )
        MyContainer(
            title = "설정",
            content = listOf("내 동네 설정", "앱 설정"),
            icons = listOf(
                R.drawable.location,
                R.drawable.info,
            )
        )
        MyContainer(
            title = "고객 지원",
            content = listOf("고객센터", "약관 및 정책"),
            icons = listOf(
                R.drawable.headset,
                R.drawable.setting
            )
        )
    }
}