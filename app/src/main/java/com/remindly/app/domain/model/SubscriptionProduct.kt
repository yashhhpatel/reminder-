package com.remindly.app.domain.model

/** Live pricing for one subscription, as returned by Google Play at query time. */
data class SubscriptionProduct(
    val productId: String,
    val formattedPrice: String,
    val billingPeriod: String,
)

/**
 * Subscription product IDs. These must be created as Play Console subscription products with
 * these exact IDs (Monetize > Products > Subscriptions) before purchases will work.
 */
object PremiumProductIds {
    const val YEARLY = "remindly_premium_yearly"
    const val MONTHLY = "remindly_premium_monthly"
    const val WEEKLY = "remindly_premium_weekly"

    val ALL = listOf(YEARLY, MONTHLY, WEEKLY)
}
