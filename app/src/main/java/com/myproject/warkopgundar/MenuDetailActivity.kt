package com.myproject.warkopgundar

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.myproject.warkopgundar.databinding.ActivityMenuDetailBinding

class MenuDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityMenuDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMenuDetailBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding.actionBack.setOnClickListener {
            navigateTo(DashboardActivity::class.java, R.id.actionMenu, typeTransition = AnimType.SLIDE, isFinal = true)
        }

        binding.actionCheckout.setOnClickListener {
            navigateTo(OrderCheckoutActivity::class.java, typeTransition = AnimType.SLIDE)
        }

        binding.actionAddToCart.setOnClickListener {
            val successDialog = DialogSuccess("Berhasil Ditambahkan ke favorite")
            successDialog.show(supportFragmentManager, "success_dialog")
            binding.etNote.text?.clear()
        }

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}