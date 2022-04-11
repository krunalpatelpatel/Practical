package com.example.practicaltest.activity

import android.os.Bundle
import com.example.practicaltest.databinding.ActivityMainBinding

class MainActivity : MainAppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}