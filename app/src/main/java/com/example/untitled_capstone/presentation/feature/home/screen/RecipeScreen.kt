package com.example.untitled_capstone.presentation.feature.home.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.presentation.feature.fridge.FridgeAction
import com.example.untitled_capstone.presentation.feature.home.HomeEvent
import com.example.untitled_capstone.presentation.feature.home.state.RecipeState
import com.example.untitled_capstone.ui.theme.CustomTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeScreen(id: Long, state: RecipeState, onEvent: (HomeEvent) -> Unit, navigate: () -> Unit){
    var expanded by remember { mutableStateOf(false) }
    val menuItem = listOf("수정", "삭제")
    LaunchedEffect(Unit) {
        onEvent(HomeEvent.GetRecipeById(id))
    }
    if(state.loading){
        CircularProgressIndicator(
            modifier = Modifier.fillMaxSize(),
            color = CustomTheme.colors.primary
        )
    }else{
        Scaffold(
            containerColor = CustomTheme.colors.onSurface,
            topBar = {
                CenterAlignedTopAppBar(
                    modifier = Modifier.padding(Dimens.topBarPadding),
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                navigate()
                                onEvent(HomeEvent.InitState)
                            }
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.chevron_left),
                                tint = CustomTheme.colors.iconDefault,
                                contentDescription = "back",
                            )
                        }
                    },
                    title = {
                        Text(
                            text = state.recipe?.title ?: "",
                            style = CustomTheme.typography.title1,
                            color = CustomTheme.colors.textPrimary,
                            softWrap = true
                        )
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                expanded = !expanded
                            }
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.more),
                                tint = CustomTheme.colors.iconDefault,
                                contentDescription = "more",
                            )
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            containerColor = CustomTheme.colors.textTertiary,
                            shape = RoundedCornerShape(Dimens.cornerRadius),
                        ) {
                            menuItem.forEach { option ->
                                DropdownMenuItem(
                                    modifier = Modifier.height(30.dp).width(90.dp),
                                    text = {
                                        Text(
                                            text = option,
                                            style = CustomTheme.typography.caption1,
                                            color = CustomTheme.colors.textPrimary,
                                        )
                                    },
                                    onClick = {
                                        expanded = false
                                        when(option){
                                            menuItem[0] -> {}
                                            menuItem[1] -> {}
                                        }
                                    },
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = CustomTheme.colors.onSurface
                    )
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding).fillMaxSize()
                    .padding(horizontal = Dimens.surfaceHorizontalPadding,
                        vertical = Dimens.surfaceVerticalPadding),
                verticalArrangement = Arrangement.spacedBy(Dimens.mediumPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if(state.recipe != null){
                    val recipe = state.recipe
                    Box(
                        modifier = Modifier.size(300.dp)
                            .clip(shape = RoundedCornerShape(Dimens.cornerRadius))
                            .background(CustomTheme.colors.surface),
                        contentAlignment = Alignment.BottomEnd,
                    ){
//                if (recipe.image != null) {
//                    AsyncImage(
//                        model = recipe.image.toUri(),
//                        contentDescription = recipe.title,
//                        alignment = Alignment.Center,
//                        contentScale = ContentScale.Fit,
//                        modifier = Modifier.size(300.dp)
//                            .clip(shape = RoundedCornerShape(Dimens.cornerRadius))
//                    )
//                }
                        Row {
                            IconButton(
                                onClick = { }
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.camera),
                                    contentDescription = "like",
                                    tint = CustomTheme.colors.iconDefault,
                                )
                            }
                            IconButton(
                                onClick = {
                                    onEvent(HomeEvent.ToggleLike(recipe.id, !recipe.liked))
                                }
                            ) {
                                if(recipe.liked){
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.heart_filled),
                                        contentDescription = "like",
                                        tint = CustomTheme.colors.iconRed,
                                    )
                                }else{
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.heart),
                                        contentDescription = "like",
                                        tint = CustomTheme.colors.iconDefault,
                                    )
                                }
                            }
                        }
                    }
                    val regex = "\\[(.*?)]".toRegex() // [ ] 안의 텍스트 추출 정규식
                    val parts = regex.split(recipe.instructions) // [] 기준으로 텍스트 나누기
                    val matches = regex.findAll(recipe.instructions).map { it.groupValues[1] }.toList()
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        parts.forEachIndexed { index, text ->
                            if (index > 1) { // 첫 번째 항목은 [] 앞에 있는 내용이므로 제외
                                Text(
                                    text = matches[index - 1].trim(), // [ ] 안의 텍스트
                                    style = CustomTheme.typography.title1,
                                    color = CustomTheme.colors.textPrimary,
                                    modifier = Modifier.padding(vertical = Dimens.mediumPadding)
                                )
                            }

                            if (text.trim().startsWith('-')) {
                                val ingredients = text.split('-').map { it.trim() }.filter { it.isNotEmpty() }

                                ingredients.forEach { ingredient ->
                                    Text(
                                        text = ingredient,
                                        style = CustomTheme.typography.body2,
                                        color = CustomTheme.colors.textPrimary,
                                    )
                                }
                            } else {
                                Column(
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    val steps = splitByNumber(text)

                                    steps.forEach { step ->
                                        Text(
                                            text = step,
                                            style = CustomTheme.typography.body2,
                                            color = CustomTheme.colors.textPrimary,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun splitByNumber(text: String): List<String> {
    return text.split(Regex("(?=\\d+\\.)")) // 숫자 + 점(.)이 나오면 그 앞에서 split
        .map { it.trim() }
        .filter { it.isNotEmpty() }
}