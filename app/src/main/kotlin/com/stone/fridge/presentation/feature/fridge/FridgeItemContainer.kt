package com.stone.fridge.presentation.feature.fridge

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.stone.fridge.MainActivity
import com.stone.fridge.R
import com.stone.fridge.core.util.Dimens
import com.stone.fridge.domain.model.FridgeItem
import com.stone.fridge.ui.theme.CustomTheme
import com.stone.fridge.presentation.feature.fridge.crud.convertMillisToDate

@Composable
fun FridgeItemContainer(
    item: FridgeItem,
    toggleNotification: (Long, Boolean) -> Unit,
    onShowDialog: () -> Unit,
    navigate: () -> Unit,
    deleteItem: (Long) -> Unit,
) {
    val context = LocalContext.current
    val isNotification = remember { mutableStateOf(item.notification) }
    val expirationDate = convertMillisToDate(item.expirationDate)
    var expanded by remember { mutableStateOf(false) }
    val menuItem = listOf("수정", "삭제")
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                toggleNotification(item.id, item.notification) // 알림 토글
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
                .fillMaxWidth()
                .padding(Dimens.mediumPadding),
            horizontalArrangement = Arrangement.spacedBy(Dimens.mediumPadding)
        ){
            if (item.image != null) {
                AsyncImage(
                    model = item.image,
                    contentDescription = item.name,
                    alignment = Alignment.Center,
                    contentScale = ContentScale.Crop,
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
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = false
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
                    text = "수량/g: ${item.quantity}",
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
                        containerColor = CustomTheme.colors.onSurface,
                        shape = RoundedCornerShape(Dimens.cornerRadius),
                        border = BorderStroke(
                            width = 1.dp,
                            color = CustomTheme.colors.borderLight
                        )
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
                                        menuItem[0] -> navigate()
                                        menuItem[1] -> deleteItem(item.id)
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
                                toggleNotification(item.id, item.notification) // 알림 토글
                                isNotification.value = !isNotification.value
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
