package com.example.data.model

enum class TargetPlatform(val displayName: String, val iconName: String) {
    TIKTOK("TikTok", "🎵"),
    INSTAGRAM("Instagram Reels", "📸"),
    FACEBOOK("Facebook Reels", "🌐"),
    YOUTUBE("YouTube Shorts", "▶️")
}

data class OptimizationRequest(
    val topic: String,
    val platform: TargetPlatform = TargetPlatform.TIKTOK,
    val tone: String = "Viral & High Energy",
    val keywords: String = ""
)

data class HookItem(
    val type: String, // "Verbal Pattern Interrupt", "Visual Action", "On-Screen Text"
    val content: String,
    val duration: String = "First 3s"
)

data class HashtagGroup(
    val highReach: List<String>,
    val niche: List<String>,
    val trending: List<String>
) {
    fun allHashtags(): String = (highReach + niche + trending).joinToString(" ")
}

data class ThumbnailIdea(
    val concept: String,
    val textOverlay: String,
    val facialExpression: String,
    val colorPalette: String,
    val aiPrompt: String
)

data class SeoScoreBreakdown(
    val overallScore: Int, // 0 - 100
    val titleScore: Int,
    val hookScore: Int,
    val keywordDensityScore: Int,
    val hashtagPowerScore: Int,
    val ctaScore: Int,
    val recommendations: List<String>
)

data class OptimizationResult(
    val id: String = System.currentTimeMillis().toString(),
    val timestamp: Long = System.currentTimeMillis(),
    val topic: String,
    val platform: TargetPlatform,
    val titles: List<String>,
    val hooks: List<HookItem>,
    val hashtags: HashtagGroup,
    val description: String,
    val thumbnailIdea: ThumbnailIdea,
    val seoScore: SeoScoreBreakdown
)
