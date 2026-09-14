package com.remindly.app.domain.repository

import android.app.Activity
import com.remindly.app.domain.model.SubscriptionProduct
import kotlinx.coroutines.flow.Flow

/**
 * Abstraction over entitlement state, backed by Google Play Billing (see
 * BillingRepositoryImpl). [isPremium] reflects the last-known entitlement cached locally so the
 * UI has an instant answer even before the billing connection is ready; it is kept in sync with
 * Play on connect, after a purchase, and on [restorePurchases].
 */
interface PremiumRepository {
    val isPremium: Flow<Boolean>

    /** Live subscription pricing from Play, populated once the billing connection is ready. */
    val products: Flow<List<SubscriptionProduct>>

    /** Launches Play's purchase UI for the given product ID. Result arrives via [isPremium]. */
    fun launchPurchaseFlow(activity: Activity, productId: String)

    /** Re-queries Play for active purchases and refreshes [isPremium]. Returns the result. */
    suspend fun restorePurchases(): Boolean
}
