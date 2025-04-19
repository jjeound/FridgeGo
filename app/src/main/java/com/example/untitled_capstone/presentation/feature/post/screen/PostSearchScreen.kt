package com.example.untitled_capstone.presentation.feature.post.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.domain.model.Keyword
import com.example.untitled_capstone.domain.model.PostRaw
import com.example.untitled_capstone.navigation.Screen
import com.example.untitled_capstone.presentation.feature.post.PostEvent
import com.example.untitled_capstone.presentation.feature.post.composable.PostListContainer
import com.example.untitled_capstone.ui.theme.CustomTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostSearchScreen(
    searchPagingData: LazyPagingItems<PostRaw>,
    searchHistoryState: List<Keyword>,
    onEvent: (PostEvent) -> Unit,
) {
    var keyword by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    var showResult by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        onEvent(PostEvent.GetSearchHistory)
    }
    Scaffold(
        containerColor = CustomTheme.colors.surface,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth().padding(
                        vertical = Dimens.largePadding
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = Dimens.largePadding,
                            horizontal = Dimens.topBarPadding,
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Dimens.mediumPadding)
                ) {
                    IconButton(
                        onClick = {
                            onEvent(PostEvent.PopBackStack)
                        }
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.chevron_left),
                            tint = CustomTheme.colors.iconDefault,
                            contentDescription = "back",
                        )
                    }
                    TextField(
                        modifier = Modifier
                            .weight(1f).height(48.dp)
                            .onFocusChanged {
                                if (it.isFocused) {
                                    showResult = false
                                }
                            },
                        value = keyword,
                        onValueChange = { keyword = it },
                        placeholder = {
                            Text(
                                text = "검색",
                                style = CustomTheme.typography.caption1,
                                color = CustomTheme.colors.textSecondary
                            )
                        },
                        textStyle = CustomTheme.typography.button2,
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = CustomTheme.colors.textPrimary,
                            unfocusedTextColor = CustomTheme.colors.textPrimary,
                            focusedContainerColor = CustomTheme.colors.borderLight,
                            unfocusedContainerColor = CustomTheme.colors.borderLight,
                            cursorColor = CustomTheme.colors.textPrimary,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(Dimens.cornerRadius),
                        singleLine = true,
                        maxLines = 1,
                        keyboardActions = KeyboardActions(onDone = {
                            focusManager.clearFocus()
                            if (keyword.isNotBlank()) {
                                onEvent(PostEvent.SearchPost(keyword))
                                showResult = true
                                onEvent(PostEvent.AddSearchHistory(keyword))
                            }
                        })
                    )
                    Box(
                        modifier = Modifier.size(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            modifier = Modifier.clickable {
                                onEvent(PostEvent.PopBackStack)
                            },
                            text = "닫기",
                            style = CustomTheme.typography.button1,
                            color = CustomTheme.colors.textPrimary,
                        )
                    }
                }
            }
        },
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(
                    horizontal = Dimens.surfaceHorizontalPadding,
                    vertical = Dimens.surfaceVerticalPadding
                )
                .background(CustomTheme.colors.surface),
        ){
            if(showResult){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimens.mediumPadding),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "검색 결과",
                        style = CustomTheme.typography.button2,
                        color = CustomTheme.colors.textPrimary,
                    )
                }
                if(searchPagingData.itemCount == 0){
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = "검색 결과가 없습니다.",
                            style = CustomTheme.typography.body1,
                            color = CustomTheme.colors.textPrimary,
                        )
                    }
                }
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(Dimens.mediumPadding)
                ) {
                    items(searchPagingData.itemCount) { index ->
                        val post = searchPagingData[index]
                        if(post != null){
                            Box(
                                modifier = Modifier.clickable {
                                    onEvent(PostEvent.NavigateUp(
                                        Screen.PostDetailNav(
                                            post.id
                                        )
                                    ))
                                }
                            ){
                                PostListContainer(post, onEvent = onEvent)
                            }
                        }
                    }
                    item {
                        if (searchPagingData.loadState.append is LoadState.Loading && searchPagingData.itemCount > 10) {
                            CircularProgressIndicator(modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth(Alignment.CenterHorizontally))
                        }
                    }
                }
            }else{
                Column(
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Dimens.mediumPadding),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "최근 검색",
                            style = CustomTheme.typography.button2,
                            color = CustomTheme.colors.textPrimary,
                        )
                        Text(
                            modifier = Modifier.clickable{
                                if(searchHistoryState.isNotEmpty()){
                                    onEvent(PostEvent.DeleteAllSearchHistory)
                                }
                            },
                            text = "전체 삭제",
                            style = CustomTheme.typography.button2,
                            color = CustomTheme.colors.textSecondary,
                        )
                    }
                    Spacer(
                        modifier = Modifier.height(Dimens.mediumPadding)
                    )
                    LazyColumn (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                vertical = Dimens.mediumPadding,
                                horizontal = 6.dp
                            ),
                        verticalArrangement = Arrangement.spacedBy(Dimens.mediumPadding)
                    ) {
                        items(searchHistoryState.size) { index ->
                            val keyword = searchHistoryState[index]
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.history),
                                    contentDescription = "search",
                                    tint = CustomTheme.colors.iconDefault
                                )
                                Spacer(
                                    modifier = Modifier.width(Dimens.mediumPadding)
                                )
                                Text(
                                    modifier = Modifier.weight(1f),
                                    text = keyword.keyword,
                                    style = CustomTheme.typography.body1,
                                    color = CustomTheme.colors.textPrimary,
                                )
                                IconButton(
                                    onClick = {
                                        onEvent(PostEvent.DeleteSearchHistory(keyword.keyword))
                                    },
                                    modifier = Modifier.size(24.dp)
                                ){
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.close),
                                        contentDescription = "close",
                                        tint = CustomTheme.colors.iconDefault
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
