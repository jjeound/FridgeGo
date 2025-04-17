package com.example.untitled_capstone.presentation.feature.fridge.composable

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.example.untitled_capstone.MainActivity
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.presentation.util.cancelExpirationAlarm
import com.example.untitled_capstone.presentation.util.scheduleExpirationAlarms
import com.example.untitled_capstone.domain.model.FridgeItem
import com.example.untitled_capstone.presentation.feature.fridge.FridgeAction
import com.example.untitled_capstone.ui.theme.CustomTheme
import androidx.core.net.toUri

@Composable
fun FridgeItemContainer(item: FridgeItem, onAction: (FridgeAction) -> Unit, onShowDialog: () -> Unit,
                        navigateToModifyItemScreen: () -> Unit) {
    val context = LocalContext.current
    val isNotification = remember { mutableStateOf(item.notification) }
    val expirationDate = convertMillisToDate(item.expirationDate)
    var expanded by remember { mutableStateOf(false) }
    val menuItem = listOf("수정", "삭제")
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                onAction(FridgeAction.ToggleNotification(item.id, item.notification)) // 알림 토글
                isNotification.value = !isNotification.value
                Log.d("Alarm", "알람 등록")
            }
        })
    Card(
        colors = CardDefaults.cardColors(
            containerColor = CustomTheme.colors.onSurface,
        ),
        shape = RoundedCornerShape(Dimens.cornerRadius),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimens.mediumPadding),
            horizontalArrangement = Arrangement.spacedBy(Dimens.mediumPadding)
        ){
            if (item.image != null) {
                AsyncImage(
                    model = item.image,
                    contentDescription = item.name,
                    alignment = Alignment.Center,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(shape = RoundedCornerShape(Dimens.cornerRadius))
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.thumbnail),
                    contentDescription = "thumbnail",
                    alignment = Alignment.Center,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(shape = RoundedCornerShape(Dimens.mediumPadding))
                        .background(CustomTheme.colors.surface)
                )
            }
            Column(
                modifier = Modifier.weight(1f).height(80.dp),
                horizontalAlignment = Alignment.Start
            ){
                Text(
                    text = item.name,
                    style = CustomTheme.typography.body1,
                    color = CustomTheme.colors.textPrimary,
                    maxLines = 2,
                )
                Text(
                    text = expirationDate,
                    style = CustomTheme.typography.title1,
                    color = CustomTheme.colors.textPrimary,
                    maxLines = 1,
                )
                Spacer(
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "수량: ${item.quantity}",
                    style = CustomTheme.typography.body2,
                    color = CustomTheme.colors.textPrimary,
                    maxLines = 1,
                )
            }
            Column(
                modifier = Modifier.height(80.dp),
                verticalArrangement = Arrangement.SpaceBetween,
            ){
                Box{
                    IconButton(
                        modifier = Modifier.then(Modifier.size(24.dp)),
                        onClick = {expanded = true}
                    ) {
                        Icon(
                            imageVector  = ImageVector.vectorResource(R.drawable.more),
                            contentDescription = "numberOfPeople",
                            tint = CustomTheme.colors.iconDefault,
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
                                        menuItem[0] -> navigateToModifyItemScreen()
                                        menuItem[1] -> onAction(FridgeAction.DeleteItem(item.id))
                                    }
                                },
                            )
                        }
                    }
                }
                IconButton(
                    modifier = Modifier.then(Modifier.size(24.dp)),
                    onClick = {
                        when {
                            ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.POST_NOTIFICATIONS
                            ) == PackageManager.PERMISSION_GRANTED -> {
                                onAction(FridgeAction.ToggleNotification(item.id, item.notification)) // 알림 토글
                                isNotification.value = !isNotification.value
                                if(isNotification.value){
                                    scheduleExpirationAlarms(context, item.name, item.expirationDate)
                                    Log.d("Alarm", "알람 등록")
                                } else {
                                    cancelExpirationAlarm(context, item.name)
                                    Log.d("Alarm", "알람 취소")
                                }
                            }
                            shouldShowRequestPermissionRationale(
                                context as MainActivity, Manifest.permission.POST_NOTIFICATIONS) -> {
                                onShowDialog()
                            }
                            else -> {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    requestPermissionLauncher.launch(
                                        Manifest.permission.POST_NOTIFICATIONS)
                                }
                            }
                        }
                    }
                ) {
                    if(isNotification.value){
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.bell_selected),
                            contentDescription = "notification is on",
                            tint = CustomTheme.colors.iconSelected,
                        )
                    } else {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.bell_outlined),
                            contentDescription = "notification is off",
                            tint = CustomTheme.colors.iconDefault,
                        )
                    }
                }
            }
        }
    }
}
