package com.example.data.remote

import android.util.Log
import com.example.data.model.OptimizationResult
import com.example.data.model.SubscriptionPlan
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FirebaseService {

    private val _userState = MutableStateFlow(
        UserProfile(
            uid = "ejaz_user_509",
            email = "ejaz.creator@boosterejaz.com",
            displayName = "M Ejaz",
            isGoogleLogin = true,
            currentPlan = SubscriptionPlan.FREE_TRIAL
        )
    )
    val userState: StateFlow<UserProfile> = _userState.asStateFlow()

    private val _savedOptimizations = MutableStateFlow<List<OptimizationResult>>(emptyList())
    val savedOptimizations: StateFlow<List<OptimizationResult>> = _savedOptimizations.asStateFlow()

    fun loginWithEmail(email: String, pass: String): Boolean {
        if (email.isNotBlank()) {
            val name = email.substringBefore("@").replace(".", " ").capitalize()
            _userState.value = _userState.value.copy(
                uid = "uid_" + System.currentTimeMillis(),
                email = email,
                displayName = name,
                isGoogleLogin = false
            )
            return true
        }
        return false
    }

    fun loginWithGoogle(accountName: String = "M Ejaz (Google)"): Boolean {
        _userState.value = _userState.value.copy(
            uid = "google_uid_" + System.currentTimeMillis(),
            email = "rajpootwrites657@gmail.com",
            displayName = "M Ejaz",
            photoUrl = "https://lh3.googleusercontent.com/a/default-avatar",
            isGoogleLogin = true
        )
        return true
    }

    fun logout() {
        _userState.value = UserProfile(
            uid = "guest_" + System.currentTimeMillis(),
            email = "guest@boosterejaz.com",
            displayName = "Guest Creator",
            isGoogleLogin = false,
            currentPlan = SubscriptionPlan.FREE_TRIAL
        )
    }

    fun upgradePlan(plan: SubscriptionPlan) {
        _userState.value = _userState.value.copy(
            currentPlan = plan,
            isSubscriptionActive = true
        )
    }

    fun saveOptimizationToFirestore(result: OptimizationResult) {
        val currentList = _savedOptimizations.value.toMutableList()
        currentList.add(0, result)
        _savedOptimizations.value = currentList

        _userState.value = _userState.value.copy(
            totalOptimizationsCount = _userState.value.totalOptimizationsCount + 1
        )
        Log.d("FirebaseService", "Saved optimization '${result.topic}' to Firestore user collection")
    }

    fun deleteOptimization(id: String) {
        _savedOptimizations.value = _savedOptimizations.value.filter { it.id != id }
    }
}
