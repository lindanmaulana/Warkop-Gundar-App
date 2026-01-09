package com.myproject.warkopgundar

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.myproject.warkopgundar.databinding.ActivitySplashScreenBinding
import com.myproject.warkopgundar.db.AppDatabase
import com.myproject.warkopgundar.features.dashboard.DashboardActivity
import com.myproject.warkopgundar.utils.AnimType
import com.myproject.warkopgundar.utils.BaseActivity
import com.myproject.warkopgundar.utils.SessionManager
import kotlinx.coroutines.Dispatchers
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

        lifecycleScope.launch(Dispatchers.IO) {
            delay(SPLASH_TIME_LOAD)

            Log.d("DB_CHECK", "Mencoba mengakses database...")
            val db = AppDatabase.getDatabase(applicationContext)

            val cursor = db.query("SELECT * FROM menus", null)
            val count = cursor.count
            cursor.close()

            Log.d("DB_CHECK", "Berhasil akses! Jumlah baris di tabel menu: $count")

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