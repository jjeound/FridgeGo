package com.example.untitled_capstone.feature.refrigerator.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastCbrt
import androidx.navigation.NavHostController
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.feature.refrigerator.presentation.composable.NewFridgeItemForm
import com.example.untitled_capstone.feature.shopping.presentation.composable.NewPostForm
import com.example.untitled_capstone.ui.theme.CustomTheme
import kotlinx.serialization.Serializable

@Serializable
object AddFridgeItemNav

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFridgeItemScreen(navController: NavHostController){
    Scaffold(
        containerColor = CustomTheme.colors.onSurface,
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.padding(Dimens.surfacePadding),
                title = {
                    Text(
                        text = "냉장고",
                        fontFamily = CustomTheme.typography.title1.fontFamily,
                        fontWeight = CustomTheme.typography.title1.fontWeight,
                        fontSize = CustomTheme.typography.title1.fontSize,
                        color = CustomTheme.colors.textPrimary,
                    )
                },
                navigationIcon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.close),
                        tint = CustomTheme.colors.iconSelected,
                        contentDescription = "back",
                        modifier = Modifier.clickable {
                            navController.popBackStack()
                        }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CustomTheme.colors.onSurface
                )
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ){
           NewFridgeItemForm(navController)
        }
    }
}