package com.remindly.app.data.repository

import com.remindly.app.data.datastore.SettingsDataStore
import com.remindly.app.domain.repository.PremiumRepository
import kotlinx.coroutines.flow.Flow

/**
 * Development/MVP implementation: entitlement is just a persisted flag the user can flip from
 * the Premium screen's "Continue" button. Replace with a Google Play Billing–backed
 * implementation when real payments are wired up — nothing else in the app depends on how
 * entitlement is determined.
 */
class PremiumRepositoryImpl(
    private val settingsDataStore: SettingsDataStore,
) : PremiumRepository {

    override val isPremium: Flow<Boolean> = settingsDataStore.isPremium

    override suspend fun setPremium(value: Boolean) {
        settingsDataStore.setPremium(value)
    }

    override suspend fun restorePurchases(): Boolean {
        // No real billing backend in this build; nothing to restore.
        return false
    }
}
