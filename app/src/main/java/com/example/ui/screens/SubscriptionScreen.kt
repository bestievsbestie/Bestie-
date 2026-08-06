package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SubscriptionPlan
import com.example.ui.components.GoldButton
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel

@Composable
fun SubscriptionScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val userProfile by viewModel.userProfile.collectAsState()
    var selectedPlanToBuy by remember { mutableStateOf(userProfile.currentPlan) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    val trialRemainingMs = userProfile.getTrialRemainingMs()
    val trialHours = trialRemainingMs / (1000 * 60 * 60)
    val trialMinutes = (trialRemainingMs / (1000 * 60)) % 60

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 120.dp, top = 8.dp)
    ) {
        // Free Trial Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(Color(0xFF2C1E00), Color(0xFF003816))
                        ),
                        shape = RoundedCornerShape(24.dp)
                    )
                    .border(
                        width = 1.dp,
                        brush = Brush.horizontalGradient(
                            colors = listOf(MetallicGold, EmeraldGreen)
                        ),
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Surface(color = MetallicGold, shape = RoundedCornerShape(8.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Timer, contentDescription = null, tint = VelvetBlack, modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "1-DAY FREE TRIAL ACTIVE",
                                    color = VelvetBlack,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Trial Ends In: ${trialHours}h ${trialMinutes}m",
                            color = PureWhite,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Enjoy unlimited AI video optimizations.",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = MetallicGold,
                        modifier = Modifier.size(44.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            Text(
                text = "CHOOSE SUBSCRIPTION PLAN",
                color = MetallicGold,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.2.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Cancel anytime. Instant access to all platforms.",
                color = TextSecondary,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Plans List
        val allPlans = listOf(
            SubscriptionPlan.FREE_TRIAL,
            SubscriptionPlan.MONTHLY,
            SubscriptionPlan.THREE_MONTHS,
            SubscriptionPlan.SIX_MONTHS,
            SubscriptionPlan.YEARLY
        )

        items(allPlans) { plan ->
            PlanCard(
                plan = plan,
                isSelected = selectedPlanToBuy == plan,
                isCurrentActive = userProfile.currentPlan == plan,
                onSelect = { selectedPlanToBuy = plan }
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        // Action CTA
        item {
            Spacer(modifier = Modifier.height(12.dp))

            GoldButton(
                text = "SUBSCRIBE TO ${selectedPlanToBuy.title.uppercase()}",
                onClick = {
                    viewModel.upgradePlan(selectedPlanToBuy)
                    showSuccessDialog = true
                },
                useEmeraldGradient = selectedPlanToBuy == SubscriptionPlan.YEARLY,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("subscribe_button")
            )

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Feature Comparison List
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
                        text = "ALL PRO PLANS INCLUDE",
                        color = EmeraldGreen,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    ProFeatureRow("Unlimited AI Titles, Hashtags & Descriptions")
                    ProFeatureRow("Viral Hook Engine (Visual & Verbal Patterns)")
                    ProFeatureRow("Google Gemini 3.5 Flash High-Speed Generation")
                    ProFeatureRow("AI Thumbnail Visual Concepts & Prompts")
                    ProFeatureRow("Real-Time 0-100 SEO Score Audit")
                    ProFeatureRow("Firestore Cloud Backup & Device Sync")
                    ProFeatureRow("Priority 24/7 Support from M Ejaz Team")
                }
            }
        }
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            containerColor = CardSurface,
            title = {
                Text(
                    text = "🎉 Subscription Active!",
                    color = MetallicGold,
                    fontWeight = FontWeight.ExtraBold
                )
            },
            text = {
                Text(
                    text = "You are now subscribed to ${userProfile.currentPlan.title} (${userProfile.currentPlan.price}). Your account has full VIP access to Booster Ejaz.",
                    color = PureWhite,
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = { showSuccessDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen)
                ) {
                    Text("Start Optimizing", color = VelvetBlack, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

@Composable
fun PlanCard(
    plan: SubscriptionPlan,
    isSelected: Boolean,
    isCurrentActive: Boolean,
    onSelect: () -> Unit
) {
    val borderColor = when {
        isSelected -> MetallicGold
        isCurrentActive -> EmeraldGreen
        else -> SurfaceBorder
    }

    val bgColor = if (isSelected) Color(0xFF1F222E) else CardSurface

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onSelect() }
            .padding(14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                RadioButton(
                    selected = isSelected,
                    onClick = onSelect,
                    colors = RadioButtonDefaults.colors(
                        selectedColor = MetallicGold,
                        unselectedColor = TextMuted
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = plan.title,
                            color = PureWhite,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        plan.badge?.let { b ->
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = if (b.contains("BEST") || b.contains("POPULAR")) MetallicGold else EmeraldGreen,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = b,
                                    color = VelvetBlack,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                    if (isCurrentActive) {
                        Text(text = "Currently Active Plan", color = EmeraldGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    } else if (plan.savings != null) {
                        Text(text = plan.savings, color = TextSecondary, fontSize = 11.sp)
                    }
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = plan.price,
                    color = MetallicGold,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = plan.period,
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
fun ProFeatureRow(feature: String) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(18.dp)
                .background(EmeraldGreen.copy(alpha = 0.2f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = EmeraldGreen,
                modifier = Modifier.size(12.dp)
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = feature, color = PureWhite, fontSize = 13.sp)
    }
}
