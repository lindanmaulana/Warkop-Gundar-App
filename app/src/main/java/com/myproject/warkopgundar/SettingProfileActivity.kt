package com.myproject.warkopgundar

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import com.myproject.warkopgundar.databinding.ActivitySettingProfileBinding
import com.myproject.warkopgundar.utils.showErrorSnackBar
import kotlinx.coroutines.launch

class SettingProfileActivity : BaseActivity() {
    private lateinit var binding: ActivitySettingProfileBinding

    private lateinit var db: AppDatabase
    private lateinit var session: SessionManager

    private var userPhoneNumber: String? = null

    private lateinit var username: TextView
    private lateinit var phoneNumber: TextView
    private lateinit var inputUsername: TextInputEditText
    private lateinit var inputPhoneNumber: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySettingProfileBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        db = AppDatabase.getDatabase(this@SettingProfileActivity)
        session = SessionManager(this@SettingProfileActivity)

        userPhoneNumber = session.getPhoneNumber()

        username = binding.tvHeaderUsername
        phoneNumber = binding.tvHeaderPhoneNumber
        inputUsername = binding.inputUsername
        inputPhoneNumber = binding.inputPhoneNumber

        if (userPhoneNumber == null) {
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
            val currentPhone = userPhoneNumber

            if (currentPhone == null) {
                session.logout()
                navigateToWithData(
                    destination = AuthSigninActivity::class.java,
                    extra = "Sesi anda telah habis, Harap masuk kembali.",
                    key = ExtraKey.MESSAGE,
                    isFinal = true)
                return@setOnClickListener
            }

            val valInputUsername = inputUsername.text.toString().trim()
            if (valInputUsername.isEmpty()) {
                showDialogError("Validasi Error", "Nama tidak boleh kosong!")
                return@setOnClickListener
            }

            binding.actionUpdate.isEnabled = false

            lifecycleScope.launch {
                try {
                    val user = db.userDao().getUserByPhoneNumber(currentPhone)

                    if (user == null) {
                        showDialogError("Badrequest Error", "Akun anda belum terdaftar!")
                        return@launch
                    }

                    val result = db.userDao().updateUserProfile(valInputUsername, currentPhone)
                    if (result < 0) {
                        showDialogError("Unknown Error", "Terjadi kesalahan tidak terduga, please try again later!")
                        return@launch
                    }

                    navigateToWithData(
                        destination = DashboardActivity::class.java,
                        extra = "Profil Berhasil Diubah",
                        key = ExtraKey.MESSAGE,
                        targetMenuId = R.id.actionSetting)
                } catch (e: android.database.sqlite.SQLiteConstraintException) {
                    binding.root.showErrorSnackBar("Terjadi kesalahan tidak terduga, please try again later", binding.tvHeaderUsername)
                } catch (e: Exception) {
                    binding.root.showErrorSnackBar("Terjadi kesalahan sistem, please try again later", binding.tvHeaderUsername)
                } finally {
                    binding.actionUpdate.isEnabled = true
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val currentPhone = userPhoneNumber

        if (currentPhone == null) {
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
                val user = db.userDao().getUserByPhoneNumber(currentPhone)

                if (user != null) {
                    username.text = user.username

                    val rawPhone = user.phoneNumber ?: ""
                    if (rawPhone.length > 7) {
                        val maskedPhone = rawPhone.replaceRange(3, 7, "****")
                        phoneNumber.text = maskedPhone
                        inputPhoneNumber.setText(maskedPhone)
                        inputPhoneNumber.isEnabled = false
                    } else {
                        phoneNumber.text = rawPhone
                    }
                }
            } catch (e: android.database.sqlite.SQLiteConstraintException) {
                binding.root.showErrorSnackBar("Terjadi kesalahan tidak terduga, please try again later", binding.tvHeaderUsername)
            } catch (e: Exception) {
                binding.root.showErrorSnackBar("Terjadi kesalahan sistem, please try again later", binding.tvHeaderUsername)
            }
        }
    }
}