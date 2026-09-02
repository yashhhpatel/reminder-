package com.remindly.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.remindly.app.data.local.converter.Converters
import com.remindly.app.data.local.dao.CategoryDao
import com.remindly.app.data.local.dao.ReminderDao
import com.remindly.app.data.local.entity.CategoryEntity
import com.remindly.app.data.local.entity.ReminderEntity

@Database(
    entities = [ReminderEntity::class, CategoryEntity::class],
    version = 1,
    exportSchema = true,
)
@TypeConverters(Converters::class)
abstract class RemindlyDatabase : RoomDatabase() {

    abstract fun reminderDao(): ReminderDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        @Volatile
        private var INSTANCE: RemindlyDatabase? = null

        fun getInstance(context: Context): RemindlyDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    RemindlyDatabase::class.java,
                    "remindly.db",
                ).build().also { INSTANCE = it }
            }
    }
}
