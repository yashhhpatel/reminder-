package com.remindly.app.ui.screens.premium

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.remindly.app.domain.repository.PremiumRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PremiumViewModel(private val premiumRepository: PremiumRepository) : ViewModel() {

    val isPremium: StateFlow<Boolean> = premiumRepository.isPremium
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    private val _purchaseCompleted = MutableStateFlow(false)
    val purchaseCompleted: StateFlow<Boolean> = _purchaseCompleted

    fun continuePurchase() {
        viewModelScope.launch {
            premiumRepository.setPremium(true)
            _purchaseCompleted.value = true
        }
    }

    fun restore() {
        viewModelScope.launch {
            val restored = premiumRepository.restorePurchases()
            if (restored) _purchaseCompleted.value = true
        }
    }
}
