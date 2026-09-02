package com.remindly.app.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * Abstraction over entitlement state. The dev/debug implementation just flips a persisted
 * boolean (see PremiumRepositoryImpl) so the app is fully testable without real billing.
 * Swapping in Google Play Billing later only requires a new implementation of this interface.
 */
interface PremiumRepository {
    val isPremium: Flow<Boolean>
    suspend fun setPremium(value: Boolean)
    suspend fun restorePurchases(): Boolean
}
