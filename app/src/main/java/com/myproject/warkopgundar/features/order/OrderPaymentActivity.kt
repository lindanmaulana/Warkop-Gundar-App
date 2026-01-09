package com.myproject.warkopgundar.features.order

import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.myproject.warkopgundar.utils.AnimType
import com.myproject.warkopgundar.utils.BaseActivity
import com.myproject.warkopgundar.R
import com.myproject.warkopgundar.databinding.ActivityOrderPaymentBinding
import com.myproject.warkopgundar.features.dashboard.DashboardActivity

class OrderPaymentActivity : BaseActivity() {
    private lateinit var binding: ActivityOrderPaymentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityOrderPaymentBinding.inflate(layoutInflater)
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
        binding.actionNext.setOnClickListener {
            navigateTo(OrderPaymentStatusActivity::class.java, typeTransition = AnimType.SLIDE)
        }

        binding.actionBack.setOnClickListener {
            val dialog = MaterialAlertDialogBuilder(this).setTitle("Batalkan Pembayaran?")
                .setMessage("Jika Anda Kembali, proses pembayaran saat ini akan di batalkan. Pastikan Anda belum memindai kode QR.")
                .setNegativeButton("LANJUTKAN BAYAR") {dialog, which ->
                    dialog.dismiss()
                }
                .setPositiveButton("YA, BATALKAN") {dialog, which ->
                    navigateTo(DashboardActivity::class.java, R.id.actionMenu, isFinal = true)
                }.show()

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED)
        }
    }
}