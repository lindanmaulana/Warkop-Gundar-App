package com.myproject.warkopgundar.features.order

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.myproject.warkopgundar.utils.AnimType
import com.myproject.warkopgundar.utils.BaseActivity
import com.myproject.warkopgundar.R
import com.myproject.warkopgundar.databinding.ActivityOrderPaymentBinding
import com.myproject.warkopgundar.features.dashboard.DashboardActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

        binding.actionCheckStatus.setOnClickListener {
            setLoading(true)

            lifecycleScope.launch {
                delay(2000)

                val isPaid = true

                setLoading(false)

                if (isPaid) {
                    MaterialAlertDialogBuilder(this@OrderPaymentActivity)
                        .setTitle("Pembayaran Dikonfirmasi")
                        .setMessage("Terima kasih telah setia menjadi bagian Warkop Gundar.")
                        .setCancelable(false)
                        .setPositiveButton("Lihat Status") { _, _ ->
                            navigateTo(OrderPaymentStatusActivity::class.java, isFinal = true)
                        }
                        .show()
                } else {
                    showDialogError("Belum Dibayar", "Kami belum menerima pembayaranmu. Coba cek beberapa saat lagi ya!")
                }
            }
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

    private fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.actionCheckStatus.text = "Memverifikasi..."
            binding.actionCheckStatus.isEnabled = false
            binding.pbLoadingStatus.visibility = View.VISIBLE
        } else {
            binding.actionCheckStatus.text = "Cek Status Pembayaran"
            binding.actionCheckStatus.isEnabled = true
            binding.pbLoadingStatus.visibility = View.GONE
        }
    }
}