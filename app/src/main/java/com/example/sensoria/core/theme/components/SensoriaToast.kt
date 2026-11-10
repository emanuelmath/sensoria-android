package com.example.sensoria.core.theme.components

import androidx.compose.animation.*
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sensoria.core.theme.PrimaryGreen
import com.example.sensoria.core.theme.VeryDarkGreen
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

enum class ToastType { SUCCESS, ERROR, INFO }
data class ToastEvent(val message: String, val type: ToastType)


val LocalToastController = compositionLocalOf<(String, ToastType) -> Unit> {
    { _, _ -> }
}

@Composable
fun SensoriaToast(
    message: String,
    type: ToastType = ToastType.SUCCESS,
    isVisible: Boolean,
    onDismiss: () -> Unit
) {
    LaunchedEffect(isVisible) {
        if (isVisible) {
            delay(3000L.milliseconds)
            onDismiss()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 90.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 2 })
        ) {
            val (bgColor, borderColor, icon) = when (type) {
                ToastType.SUCCESS -> Triple(VeryDarkGreen, PrimaryGreen, Icons.Default.CheckCircle)
                ToastType.ERROR -> Triple(Color(0xFF451A03), Color(0xFFEF4444), Icons.Default.Warning)
                ToastType.INFO -> Triple(Color(0xFF1E293B), Color(0xFF38BDF8), Icons.Default.Info)
            }

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = bgColor.copy(alpha = 0.95f),
                tonalElevation = 8.dp,
                shadowElevation = 8.dp,
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .border(1.dp, borderColor.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = borderColor, modifier = Modifier.size(24.dp))
                    Text(
                        text = message,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
