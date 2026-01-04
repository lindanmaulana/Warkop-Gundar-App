package com.myproject.warkopgundar

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.myproject.warkopgundar.databinding.ActivityOrderCheckoutBinding

class OrderCheckoutActivity : BaseActivity() {
    private lateinit var binding: ActivityOrderCheckoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityOrderCheckoutBinding.inflate(layoutInflater)
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
        binding.actionBack.setOnClickListener {
            navigateTo(DashboardActivity::class.java, R.id.actionCart, typeTransition = AnimType.SLIDE, isFinal = true)
        }

        binding.actionNext.setOnClickListener {
            navigateTo(OrderPaymentActivity::class.java, typeTransition = AnimType.SLIDE)
        }
    }
}