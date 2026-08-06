package com.example.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MetallicGold

/**
 * Transparent watermark "M Ejaz" inside every app screen only (never exported into user content).
 */
@Composable
fun WatermarkOverlay(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .alpha(0.06f),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "M Ejaz",
            fontSize = 54.sp,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = FontFamily.Serif,
            color = MetallicGold,
            letterSpacing = 8.sp,
            modifier = Modifier.rotate(-25f)
        )
    }
}
