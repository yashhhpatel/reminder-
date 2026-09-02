package com.remindly.app

import android.content.Context
import com.remindly.app.data.datastore.SettingsDataStore
import com.remindly.app.data.local.RemindlyDatabase
import com.remindly.app.data.repository.CategoryRepositoryImpl
import com.remindly.app.data.repository.PremiumRepositoryImpl
import com.remindly.app.data.repository.ReminderRepositoryImpl
import com.remindly.app.domain.repository.CategoryRepository
import com.remindly.app.domain.repository.PremiumRepository
import com.remindly.app.domain.repository.ReminderRepository
import com.remindly.app.domain.repository.ReminderScheduler
import com.remindly.app.notification.AlarmScheduler

/**
 * Simple hand-rolled service locator. The app is small enough that a DI framework would add
 * ceremony without real benefit; every dependency is constructed once here and handed out by
 * the [RemindlyViewModelFactory].
 */
class AppContainer(context: Context) {
    val database: RemindlyDatabase = RemindlyDatabase.getInstance(context)
    val settingsDataStore: SettingsDataStore = SettingsDataStore(context)
    val reminderScheduler: ReminderScheduler = AlarmScheduler(context)

    val reminderRepository: ReminderRepository =
        ReminderRepositoryImpl(database.reminderDao(), reminderScheduler)

    val categoryRepository: CategoryRepository =
        CategoryRepositoryImpl(database.categoryDao(), database.reminderDao())

    val premiumRepository: PremiumRepository = PremiumRepositoryImpl(settingsDataStore)
}
