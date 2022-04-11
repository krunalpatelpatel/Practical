package com.example.practicaltest.activity

import androidx.appcompat.app.AppCompatActivity
import com.example.practicaltest.utils.LoadingDialog

open class MainAppCompatActivity : AppCompatActivity() {
    private var dialog: LoadingDialog? = null

    fun showLoadingOverlay() {
        if (dialog != null) hideLoadingOverlay()

        dialog = LoadingDialog(this, true, null)
        dialog!!.show()
    }

    fun hideLoadingOverlay() {
        if (dialog == null) return

        dialog?.dismiss()
        dialog = null
    }
}