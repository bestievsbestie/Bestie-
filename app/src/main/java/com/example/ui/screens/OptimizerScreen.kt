package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.HookItem
import com.example.data.model.OptimizationResult
import com.example.data.model.TargetPlatform
import com.example.ui.components.GoldButton
import com.example.ui.components.SeoScoreMeter
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel

@Composable
fun OptimizerScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val selectedPlatform by viewModel.selectedPlatform.collectAsState()
    val topicInput by viewModel.topicInput.collectAsState()
    val keywordsInput by viewModel.keywordsInput.collectAsState()
    val toneInput by viewModel.toneInput.collectAsState()
    val isGenerating by viewModel.isGenerating.collectAsState()
    val currentResult by viewModel.currentResult.collectAsState()

    var activeTabInResult by remember { mutableStateOf(0) } // 0: Titles, 1: Hooks, 2: Hashtags, 3: Description, 4: Thumbnails & SEO

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 120.dp, top = 8.dp)
    ) {
        // Hero Banner Header
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .border(
                        width = 1.dp,
                        brush = Brush.horizontalGradient(
                            colors = listOf(MetallicGold.copy(alpha = 0.6f), EmeraldGreen.copy(alpha = 0.6f))
                        ),
                        shape = RoundedCornerShape(24.dp)
                    )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_hero_banner),
                    contentDescription = "Booster Ejaz Hero Banner",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, VelvetBlack.copy(alpha = 0.85f))
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = MetallicGold,
                            shape = CircleShape,
                            modifier = Modifier.size(8.dp)
                        ) {}
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "AI VIRAL ENGINE v3.5",
                            color = MetallicGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.2.sp
                        )
                    }
                    Text(
                        text = "Optimize Videos in Seconds",
                        color = PureWhite,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Target Platform Selector
        item {
            Text(
                text = "1. SELECT TARGET PLATFORM",
                color = TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TargetPlatform.values().forEach { platform ->
                    val isSelected = selectedPlatform == platform
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (isSelected) MetallicGold else CardSurface)
                            .border(
                                width = 1.dp,
                                color = if (isSelected) LightGold else SurfaceBorder,
                                shape = RoundedCornerShape(14.dp)
                            )
                            .clickable { viewModel.setPlatform(platform) }
                            .testTag("platform_${platform.name.lowercase()}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = platform.iconName, fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = platform.displayName.split(" ")[0],
                                color = if (isSelected) VelvetBlack else PureWhite,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Input Form
        item {
            Text(
                text = "2. VIDEO CONCEPT & KEYWORDS",
                color = TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = topicInput,
                onValueChange = { viewModel.setTopic(it) },
                placeholder = { Text("e.g. 5 secrets to double your views on TikTok", color = TextMuted) },
                label = { Text("Video Topic / Title Concept", color = MetallicGold) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_topic"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MetallicGold,
                    unfocusedBorderColor = SurfaceBorder,
                    focusedContainerColor = CardSurface,
                    unfocusedContainerColor = CardSurface,
                    focusedTextColor = PureWhite,
                    unfocusedTextColor = PureWhite
                ),
                shape = RoundedCornerShape(16.dp),
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = keywordsInput,
                onValueChange = { viewModel.setKeywords(it) },
                placeholder = { Text("e.g. viral hacks, growth 2026, creator tips", color = TextMuted) },
                label = { Text("Target Keywords (Optional)", color = EmeraldGreen) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_keywords"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = EmeraldGreen,
                    unfocusedBorderColor = SurfaceBorder,
                    focusedContainerColor = CardSurface,
                    unfocusedContainerColor = CardSurface,
                    focusedTextColor = PureWhite,
                    unfocusedTextColor = PureWhite
                ),
                shape = RoundedCornerShape(16.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Tone Chips
            Text(
                text = "3. SELECT CREATOR TONE",
                color = TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            val tones = listOf("Viral & High Energy", "Educational SEO", "Storytelling & Mystery", "Short & Punchy")
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(tones) { tone ->
                    val isSel = toneInput == tone
                    FilterChip(
                        selected = isSel,
                        onClick = { viewModel.setTone(tone) },
                        label = { Text(tone, fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = EmeraldGreen,
                            selectedLabelColor = VelvetBlack,
                            containerColor = CardSurface,
                            labelColor = TextSecondary
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSel,
                            borderColor = SurfaceBorder,
                            selectedBorderColor = LightEmerald
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            GoldButton(
                text = if (isGenerating) "AI IS GENERATING..." else "🚀 GENERATE VIRAL METADATA",
                onClick = { viewModel.generateOptimization() },
                isLoading = isGenerating,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("generate_button")
            )

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Optimization Result Section
        currentResult?.let { res ->
            item {
                Text(
                    text = "AI GENERATED OPTIMIZATION",
                    color = MetallicGold,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.5.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                // SEO Score Gauge
                SeoScoreMeter(score = res.seoScore.overallScore)

                Spacer(modifier = Modifier.height(16.dp))

                // Tabs inside result
                val tabs = listOf("Titles (${res.titles.size})", "Hooks", "Hashtags", "Description", "Thumbnail & SEO")
                ScrollableTabRow(
                    selectedTabIndex = activeTabInResult,
                    containerColor = ObsidianSurface,
                    contentColor = MetallicGold,
                    edgePadding = 0.dp,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[activeTabInResult]),
                            color = EmeraldGreen
                        )
                    }
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = activeTabInResult == index,
                            onClick = { activeTabInResult = index },
                            text = {
                                Text(
                                    text = title,
                                    fontSize = 13.sp,
                                    fontWeight = if (activeTabInResult == index) FontWeight.Bold else FontWeight.Normal,
                                    color = if (activeTabInResult == index) MetallicGold else TextMuted
                                )
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Tab 0: AI Titles
            if (activeTabInResult == 0) {
                items(res.titles) { titleText ->
                    TitleCard(titleText = titleText, onCopy = {
                        copyToClipboard(context, "Title", titleText)
                    })
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            // Tab 1: AI Hooks
            if (activeTabInResult == 1) {
                items(res.hooks) { hookItem ->
                    HookCard(hook = hookItem, onCopy = {
                        copyToClipboard(context, "Hook", hookItem.content)
                    })
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            // Tab 2: AI Hashtags
            if (activeTabInResult == 2) {
                item {
                    HashtagSection(
                        hashtags = res.hashtags,
                        onCopyAll = {
                            copyToClipboard(context, "Hashtags", res.hashtags.allHashtags())
                        }
                    )
                }
            }

            // Tab 3: Description
            if (activeTabInResult == 3) {
                item {
                    DescriptionCard(
                        description = res.description,
                        onCopy = { copyToClipboard(context, "Description", res.description) }
                    )
                }
            }

            // Tab 4: Thumbnail & SEO breakdown
            if (activeTabInResult == 4) {
                item {
                    ThumbnailAndSeoSection(res = res, context = context)
                }
            }
        }
    }
}

@Composable
fun TitleCard(titleText: String, onCopy: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(CardSurface, RoundedCornerShape(16.dp))
            .border(1.dp, SurfaceBorder, RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.FormatQuote,
                    contentDescription = null,
                    tint = MetallicGold,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = titleText,
                    color = PureWhite,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = onCopy,
                modifier = Modifier
                    .size(36.dp)
                    .background(ObsidianSurface, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = "Copy Title",
                    tint = EmeraldGreen,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
fun HookCard(hook: HookItem, onCopy: () -> Unit) {
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
                Surface(
                    color = EmeraldGreen.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = hook.type,
                        color = EmeraldGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                Text(
                    text = hook.duration,
                    color = MetallicGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = hook.content,
                color = PureWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable { onCopy() }
            ) {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Copy Hook",
                    color = TextSecondary,
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
fun HashtagSection(
    hashtags: com.example.data.model.HashtagGroup,
    onCopyAll: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(CardSurface, RoundedCornerShape(20.dp))
            .border(1.dp, SurfaceBorder, RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "TAGS BREAKDOWN",
                color = MetallicGold,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )

            Button(
                onClick = onCopyAll,
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = null,
                    tint = VelvetBlack,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Copy All Tags",
                    color = VelvetBlack,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(text = "🔥 High Reach (>1M)", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            hashtags.highReach.forEach { tag ->
                TagChip(tag = tag, color = MetallicGold)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(text = "🎯 Niche Targeted", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            hashtags.niche.forEach { tag ->
                TagChip(tag = tag, color = EmeraldGreen)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(text = "⚡ Trending Now", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            hashtags.trending.forEach { tag ->
                TagChip(tag = tag, color = LightGold)
            }
        }
    }
}

@Composable
fun TagChip(tag: String, color: Color) {
    Surface(
        color = color.copy(alpha = 0.15f),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.4f)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = tag,
            color = color,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun DescriptionCard(description: String, onCopy: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(CardSurface, RoundedCornerShape(20.dp))
            .border(1.dp, SurfaceBorder, RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "SEO DESCRIPTION & TIMESTAMPS",
                    color = MetallicGold,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                IconButton(
                    onClick = onCopy,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy Description",
                        tint = EmeraldGreen
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = description,
                color = PureWhite,
                fontSize = 13.sp,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun ThumbnailAndSeoSection(res: OptimizationResult, context: Context) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(CardSurface, RoundedCornerShape(20.dp))
            .border(1.dp, SurfaceBorder, RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "🖼️ THUMBNAIL CONCEPT",
            color = MetallicGold,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Visual Concept:", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        Text(text = res.thumbnailIdea.concept, color = PureWhite, fontSize = 13.sp)

        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Text Overlay:", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Surface(color = EmeraldGreen.copy(0.2f), shape = RoundedCornerShape(6.dp)) {
                    Text(
                        text = res.thumbnailIdea.textOverlay,
                        color = EmeraldGreen,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(6.dp)
                    )
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Expression Guide:", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Surface(color = MetallicGold.copy(0.2f), shape = RoundedCornerShape(6.dp)) {
                    Text(
                        text = res.thumbnailIdea.facialExpression,
                        color = MetallicGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(text = "AI Image Generator Prompt:", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(ObsidianSurface, RoundedCornerShape(10.dp))
                .padding(10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = res.thumbnailIdea.aiPrompt,
                    color = TextSecondary,
                    fontSize = 11.sp,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = {
                    copyToClipboard(context, "AI Image Prompt", res.thumbnailIdea.aiPrompt)
                }) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy Prompt",
                        tint = MetallicGold,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "📊 SEO SCORE BREAKDOWN",
            color = EmeraldGreen,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        SeoBar("Title Optimization", res.seoScore.titleScore)
        SeoBar("Hook Strength", res.seoScore.hookScore)
        SeoBar("Keyword Density", res.seoScore.keywordDensityScore)
        SeoBar("Hashtag Power", res.seoScore.hashtagPowerScore)
        SeoBar("Call To Action", res.seoScore.ctaScore)

        Spacer(modifier = Modifier.height(12.dp))

        Text(text = "Pro Recommendations:", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        res.seoScore.recommendations.forEach { rec ->
            Row(modifier = Modifier.padding(vertical = 2.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = EmeraldGreen, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = rec, color = PureWhite, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun SeoBar(label: String, score: Int) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text(text = label, color = TextSecondary, fontSize = 12.sp)
            Text(text = "$score/100", color = MetallicGold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(2.dp))
        LinearProgressIndicator(
            progress = { score / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = if (score >= 90) EmeraldGreen else MetallicGold,
            trackColor = ObsidianSurface
        )
    }
}

fun copyToClipboard(context: Context, label: String, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(label, text)
    clipboard.setPrimaryClip(clip)
    Toast.makeText(context, "Copied $label to clipboard!", Toast.LENGTH_SHORT).show()
}
