package com.example.compass.smartprintertest.utils

import android.preference.PreferenceManager
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.example.compass.smartprintertest.data.roomDb.AppDatabase
import org.osmdroid.config.Configuration


class MyApp : MultiDexApplication() {

    init {
        instance = this
    }

    companion object {
        private var instance: MyApp? = null
        lateinit var historyDatabase: AppDatabase


        fun applicationContext(): MyApp {
            return instance as MyApp
        }

    }
    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this@MyApp)
        historyDatabase = AppDatabase.getDatabase(this)

        Configuration.getInstance().apply {
            userAgentValue = packageName
            load(this@MyApp, PreferenceManager.getDefaultSharedPreferences(this@MyApp))
        }

    }


}