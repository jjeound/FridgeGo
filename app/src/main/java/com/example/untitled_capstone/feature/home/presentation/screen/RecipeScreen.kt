package com.example.untitled_capstone.feature.home.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.feature.home.domain.model.Recipe
import com.example.untitled_capstone.ui.theme.CustomTheme
import kotlinx.serialization.Serializable

@Serializable
data class RecipeNav(
    val recipe: Recipe
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeScreen(recipe: Recipe, navController: NavHostController){
    Scaffold(
        containerColor = CustomTheme.colors.onSurface,
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.padding(Dimens.surfacePadding),
                navigationIcon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.chevron_left),
                        tint = CustomTheme.colors.iconDefault,
                        contentDescription = "back",
                        modifier = Modifier.clickable {
                            navController.popBackStack()
                        }
                    )
                },
                title = {
                    Text(
                        text = recipe.title,
                        fontFamily = CustomTheme.typography.title1.fontFamily,
                        fontWeight = CustomTheme.typography.title1.fontWeight,
                        fontSize = CustomTheme.typography.title1.fontSize,
                        color = CustomTheme.colors.textPrimary,
                        softWrap = true
                    )
                },
                actions = {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.more),
                        tint = CustomTheme.colors.iconDefault,
                        contentDescription = "more",
                        modifier = Modifier.clickable {
                            TODO("menu drawer")
                        }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CustomTheme.colors.onSurface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.width(300.dp).height(300.dp)
                    .clip(shape = RoundedCornerShape(12.dp)).padding(Dimens.surfacePadding),
                contentAlignment = Alignment.BottomEnd
            ){
                if (recipe.image != null) {
                    Image(
                        painter = painterResource(recipe.image),
                        contentDescription = recipe.title,
                        alignment = Alignment.Center,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.width(300.dp).height(300.dp)
                            .clip(shape = RoundedCornerShape(12.dp))
                    )
                }
                Row {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.camera),
                        contentDescription = "like",
                        tint = CustomTheme.colors.iconDefault,
                        modifier = Modifier.padding(10.dp).clickable {
                            TODO("image upload")
                        }
                    )
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.heart),
                        contentDescription = "like",
                        tint = CustomTheme.colors.iconDefault,
                        modifier = Modifier.padding(10.dp).clickable {
                            TODO("set like")
                        }
                    )
                }
            }
            Text(
                text = "재료",
                fontFamily = CustomTheme.typography.title1.fontFamily,
                fontWeight = CustomTheme.typography.title1.fontWeight,
                fontSize = CustomTheme.typography.title1.fontSize,
                color = CustomTheme.colors.textPrimary,
                modifier = Modifier.padding(20.dp)
            )
            for(ingredient in recipe.ingredients){
                Text(
                    text = ingredient,
                    fontFamily = CustomTheme.typography.body2.fontFamily,
                    fontWeight = CustomTheme.typography.body2.fontWeight,
                    fontSize = CustomTheme.typography.body2.fontSize,
                    color = CustomTheme.colors.textPrimary
                )
            }
            Text(
                text = "레시피",
                fontFamily = CustomTheme.typography.title1.fontFamily,
                fontWeight = CustomTheme.typography.title1.fontWeight,
                fontSize = CustomTheme.typography.title1.fontSize,
                color = CustomTheme.colors.textPrimary,
                modifier = Modifier.padding(20.dp)
            )
            for(step in recipe.steps){
                Text(
                    text = step,
                    fontFamily = CustomTheme.typography.body2.fontFamily,
                    fontWeight = CustomTheme.typography.body2.fontWeight,
                    fontSize = CustomTheme.typography.body2.fontSize,
                    color = CustomTheme.colors.textPrimary
                )
            }
            Box(modifier = Modifier.fillMaxWidth().padding(20.dp))
        }
    }
}