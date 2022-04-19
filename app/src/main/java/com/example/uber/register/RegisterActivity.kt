package com.example.uber.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.uber.R
import com.example.uber.databinding.ActivityRegisterBinding
import com.example.uber.databinding.ActivitySplashBinding

class RegisterActivity : AppCompatActivity() {
    private val binding: ActivityRegisterBinding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}