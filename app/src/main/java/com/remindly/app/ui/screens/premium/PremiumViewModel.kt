package com.remindly.app.ui.screens.premium

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.remindly.app.domain.model.SubscriptionProduct
import com.remindly.app.domain.repository.PremiumRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PremiumViewModel(private val premiumRepository: PremiumRepository) : ViewModel() {

    val isPremium: StateFlow<Boolean> = premiumRepository.isPremium
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val products: StateFlow<List<SubscriptionProduct>> = premiumRepository.products
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** Launches Play's purchase sheet. On success [isPremium] flips to true and the screen closes. */
    fun purchase(activity: Activity, productId: String) {
        premiumRepository.launchPurchaseFlow(activity, productId)
    }

    fun restore() {
        viewModelScope.launch { premiumRepository.restorePurchases() }
    }
}
