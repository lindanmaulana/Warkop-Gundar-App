package com.myproject.warkopgundar

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.myproject.warkopgundar.databinding.ActivitySettingContactBinding

class SettingContactActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingContactBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySettingContactBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        setupToolbar()
        setupActions()

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbarContact)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)

        binding.toolbarContact.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupActions() {
        binding.actionContactInstagram.setOnClickListener {
            val igUsername = "__lindanmaulana"
            val uri = Uri.parse("http://instagram.com/_u/$igUsername")
            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                setPackage("com.instagram.android")
            }

            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/$igUsername")))
            }
        }

        binding.actionContactWhatsapp.setOnClickListener {
            val phoneNumber = "6285322701120"
            val message = "Halo Warkop Gundar, saya ingin bertanya..."
            val url = "https://api.whatsapp.com/send?phone=$phoneNumber&text=${Uri.encode(message)}"

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }
}