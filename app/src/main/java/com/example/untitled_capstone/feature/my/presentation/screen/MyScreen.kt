package com.example.untitled_capstone.feature.my.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.feature.my.presentation.composable.MyAccountContainer
import com.example.untitled_capstone.feature.my.presentation.composable.MyContainer

@Composable
fun MyScreen() {
    Column(
        modifier = Modifier.padding(horizontal = Dimens.surfaceHorizontalPadding,
            vertical = Dimens.surfaceVerticalPadding),
        verticalArrangement = Arrangement.spacedBy(Dimens.mediumPadding),
    ) {
        MyAccountContainer()
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
            content = listOf("내 동네 설정", "얍 설정"),
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