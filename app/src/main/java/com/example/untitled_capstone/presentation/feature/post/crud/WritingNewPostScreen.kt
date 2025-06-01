package com.example.untitled_capstone.presentation.feature.post.crud

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.domain.model.NewPost
import com.example.untitled_capstone.domain.model.Post
import com.example.untitled_capstone.presentation.util.CustomSnackbar
import com.example.untitled_capstone.ui.theme.CustomTheme
import java.io.File


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WritingNewPostScreen(
    uiState: PostCRUDUiState,
    post: Post?,
    popBackStack: () -> Unit,
    deletePostImage: (Long, Long) -> Unit,
    modifyPost: (Long, NewPost, List<File>) -> Unit,
    addNewPost: (NewPost, List<File>?) -> Unit,
    clearBackStack: () -> Unit,
){
    LaunchedEffect(uiState) {
        if(uiState == PostCRUDUiState.Success){
            clearBackStack()
        }
    }
    Scaffold(
        containerColor = CustomTheme.colors.onSurface,
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.padding(horizontal = Dimens.topBarPadding),
                title = {
                    Text(
                        text = "공동구매",
                        style = CustomTheme.typography.title1,
                        color = CustomTheme.colors.textPrimary,
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.close),
                            tint = CustomTheme.colors.iconSelected,
                            contentDescription = "close",
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CustomTheme.colors.onSurface
                )
            )
        },
    ) { innerPadding ->
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth().height(1.dp),
            color = CustomTheme.colors.border,
        )
        if(uiState == PostCRUDUiState.Loading){
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator(
                    color = CustomTheme.colors.primary,
                )
            }
        }else{
            if(post!= null){
                ModifyPostForm(
                    modifier = Modifier.padding(innerPadding),
                    post = post,
                    deletePostImage = deletePostImage,
                    modifyPost = modifyPost,
                )
            }else{
                NewPostForm(
                    modifier = Modifier.padding(innerPadding),
                    addNewPost = addNewPost,
                )
            }
        }
    }
}