package com.example.untitled_capstone.presentation.feature.my.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.untitled_capstone.domain.model.Profile

@Composable
fun ProfileDetail(profile: Profile){
    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text(
            text = "이름: ${profile.nickname?: "USER"}"
        )
        Text(
            text = "이메일: ${profile.email}"
        )
    }
}