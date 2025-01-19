package com.example.untitled_capstone.feature.main

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.untitled_capstone.Navigation
import com.example.untitled_capstone.R
import com.example.untitled_capstone.feature.chatting.Chatting
import com.example.untitled_capstone.feature.home.presentation.screen.Home
import com.example.untitled_capstone.feature.my.My
import com.example.untitled_capstone.feature.refrigerator.Refrigerator
import com.example.untitled_capstone.feature.shopping.Shopping
import com.example.untitled_capstone.ui.theme.CustomTheme

@Composable
fun MainScreen(){
    val navController = rememberNavController()
    val viewModel = viewModel<MainViewModel>()
    Scaffold(
        containerColor = CustomTheme.colors.surface,
        topBar = {
            TopBar()
        },
        bottomBar = {
            BottomNavBar(navController = navController, viewModel = viewModel)
        },
        floatingActionButton = {
            if(viewModel.selectedIndex == 0){
                FloatingActionButton(
                    onClick = { /*TODO*/ },
                    elevation = FloatingActionButtonDefaults.elevation(0.dp),
                    containerColor = Color.Unspecified,
                    contentColor = Color.Unspecified,
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ai_big),
                        contentDescription = "ai"
                    )
                }
            }
        }
    ){  innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ){
            Navigation(navController = navController)
        }
    }
}