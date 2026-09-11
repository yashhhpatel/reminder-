package com.remindly.app

import android.app.Application
import android.content.Context
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.remindly.app.notification.NotificationHelper
import com.remindly.app.util.AppLocale
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class RemindlyApp : Application() {

    lateinit var container: AppContainer
        private set

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(AppLocale.wrap(base))
    }

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
        NotificationHelper.ensureChannel(this)
        // One-time migration for installs that already had a non-English language saved in our
        // settings DataStore from before per-app language switching actually did anything.
        val savedCode = runBlocking { container.settingsDataStore.language.first() }
        AppLocale.migrateIfNeeded(this, savedCode)

        lifecycleScope().launch {
            container.categoryRepository.ensureDefaultsSeeded()
        }
    }

    private fun lifecycleScope() = ProcessLifecycleOwner.get().lifecycleScope
}
