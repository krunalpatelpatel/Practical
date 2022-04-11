package com.example.practicaltest.utils

import android.app.Activity
import com.google.android.material.snackbar.Snackbar

fun Activity.showSnackbar(msg: String) {
    Snackbar.make(window.decorView.findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG)
        .show()
}