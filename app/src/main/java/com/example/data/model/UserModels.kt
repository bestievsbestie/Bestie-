package com.example.data.model

enum class SubscriptionPlan(
    val title: String,
    val price: String,
    val period: String,
    val badge: String?,
    val savings: String?
) {
    FREE_TRIAL("1-Day Free Trial", "$0.00", "24 Hours", "ACTIVE TRIAL", null),
    MONTHLY("Monthly Booster", "$9.99", "/month", null, null),
    THREE_MONTHS("Quarterly Pass", "$24.99", "/3 months", "SAVE 15%", "Save 15%"),
    SIX_MONTHS("Semi-Annual Pro", "$44.99", "/6 months", "POPULAR", "Save 25%"),
    YEARLY("Ejaz Gold VIP", "$79.99", "/year", "BEST VALUE", "Save 33%")
}

data class UserProfile(
    val uid: String = "guest_user_509",
    val email: String = "creator@boosterejaz.com",
    val displayName: String = "Ejaz Creator",
    val photoUrl: String? = null,
    val isGoogleLogin: Boolean = false,
    val currentPlan: SubscriptionPlan = SubscriptionPlan.FREE_TRIAL,
    val trialStartTimeMs: Long = System.currentTimeMillis(),
    val isSubscriptionActive: Boolean = true,
    val totalOptimizationsCount: Int = 14
) {
    fun getTrialRemainingMs(): Long {
        val trialDurationMs = 24 * 60 * 60 * 1000L // 24 hours
        val elapsed = System.currentTimeMillis() - trialStartTimeMs
        val remaining = trialDurationMs - elapsed
        return if (remaining > 0) remaining else 0L
    }
}
