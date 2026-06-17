package com.fajary.focus.data

import android.content.Context
import androidx.room.Room

object AppDatabaseProvider {
    @Volatile
    private var instance: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase
    {
        synchronized(this)
        {
            if (instance == null) {
                val building: AppDatabase = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_db"
                ).build()

                instance = building
            }

            return instance!!;
        }
    }
}