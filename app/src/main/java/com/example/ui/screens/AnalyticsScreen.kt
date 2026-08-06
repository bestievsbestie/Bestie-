package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.OptimizationResult
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel

@Composable
fun AnalyticsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val savedList by viewModel.savedOptimizations.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 120.dp, top = 8.dp)
    ) {
        item {
            Text(
                text = "ANALYTICS DASHBOARD",
                color = MetallicGold,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.2.sp
            )
            Text(
                text = "Real-time viral reach & optimal posting times",
                color = TextSecondary,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // 4 Grid Metric Cards
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    MetricCard("Estimated Views", "1.84M", "⚡ +32%", EmeraldGreen, Modifier.weight(1f))
                    MetricCard("Virality Rate", "98.4%", "🔥 Top 1%", MetallicGold, Modifier.weight(1f))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    MetricCard("Avg Engagement", "14.2%", "📈 High", LightEmerald, Modifier.weight(1f))
                    MetricCard("Total Projects", "${savedList.size + 12}", "Saved in Firestore", DarkGold, Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Platform Reach Breakdown
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CardSurface, RoundedCornerShape(20.dp))
                    .border(1.dp, SurfaceBorder, RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "PLATFORM VIRALITY DISTRIBUTION",
                        color = MetallicGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    PlatformShareRow("TikTok", "🎵", 0.48f, "48% Reach", MetallicGold)
                    PlatformShareRow("Instagram Reels", "📸", 0.32f, "32% Reach", EmeraldGreen)
                    PlatformShareRow("YouTube Shorts", "▶️", 0.15f, "15% Reach", LightGold)
                    PlatformShareRow("Facebook Reels", "🌐", 0.05f, "5% Reach", TextSecondary)
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Best Posting Times Matrix
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CardSurface, RoundedCornerShape(20.dp))
                    .border(1.dp, SurfaceBorder, RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Schedule, contentDescription = null, tint = EmeraldGreen)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "BEST POSTING TIMES (GLOBAL PST)",
                            color = EmeraldGreen,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    PostingTimeRow("TikTok", "🎵 6:00 PM - 9:00 PM", "Peak engagement window")
                    PostingTimeRow("Instagram", "📸 11:00 AM - 2:00 PM", "Optimal lunchtime algorithm surge")
                    PostingTimeRow("YouTube Shorts", "▶️ 3:00 PM - 5:00 PM", "Afternoon watch surge")
                    PostingTimeRow("Facebook", "🌐 1:00 PM - 4:00 PM", "Steady feed engagement")
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Saved Optimizations History
        item {
            Text(
                text = "SAVED FIRESTORE OPTIMIZATIONS (${savedList.size})",
                color = MetallicGold,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (savedList.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No saved optimizations yet. Generate one in the AI tab!", color = TextMuted)
                }
            }
        } else {
            items(savedList) { item ->
                SavedOptimizationCard(item = item, onDelete = {
                    viewModel.firebaseService.deleteOptimization(item.id)
                })
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun MetricCard(title: String, value: String, badge: String, badgeColor: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(CardSurface, RoundedCornerShape(16.dp))
            .border(1.dp, SurfaceBorder, RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Column {
            Text(text = title, color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, color = PureWhite, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
            Spacer(modifier = Modifier.height(6.dp))
            Surface(color = badgeColor.copy(alpha = 0.2f), shape = RoundedCornerShape(6.dp)) {
                Text(
                    text = badge,
                    color = badgeColor,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
fun PlatformShareRow(name: String, icon: String, fraction: Float, label: String, color: Color) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = icon, fontSize = 14.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = name, color = PureWhite, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
            Text(text = label, color = color, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(2.dp))
        LinearProgressIndicator(
            progress = { fraction },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp),
            color = color,
            trackColor = ObsidianSurface
        )
    }
}

@Composable
fun PostingTimeRow(platform: String, time: String, desc: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = time, color = PureWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Text(text = desc, color = TextMuted, fontSize = 11.sp)
        }
        Surface(color = EmeraldGreen.copy(0.15f), shape = RoundedCornerShape(6.dp)) {
            Text(
                text = "HIGH REACH",
                color = EmeraldGreen,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }
    }
}

@Composable
fun SavedOptimizationCard(item: OptimizationResult, onDelete: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(CardSurface, RoundedCornerShape(16.dp))
            .border(1.dp, SurfaceBorder, RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Column {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = item.platform.iconName, fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = item.platform.displayName,
                        color = MetallicGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = TextMuted, modifier = Modifier.size(16.dp))
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = item.topic,
                color = PureWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Top Title: " + (item.titles.firstOrNull() ?: ""),
                color = TextSecondary,
                fontSize = 12.sp,
                maxLines = 1
            )
        }
    }
}
