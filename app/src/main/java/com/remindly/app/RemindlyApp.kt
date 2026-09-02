package com.remindly.app

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.remindly.app.notification.NotificationHelper
import kotlinx.coroutines.launch

class RemindlyApp : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
        NotificationHelper.ensureChannel(this)

        lifecycleScope().launch {
            container.categoryRepository.ensureDefaultsSeeded()
        }
    }

    private fun lifecycleScope() = ProcessLifecycleOwner.get().lifecycleScope
}
