package com.myproject.warkopgundar.features.auth

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import com.myproject.warkopgundar.utils.AnimType
import com.myproject.warkopgundar.db.AppDatabase
import com.myproject.warkopgundar.utils.BaseActivity
import com.myproject.warkopgundar.features.dashboard.DashboardActivity
import com.myproject.warkopgundar.utils.ExtraKey
import com.myproject.warkopgundar.utils.SessionManager
import com.myproject.warkopgundar.databinding.ActivityAuthSigninBinding
import com.myproject.warkopgundar.utils.showErrorSnackBar
import com.myproject.warkopgundar.utils.showSuccessSnackBar
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AuthSigninActivity : BaseActivity() {
    private lateinit var db: AppDatabase
    private lateinit var session: SessionManager
    private lateinit var binding: ActivityAuthSigninBinding
    private lateinit var inputEmail: TextInputEditText
    private lateinit var inputPassword: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthSigninBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        db = AppDatabase.Companion.getDatabase(this@AuthSigninActivity)
        session = SessionManager(this@AuthSigninActivity)

        val messageSessionLogout = intent.getStringExtra(ExtraKey.SESSION_LOGOUT.value)
        val messageSessionExpired = intent.getStringExtra(ExtraKey.SESSION_EXPIRED.value)

        if (messageSessionLogout != null) {
            showDialogSuccess("Akun Keluar", messageSessionLogout)

            intent.removeExtra(ExtraKey.SESSION_LOGOUT.value)
        }

        if (messageSessionExpired != null) {
            showDialogSuccess("Sesi Berakhir", messageSessionExpired)

            intent.removeExtra(ExtraKey.SESSION_EXPIRED.value)
        }

        inputEmail = binding.inputEmail
        inputPassword = binding.inputPassword

        binding.actionSignin.setOnClickListener {
            val email = inputEmail.text.toString().trim()
            val password = inputPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                binding.root.showErrorSnackBar("Semua kolom tidak boleh kosong!", binding.actionSignin)

                return@setOnClickListener
            }

            serviceSignin(email, password)
        }

        binding.actionToSignup.setOnClickListener {
            navigateTo(AuthSignupActivity::class.java, typeTransition = AnimType.SLIDE)
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun serviceSignin(email: String, password: String) {
        lifecycleScope.launch {
            try {
                val result = db.userDao().getUserByEmail(email)

                if (result == null) {
                    binding.root.showErrorSnackBar("Invalid Credentials!", binding.actionSignin)
                    return@launch
                }

                if (password != result.password) {
                    binding.root.showErrorSnackBar("Invalid Credentials!", binding.actionSignin)
                    return@launch
                }

                binding.root.showSuccessSnackBar("Login Berhasil", binding.actionSignin)
                session.createLoginSession(result.id, result.email)
                delay(1000)

                navigateTo(DashboardActivity::class.java, isFinal = true)
            } catch (e: Exception) {
                e.printStackTrace()
                if (e is CancellationException) throw e

                binding.root.showErrorSnackBar("Terjadi kesalahan sistem, please try again later", binding.actionSignin)
            }
        }
    }
}