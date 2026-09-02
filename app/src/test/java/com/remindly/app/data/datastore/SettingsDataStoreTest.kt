package com.remindly.app.data.datastore

import android.os.Build
import androidx.test.core.app.ApplicationProvider
import com.remindly.app.domain.model.AppThemeMode
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.TIRAMISU])
class SettingsDataStoreTest {

    private lateinit var settingsDataStore: SettingsDataStore

    @Before
    fun setUp() {
        settingsDataStore = SettingsDataStore(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun `theme mode defaults to auto and persists changes`() = runBlocking {
        assertEquals(AppThemeMode.AUTO, settingsDataStore.themeMode.first())

        settingsDataStore.setThemeMode(AppThemeMode.DARK)
        assertEquals(AppThemeMode.DARK, settingsDataStore.themeMode.first())
    }

    @Test
    fun `onboarding done defaults to false and persists true`() = runBlocking {
        assertFalse(settingsDataStore.onboardingDone.first())

        settingsDataStore.setOnboardingDone(true)
        assertTrue(settingsDataStore.onboardingDone.first())
    }

    @Test
    fun `preset toggles default correctly`() = runBlocking {
        assertTrue(settingsDataStore.presetAddTime.first())
        assertFalse(settingsDataStore.presetAddPlace.first())
        assertTrue(settingsDataStore.presetAfterCall.first())
    }

    @Test
    fun `premium flag persists`() = runBlocking {
        assertFalse(settingsDataStore.isPremium.first())
        settingsDataStore.setPremium(true)
        assertTrue(settingsDataStore.isPremium.first())
    }
}
