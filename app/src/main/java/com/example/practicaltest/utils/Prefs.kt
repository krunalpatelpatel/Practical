package com.example.practicaltest.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit


object Prefs {
    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences("PracticalTest", Context.MODE_PRIVATE)
    }

    var authAPI: String
        get() = preferences.getString("authAPI", "") ?: ""
        set(value) = preferences.edit {
            putString("authAPI", value)
        }
}
