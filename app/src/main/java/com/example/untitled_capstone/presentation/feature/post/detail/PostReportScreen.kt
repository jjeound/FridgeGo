package com.example.untitled_capstone.presentation.feature.post.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.presentation.feature.post.PostUiState
import com.example.untitled_capstone.presentation.util.ReportType
import com.example.untitled_capstone.ui.theme.CustomTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostReportScreen(
    uiState: PostDetailUiState,
    postId: Long,
    repostPost: (Long, String, String) -> Unit,
    popBackStack: () -> Unit,
) {
    var isExpanded by remember { mutableStateOf(false) }
    val reportType = listOf(
        "욕설",
        "사기",
        "혐오",
        "기타"
    )
    var reportTypeText by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val cautions = listOf(
        "신고 전 꼭 확인해주세요",
        "• 이 기능은 서비스 운영 정책을 위반한 게시글을 제보하는 용도로 사용됩니다.",
        "• 단순한 의견 차이나 불쾌함만으로는 신고 대상이 아닐 수 있습니다.",
        "• 아래와 같은 게시글은 신고 대상이 될 수 있습니다.",
        "• 욕설, 혐오 표현, 비방, 인신공격",
        "• 음란물 또는 선정적인 내용",
        "• 도배, 광고, 스팸 등 커뮤니티 운영을 방해하는 행위",
        "• 타인의 개인정보가 포함된 게시글",
        "• 기타 커뮤니티 이용약관에 위반되는 내용",
        "• 허위 신고를 반복할 경우, 서비스 이용이 제한될 수 있습니다.",
        "• 신고된 내용은 운영진의 판단에 따라 처리되며, 처리 결과는 별도로 안내되지 않을 수 있습니다.",
        "• 신고는 익명으로 처리되며, 신고자의 정보는 상대방에게 공개되지 않습니다."
    )
    var showDialog by remember { mutableStateOf(false) }
    LaunchedEffect(uiState) {
        if (uiState == PostDetailUiState.Success) {
            showDialog = true
        }
    }
    Scaffold(
        containerColor = if(showDialog) CustomTheme.colors.border else CustomTheme.colors.onSurface,
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.padding(horizontal = Dimens.topBarPadding),
                title = {
                    Text(
                        text = "신고하기",
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
                    containerColor = if(showDialog) CustomTheme.colors.border else CustomTheme.colors.onSurface
                )
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = Dimens.surfaceHorizontalPadding,
                    vertical = Dimens.surfaceVerticalPadding)
                .verticalScroll(
                    rememberScrollState()
                )
                .clickable{
                    focusManager.clearFocus()
                },
            verticalArrangement = Arrangement.spacedBy(Dimens.mediumPadding)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(Dimens.mediumPadding),
                verticalArrangement = Arrangement.spacedBy(Dimens.mediumPadding),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "신고 유형",
                    style = CustomTheme.typography.title2,
                    color = CustomTheme.colors.textPrimary,
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = reportTypeText,
                    onValueChange = {},
                    placeholder = {
                        Text(
                            text = "신고 유형을 선택해주세요.",
                            style = CustomTheme.typography.body2,
                            color = CustomTheme.colors.textSecondary
                        )
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = { isExpanded = true }
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.chevron_down),
                                contentDescription = "expand dropdown",
                            )
                        }
                    },
                    textStyle = CustomTheme.typography.body2,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = if(showDialog) CustomTheme.colors.border else CustomTheme.colors.onSurface,
                        focusedIndicatorColor = CustomTheme.colors.border,
                        unfocusedTextColor = CustomTheme.colors.textPrimary,
                        unfocusedContainerColor = if(showDialog) CustomTheme.colors.border else CustomTheme.colors.onSurface,
                        unfocusedIndicatorColor = CustomTheme.colors.border,
                        unfocusedTrailingIconColor = CustomTheme.colors.iconSelected,
                    ),
                    shape = RoundedCornerShape(Dimens.cornerRadius),
                    readOnly = true,
                )
                DropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false },
                    containerColor = CustomTheme.colors.onSurface,
                    border = BorderStroke(
                        width = 1.dp,
                        color = CustomTheme.colors.border
                    ),
                    shadowElevation = 0.dp,
                    tonalElevation = 0.dp,
                    shape = RoundedCornerShape(Dimens.cornerRadius),
                ) {
                    reportType.forEach { option ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = option,
                                    style = CustomTheme.typography.body2,
                                    color = CustomTheme.colors.textPrimary,
                                )},
                            onClick = {
                                isExpanded = false
                                reportTypeText = option
                            },
                        )
                    }
                }
            }
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(Dimens.mediumPadding),
                verticalArrangement = Arrangement.spacedBy(Dimens.mediumPadding),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "신고 내용",
                    style = CustomTheme.typography.title2,
                    color = CustomTheme.colors.textPrimary,
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth()
                        .height(120.dp),
                    value = content,
                    onValueChange = {content = it},
                    placeholder = {
                        Text(
                            text = "신고 내용을 작성해주세요.",
                            style = CustomTheme.typography.body2,
                            color = CustomTheme.colors.textSecondary
                        )
                    },
                    textStyle = CustomTheme.typography.body2,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CustomTheme.colors.textFieldBorder,
                        unfocusedBorderColor = CustomTheme.colors.border,
                        focusedTextColor = CustomTheme.colors.textPrimary,
                        unfocusedTextColor = CustomTheme.colors.textPrimary,
                        focusedContainerColor = if(showDialog) CustomTheme.colors.border else CustomTheme.colors.onSurface,
                        unfocusedContainerColor = if(showDialog) CustomTheme.colors.border else CustomTheme.colors.onSurface,
                        cursorColor = CustomTheme.colors.textPrimary,
                        errorCursorColor = CustomTheme.colors.error,
                    ),
                    shape = RoundedCornerShape(Dimens.cornerRadius),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(Dimens.mediumPadding),
                verticalArrangement = Arrangement.spacedBy(Dimens.mediumPadding),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "주의사항 \uD83D\uDEA8",
                    style = CustomTheme.typography.title2,
                    color = CustomTheme.colors.textPrimary,
                )
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ){
                    cautions.forEach {
                        Text(
                            text = it,
                            style = CustomTheme.typography.caption1,
                            color = CustomTheme.colors.textPrimary,
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(Dimens.cornerRadius),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CustomTheme.colors.primary,
                    disabledContainerColor = CustomTheme.colors.onSurface,
                    contentColor = CustomTheme.colors.onPrimary,
                    disabledContentColor = CustomTheme.colors.textTertiary
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = CustomTheme.colors.border
                ),
                enabled = reportTypeText.isNotBlank() && content.isNotBlank(),
                onClick = {
                    repostPost(postId, ReportType.fromKor(reportTypeText) ?: "OTHER", content)
                }
            ) {
                Text(
                    text = "신고하기",
                    style = CustomTheme.typography.button1,
                )
            }
            if(showDialog){
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    text = {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(
                                Dimens.largePadding
                            ),
                            horizontalArrangement = Arrangement.Center,
                        )  {
                            Text(
                                text = "신고가 정상적으로 접수되었습니다.",
                                style = CustomTheme.typography.title2,
                                color = CustomTheme.colors.textPrimary,
                            )
                        }
                    },
                    containerColor = CustomTheme.colors.onSurface,
                    confirmButton = {
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CustomTheme.colors.primary,
                                contentColor = CustomTheme.colors.onPrimary,
                            ),
                            onClick = {
                                showDialog = false
                                popBackStack()
                            }
                        ) {
                            Text("확인")
                        }
                    }
                )
            }
        }
    }
}