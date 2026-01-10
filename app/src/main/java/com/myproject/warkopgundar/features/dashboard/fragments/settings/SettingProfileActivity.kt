package com.myproject.warkopgundar.features.dashboard.fragments.settings

import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import com.myproject.warkopgundar.utils.BaseActivity
import com.myproject.warkopgundar.utils.ExtraKey
import com.myproject.warkopgundar.R
import com.myproject.warkopgundar.utils.SessionManager
import com.myproject.warkopgundar.features.auth.AuthSigninActivity
import com.myproject.warkopgundar.databinding.ActivitySettingProfileBinding
import com.myproject.warkopgundar.db.AppDatabase
import com.myproject.warkopgundar.features.dashboard.DashboardActivity
import com.myproject.warkopgundar.utils.maskEmail
import com.myproject.warkopgundar.utils.showErrorSnackBar
import kotlinx.coroutines.launch

class SettingProfileActivity : BaseActivity() {
    private lateinit var binding: ActivitySettingProfileBinding

    private lateinit var db: AppDatabase
    private lateinit var session: SessionManager

    private var userEmail: String? = null

    private lateinit var username: TextView
    private lateinit var email: TextView
    private lateinit var inputUsername: TextInputEditText
    private lateinit var inputPhoneNumber: TextInputEditText
    private lateinit var inputEmail: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySettingProfileBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        db = AppDatabase.getDatabase(this@SettingProfileActivity)
        session = SessionManager(this@SettingProfileActivity)

        userEmail = session.getUserEmail()

        username = binding.tvHeaderUsername
        email = binding.tvHeaderEmail
        inputUsername = binding.inputUsername
        inputPhoneNumber = binding.inputPhoneNumber
        inputEmail = binding.inputEmail

        if (userEmail == null) {
            session.logout()
            navigateToWithData(
                destination = AuthSigninActivity::class.java,
                extra = "Demi keamanan, sesi Anda telah habis. Silakan masuk kembali.",
                key = ExtraKey.SESSION_EXPIRED,
                isFinal = true)
            return
        }

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
        binding.actionUpdate.setOnClickListener {
            val currentEmail = userEmail

            if (currentEmail == null) {
                session.logout()
                navigateToWithData(
                    destination = AuthSigninActivity::class.java,
                    extra = "Sesi anda telah habis, Harap masuk kembali.",
                    key = ExtraKey.MESSAGE,
                    isFinal = true)
                return@setOnClickListener
            }

            val valInputUsername = inputUsername.text.toString().trim()
            val valInputPhoneNumber = inputPhoneNumber.text.toString().trim()

            if (valInputUsername.isBlank() && valInputPhoneNumber.isBlank()) {
                showDialogError("Validasi Error", "Harap isi minimal salah satu: Nama atau Nomor Handphone!")
                return@setOnClickListener
            }

            if (valInputPhoneNumber.isNotBlank()) {
                when {
                    !valInputPhoneNumber.all { it.isDigit() } -> {
                        showDialogError("Validasi Error", "Nomor handphone hanya boleh berisi angka!")
                        return@setOnClickListener
                    }

                    !valInputPhoneNumber.startsWith("08") -> {
                        showDialogError("Validasi Error", "Format nomor salah! Gunakan awalan 08")
                        return@setOnClickListener
                    }

                    valInputPhoneNumber.length !in 10..13 -> {
                        showDialogError("Validasi Error","Nomor handphone harus 10-13 digit!")
                        return@setOnClickListener
                    }
                }
            }

            binding.actionUpdate.isEnabled = false
            lifecycleScope.launch {
                try {
                    val user = db.userDao().getUserByEmail(currentEmail)

                    if (user == null) {
                        showDialogError("Badrequest Error", "Akun anda belum terdaftar!")
                        return@launch
                    }

                    val result = db.userDao().updateUserProfile(valInputUsername, currentEmail)
                    if (result < 0) {
                        showDialogError("Unknown Error", "Terjadi kesalahan tidak terduga, please try again later!")
                        return@launch
                    }

                    navigateToWithData(
                        destination = DashboardActivity::class.java,
                        extra = "Profil Berhasil Diubah",
                        key = ExtraKey.MESSAGE,
                        targetMenuId = R.id.actionSetting)
                } catch (e: Exception) {
                    e.printStackTrace()
                    binding.root.showErrorSnackBar("Terjadi kesalahan sistem, please try again later", binding.tvHeaderUsername)
                } finally {
                    binding.actionUpdate.isEnabled = true
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val currentEmail = userEmail

        if (currentEmail == null) {
            session.logout()
            navigateToWithData(
                destination = AuthSigninActivity::class.java,
                extra = "Sesi anda telah habis, Harap masuk kembali.",
                key = ExtraKey.MESSAGE,
                isFinal = true)
            return
        }

        lifecycleScope.launch {
            try {
                val user = db.userDao().getUserByEmail(currentEmail)

                when {
                    user != null -> {
                        username.text = user.username
                        email.text = maskEmail(user.email)

                        inputEmail.setText(maskEmail(user.email))
                        inputEmail.isEnabled = false
                    }

                    else -> {
                        session.logout()
                        navigateToWithData(
                            destination = AuthSigninActivity::class.java,
                            extra = "Sesi anda telah habis, Harap masuk kembali.",
                            key = ExtraKey.MESSAGE,
                            isFinal = true)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                binding.root.showErrorSnackBar("Terjadi kesalahan sistem, please try again later", binding.tvHeaderUsername)
            }
        }
    }
}