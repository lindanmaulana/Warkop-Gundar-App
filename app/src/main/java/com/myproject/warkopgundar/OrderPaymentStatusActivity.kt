package com.myproject.warkopgundar

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.myproject.warkopgundar.databinding.ActivityOrderPaymentStatusBinding

class OrderPaymentStatusActivity : BaseActivity() {
    private lateinit var binding: ActivityOrderPaymentStatusBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityOrderPaymentStatusBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        setupActions()
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupActions() {
        binding.actionFinish.setOnClickListener {
            navigateTo(DashboardActivity::class.java, R.id.actionHome, isFinal = true)
        }
    }
}