package com.example.untitled_capstone.presentation.feature.home.composable

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.presentation.feature.home.state.AiState
import com.example.untitled_capstone.presentation.feature.home.HomeEvent
import com.example.untitled_capstone.ui.theme.CustomTheme

@Composable
fun ChatBot(aiState: AiState, onEvent: (HomeEvent) -> Unit, isExpanded: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = Dimens.surfaceHorizontalPadding
            )
            .padding(top = Dimens.surfaceVerticalPadding)
            .background(color = CustomTheme.colors.onSurface),
        verticalArrangement = Arrangement.spacedBy(Dimens.mediumPadding)
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(Dimens.largePadding)
        ) {
            items(
                count =  aiState.response.size
            ) {
                var recipe = aiState.response[it].replace("\\n", "").replace("\"", "").replace("+", "")
                val regex = "\\[(.*?)]".toRegex() // [ ] 안의 텍스트 추출 정규식
                val parts = regex.split(recipe) // [] 기준으로 텍스트 나누기
                val matches = regex.findAll(recipe).map { it.groupValues[1] }.toList() // [] 안의 내용 추출
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ai_small),
                        tint = Color.Unspecified,
                        contentDescription = "ai"
                    )
                    Spacer(
                        modifier = Modifier.size(Dimens.mediumPadding)
                    )
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = CustomTheme.colors.textTertiary,
                        ),
                        shape = RoundedCornerShape(Dimens.cornerRadius),
                        modifier = Modifier
                            .wrapContentSize()
                    ) {
                        Column(
                            modifier = Modifier.padding(Dimens.mediumPadding)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    modifier = Modifier.then(Modifier.size(24.dp)),
                                    onClick = {
                                        onEvent(HomeEvent.AddRecipe(recipe))
                                    }
                                ) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.flag),
                                        tint = CustomTheme.colors.iconDefault,
                                        contentDescription = "save"
                                    )
                                }
                                Text(
                                    text = "저장",
                                    style = CustomTheme.typography.caption2,
                                    color = CustomTheme.colors.textSecondary
                                )
                            }
                            parts.forEachIndexed { index, text ->
                                if (index > 0) { // 첫 번째 항목은 [] 앞에 있는 내용이므로 제외
                                    Text(
                                        text = matches[index - 1].trim(), // [ ] 안의 텍스트
                                        style = CustomTheme.typography.title1,
                                        color = CustomTheme.colors.textPrimary,
                                    )
                                }
                                Text(
                                    text = text.trim(),
                                    style = CustomTheme.typography.body1,
                                    color = CustomTheme.colors.textPrimary,
                                )
                            }
                        }
                    }
                }
            }
            item{
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ai_small),
                        tint = Color.Unspecified,
                        contentDescription = "ai"
                    )
                    Spacer(
                        modifier = Modifier.size(Dimens.mediumPadding)
                    )
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = CustomTheme.colors.textTertiary,
                        ),
                        shape = RoundedCornerShape(Dimens.cornerRadius),
                        modifier = Modifier.wrapContentSize()
                    ){
                        if(aiState.isLoading){
                            DotLoadingAnimation(
                                modifier = Modifier.padding(Dimens.mediumPadding)
                            )
                        }
                    }
                }
            }
        }
        Box(
            modifier = Modifier.fillMaxWidth().weight(1f)
        ) {
            ElevatedButton(
                modifier = Modifier.align(
                    alignment = if (isExpanded) Alignment.BottomEnd else Alignment.TopEnd
                ),
                onClick = {
                    onEvent(HomeEvent.GetRecipeByAi)
                },
                enabled = !aiState.isLoading,
                shape = ButtonDefaults.elevatedShape,
                elevation = ButtonDefaults.elevatedButtonElevation(),
                colors = ButtonColors(
                    containerColor = CustomTheme.colors.buttonSurface,
                    contentColor = CustomTheme.colors.textPrimary,
                    disabledContainerColor = CustomTheme.colors.textTertiary,
                    disabledContentColor = CustomTheme.colors.textPrimary,
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = CustomTheme.colors.borderLight
                )
            ) {
                Text(
                    text = "레시피 추천해줘!!",
                    style = CustomTheme.typography.button2,
                )
            }
        }
        Spacer(
            modifier = Modifier.height(Dimens.hugePadding)
        )
    }
}

@Composable
fun DotLoadingAnimation(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "dot_loading")

    val alpha1 by transition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "alpha1"
    )

    val alpha2 by transition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, delayMillis = 300, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "alpha2"
    )

    val alpha3 by transition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, delayMillis = 600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "alpha3"
    )

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Circle(alpha = alpha1)
        Circle(alpha = alpha2)
        Circle(alpha = alpha3)
    }
}

@Composable
fun Circle(alpha: Float) {
    Box(
        modifier = Modifier
            .size(12.dp)
            .graphicsLayer(alpha = alpha)
            .background(CustomTheme.colors.border, shape = CircleShape)
    )
}