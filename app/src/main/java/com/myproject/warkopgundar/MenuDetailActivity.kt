package com.myproject.warkopgundar

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.content.IntentCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.myproject.warkopgundar.databinding.ActivityMenuDetailBinding
import com.myproject.warkopgundar.utils.toParseCurrency

class MenuDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityMenuDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMenuDetailBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val dataMenu = IntentCompat.getParcelableExtra(intent, ExtraKey.MENU.value, Menu::class.java)

        if (dataMenu != null) setupDetailmenu(dataMenu)
        setupActions()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupActions() {
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
    }

    private fun setupDetailmenu(data: Menu) {
        binding.tvMenuName.text = data.name
        binding.tvMenuPrice.text = data.price.toParseCurrency()
        binding.tvMenuDescription.text = data.description
        binding.imgMenu.setImageResource(data.imageRes ?: R.drawable.img_placeholder)
    }
}