package com.example.practicaltest

import android.app.Application
import com.example.practicaltest.utils.Prefs

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Prefs.init(this)
    }
}