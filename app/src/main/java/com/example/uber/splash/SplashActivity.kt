package com.example.uber.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.uber.App
import com.example.uber.R
import com.example.uber.databinding.ActivitySplashBinding
import com.example.uber.register.RegisterActivity
import com.example.uber.register.customer.CustomerMapsActivity
import com.example.uber.register.driver.DriverMapsActivity
import com.example.uber.register.driver.OrderList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private val binding: ActivitySplashBinding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()


        CoroutineScope(Dispatchers.Main).launch {
            delay(9000L)
            binding.splashLottie.playAnimation()
            binding.splashLottie.speed = 1F

            App.pref = getSharedPreferences("pref", MODE_PRIVATE)
            when (App.pref?.getString("userType", "none") as String) {
                "customer" -> startActivity(Intent(this@SplashActivity, CustomerMapsActivity::class.java))
                "driver" -> startActivity(Intent(this@SplashActivity, OrderList::class.java))
                "none" -> startActivity(Intent(this@SplashActivity,RegisterActivity::class.java))
            }


//            startActivity(Intent(this@SplashActivity,RegisterActivity::class.java))
        }
    }
}