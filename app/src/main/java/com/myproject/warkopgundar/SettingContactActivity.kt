package com.myproject.warkopgundar

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.myproject.warkopgundar.databinding.ActivitySettingContactBinding

class SettingContactActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingContactBinding
    private lateinit var webview: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySettingContactBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        webview = binding.webViewMaps

        setupToolbar()
        setupActions()
        setupMaps()

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

    private fun setupMaps() {
        webview.settings.javaScriptEnabled = true
        webview.settings.domStorageEnabled = true
        webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val url = request?.url?.toString()

                if (url != null && (url.contains("google.com/maps") || url.contains("maps.app.goo.gl"))) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(intent)
                    return true
                }
                return false
            }
        }

        val embedHtml = """
                            <html>
                                <body style="margin:0;padding:0;">
                                    <iframe 
                                        src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3966.2607667870925!2d106.8485553!3d-6.229311999999999!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x2e69f32a742b14b1%3A0x7506eb5bfe02cebb!2sKPP%20Madya%20Dua%20Jakarta%20Selatan%20I!5e0!3m2!1sen!2sid!4v1767598144401!5m2!1sen!2sid" width="600" height="450" style="border:0;" allowfullscreen="" loading="lazy" referrerpolicy="no-referrer-when-downgrade" 
                                        width="100%" 
                                        height="100%" 
                                        style="border:0;" 
                                        allowfullscreen="" 
                                        loading="lazy">
                                    </iframe>
                                </body>
                            </html>
                        """.trimIndent()

        webview.loadData(embedHtml, "text/html", "utf-8")
    }
}