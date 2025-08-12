package com.stone.fridge.feature.post.crud

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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stone.fridge.core.designsystem.Dimens
import com.stone.fridge.core.designsystem.R
import com.stone.fridge.core.designsystem.theme.CustomTheme
import com.stone.fridge.core.model.ModifyPost
import com.stone.fridge.core.model.NewPost
import com.stone.fridge.core.model.Post
import com.stone.fridge.core.navigation.currentComposeNavigator
import com.stone.fridge.core.ui.GoPreviewTheme
import java.io.File


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostCRUDScreen(
    viewModel: PostCRUDViewModel = hiltViewModel(),
    onShowSnackbar: suspend (String, String?) -> Unit,
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val post by viewModel.post.collectAsStateWithLifecycle()
    PostCRUDScreenContent(
        uiState = uiState,
        post = post,
        addNewPost = viewModel::addNewPost,
        modifyPost = viewModel::modifyPost,
        deletePostImage = viewModel::deletePostImage,
        onShowSnackbar = onShowSnackbar,
    )
}

@Composable
internal fun PostCRUDScreenContent(
    uiState: PostCRUDUiState,
    post: Post?,
    addNewPost: (NewPost, List<File>?) -> Unit,
    modifyPost: (Long, ModifyPost, List<File>) -> Unit,
    deletePostImage: (Long, Long) -> Unit,
    onShowSnackbar: suspend (String, String?) -> Unit,
){
    val composeNavigator = currentComposeNavigator
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
                            composeNavigator.navigateUp()
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
                ),
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
                    uiState = uiState,
                    modifier = Modifier.padding(innerPadding),
                    post = post,
                    deletePostImage = deletePostImage,
                    modifyPost = modifyPost,
                    onShowSnackbar = onShowSnackbar,
                )
            }else{
                NewPostForm(
                    uiState = uiState,
                    modifier = Modifier.padding(innerPadding),
                    addNewPost = addNewPost,
                    onShowSnackbar = onShowSnackbar,
                )
            }
        }
    }
}

@Preview
@Composable
fun PostCRUDScreenContentPreview() {
    GoPreviewTheme {
        PostCRUDScreenContent(
            uiState = PostCRUDUiState.Idle,
            post = null,
            addNewPost = { _, _ -> },
            modifyPost = { _, _, _ -> },
            deletePostImage = { _, _ -> },
            onShowSnackbar = { _, _ -> }
        )
    }
}