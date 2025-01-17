package com.example.untitled_capstone

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.untitled_capstone.feature.chatting.Chatting
import com.example.untitled_capstone.feature.home.presentation.Home
import com.example.untitled_capstone.feature.my.My
import com.example.untitled_capstone.feature.refrigerator.Refrigerator
import com.example.untitled_capstone.feature.shopping.Shopping
import com.example.untitled_capstone.ui.theme.CustomTheme
import com.example.untitled_capstone.ui.theme.Untitled_CapstoneTheme


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
//            setKeepOnScreenCondition(
//                !viewModel.isReady.value
//            )
        }
        enableEdgeToEdge()
        setContent {
            Untitled_CapstoneTheme {
                val items = listOf(
                    BottomNavItem(
                        title = "홈",
                        route = Home,
                        icon = ImageVector.vectorResource(id = R.drawable.home)
                    ),
                    BottomNavItem(
                        title = "공동구매",
                        route = Shopping,
                        icon = ImageVector.vectorResource(id = R.drawable.shopping)
                    ),
                    BottomNavItem(
                        title = "냉장고",
                        route = Refrigerator,
                        icon = ImageVector.vectorResource(id = R.drawable.refrigerator)
                    ),
                    BottomNavItem(
                        title = "채팅",
                        route = Chatting,
                        icon = ImageVector.vectorResource(id = R.drawable.chat)
                    ),
                    BottomNavItem(
                        title = "My",
                        route = My,
                        icon = ImageVector.vectorResource(id = R.drawable.my)
                    )
                )

                val navController = rememberNavController()
                var selectedIndex by remember {
                    mutableIntStateOf(0)
                }
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = "",
                                )
                            },
                            actions = {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.search),
                                    tint = CustomTheme.colors.iconDefault,
                                    contentDescription = "search"
                                )
                                Icon(
                                    modifier = Modifier.padding(start = 24.dp, end = 20.dp),
                                    tint = CustomTheme.colors.iconDefault,
                                    imageVector = ImageVector.vectorResource(R.drawable.bell),
                                    contentDescription = "alarm"
                                )
                            }
                        )
                    },
                    bottomBar = {
                        NavigationBar(
                            modifier = Modifier.height(94.dp),
                            containerColor = CustomTheme.colors.onSurface
                        ) {
                            items.forEachIndexed() { index, item ->
                                NavigationBarItem(
                                    selected = selectedIndex == index,
                                        onClick = {
                                            selectedIndex = index
                                        navController.navigate(item.route){
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }

                                    },
                                    icon = {
                                        Icon(
                                            imageVector = item.icon,
                                            contentDescription = item.title
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = item.title,
                                            fontWeight = CustomTheme.typography.button2.fontWeight,
                                            fontSize = CustomTheme.typography.button2.fontSize,
                                            fontFamily = CustomTheme.typography.button2.fontFamily,
                                        )
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = CustomTheme.colors.iconSelected,
                                        unselectedIconColor = CustomTheme.colors.iconDefault,
                                        selectedTextColor = CustomTheme.colors.textPrimary,
                                        unselectedTextColor = CustomTheme.colors.textSecondary,
                                    )
                                )
                            }
                        }
                    }
                ){  innerPadding ->
                    Navigation(navController = navController, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

