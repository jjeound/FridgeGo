package com.example.untitled_capstone

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.untitled_capstone.feature.chatting.Chatting
import com.example.untitled_capstone.feature.chatting.ChattingScreen
import com.example.untitled_capstone.feature.home.presentation.Home
import com.example.untitled_capstone.feature.home.presentation.HomeScreen
import com.example.untitled_capstone.feature.my.My
import com.example.untitled_capstone.feature.my.MyScreen
import com.example.untitled_capstone.feature.refrigerator.Refrigerator
import com.example.untitled_capstone.feature.refrigerator.RefrigeratorScreen
import com.example.untitled_capstone.feature.shopping.Shopping
import com.example.untitled_capstone.feature.shopping.ShoppingScreen


@Composable
fun Navigation(navController: NavHostController, modifier: Modifier) {
     NavHost(navController = navController, startDestination = Home){
         composable<Home>{
             HomeScreen()
         }
         composable<Shopping>{
             ShoppingScreen(navController = navController)
         }
         composable<Refrigerator>{
             RefrigeratorScreen()
         }
         composable<Chatting>{
             ChattingScreen()
         }
         composable<My>{
             MyScreen()
         }
//         composable<ScreenA> {
//             Button(onClick = {
//                 navController.navigate(ScreenB(
//                     name = "John",
//                     age = 25
//                 ))
//             }){}
//         }
//         composable<ScreenB> {
//             val args = it.toRoute<ScreenB>()
//             Text(
//                 text = "${args.name}, ${args.age}"
//             )
//         }
     }

}

//@Serializable
//object ScreenA
//
//@Serializable
//data class ScreenB(
//    val name: String?,
//    val age: Int
//)