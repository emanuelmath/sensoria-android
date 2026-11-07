package com.example.sensoria.core.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

fun Modifier.glassSurface(): Modifier {
    return this
        .clip(RoundedCornerShape(16.dp))
        .background(Color.White.copy(alpha = 0.75f))
        .border(
            width = 1.dp,
            color = Color.White.copy(alpha = 0.5f),
            shape = RoundedCornerShape(16.dp)
        )
}