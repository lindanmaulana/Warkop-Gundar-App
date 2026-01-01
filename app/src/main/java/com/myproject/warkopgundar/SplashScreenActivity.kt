package com.myproject.warkopgundar

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.myproject.warkopgundar.databinding.ActivitySplashScreenBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenActivity : BaseActivity() {
    private val SPLASH_TIME_LOAD: Long = 1000
    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        val session = SessionManager(this@SplashScreenActivity)

        lifecycleScope.launch {
            delay(SPLASH_TIME_LOAD)

            val targetActivity = when {
                session.isLoggedIn() -> DashboardActivity::class.java

                else -> MainActivity::class.java
            }

            navigateTo(targetActivity, typeTransition = AnimType.SLIDE, isFinal = true)
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}