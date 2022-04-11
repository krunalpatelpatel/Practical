package com.example.practicaltest.utils

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import com.example.practicaltest.databinding.LoadingBinding

class LoadingDialog(
    context: Context,
    cancelable: Boolean,
    cancelListener: DialogInterface.OnCancelListener?
) : Dialog(context, cancelable, cancelListener) {

    lateinit var binding: LoadingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val dividerId = context.resources
            .getIdentifier("android:id/titleDivider", null, null)
        val divider = findViewById<View>(dividerId)
        if (divider != null) {
            divider.background = null
        }
    }
}