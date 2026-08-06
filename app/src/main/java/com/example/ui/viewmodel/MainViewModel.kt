package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.remote.FirebaseService
import com.example.data.remote.GeminiService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val geminiService = GeminiService()
    val firebaseService = FirebaseService()

    val userProfile: StateFlow<UserProfile> = firebaseService.userState
    val savedOptimizations: StateFlow<List<OptimizationResult>> = firebaseService.savedOptimizations

    private val _selectedPlatform = MutableStateFlow(TargetPlatform.TIKTOK)
    val selectedPlatform: StateFlow<TargetPlatform> = _selectedPlatform.asStateFlow()

    private val _topicInput = MutableStateFlow("")
    val topicInput: StateFlow<String> = _topicInput.asStateFlow()

    private val _keywordsInput = MutableStateFlow("")
    val keywordsInput: StateFlow<String> = _keywordsInput.asStateFlow()

    private val _toneInput = MutableStateFlow("Viral & High Energy")
    val toneInput: StateFlow<String> = _toneInput.asStateFlow()

    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating.asStateFlow()

    private val _currentResult = MutableStateFlow<OptimizationResult?>(null)
    val currentResult: StateFlow<OptimizationResult?> = _currentResult.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    // Active screen navigation index
    private val _selectedTab = MutableStateFlow(0) // 0: Optimizer, 1: Analytics, 2: Subscription, 3: Account
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    init {
        // Pre-populate with sample optimization for immediate rich preview
        viewModelScope.launch {
            val sampleReq = OptimizationRequest(
                topic = "How to explode on TikTok with AI in 2026",
                platform = TargetPlatform.TIKTOK,
                tone = "Viral & High Energy"
            )
            val sampleRes = geminiService.generateOptimization(sampleReq)
            _currentResult.value = sampleRes
            firebaseService.saveOptimizationToFirestore(sampleRes)
        }
    }

    fun setPlatform(platform: TargetPlatform) {
        _selectedPlatform.value = platform
    }

    fun setTopic(topic: String) {
        _topicInput.value = topic
    }

    fun setKeywords(keywords: String) {
        _keywordsInput.value = keywords
    }

    fun setTone(tone: String) {
        _toneInput.value = tone
    }

    fun setSelectedTab(index: Int) {
        _selectedTab.value = index
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    fun showToast(msg: String) {
        _toastMessage.value = msg
    }

    fun generateOptimization() {
        val topic = _topicInput.value.ifBlank { "How to go viral on " + _selectedPlatform.value.displayName }
        _isGenerating.value = true

        viewModelScope.launch {
            try {
                val req = OptimizationRequest(
                    topic = topic,
                    platform = _selectedPlatform.value,
                    tone = _toneInput.value,
                    keywords = _keywordsInput.value
                )
                val result = geminiService.generateOptimization(req)
                _currentResult.value = result
                firebaseService.saveOptimizationToFirestore(result)
                _toastMessage.value = "✨ AI Metadata Generated Successfully!"
            } catch (e: Exception) {
                _toastMessage.value = "Error: ${e.localizedMessage}"
            } finally {
                _isGenerating.value = false
            }
        }
    }

    fun loginWithEmail(email: String, pass: String) {
        if (firebaseService.loginWithEmail(email, pass)) {
            _toastMessage.value = "Welcome back, Creator!"
        } else {
            _toastMessage.value = "Please enter a valid email address."
        }
    }

    fun loginWithGoogle() {
        firebaseService.loginWithGoogle()
        _toastMessage.value = "Google Login Successful! Connected as M Ejaz."
    }

    fun logout() {
        firebaseService.logout()
        _toastMessage.value = "Logged out successfully."
    }

    fun upgradePlan(plan: SubscriptionPlan) {
        firebaseService.upgradePlan(plan)
        _toastMessage.value = "🎉 Subscribed to ${plan.title}! All Pro features unlocked."
    }
}
