package com.remindly.app.util

/**
 * Centralized ad-serving hook. Deliberately a no-op in this build — no ad SDK is wired up, so
 * there is nothing intrusive to show during development. Premium users must never see ads;
 * callers should always gate on [PremiumRepository.isPremium] before calling [shouldShowAds].
 * Wiring in Google Mobile Ads later only means implementing the real logic in here.
 */
object AdsManager {
    fun shouldShowAds(isPremium: Boolean): Boolean = !isPremium && ADS_ENABLED

    private const val ADS_ENABLED = false
}
