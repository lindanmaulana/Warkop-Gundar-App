package com.myproject.warkopgundar

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.myproject.warkopgundar.databinding.ActivityAuthSigninBinding
import com.myproject.warkopgundar.databinding.ActivitySplashScreenBinding
import com.myproject.warkopgundar.utils.showErrorSnackBar
import com.myproject.warkopgundar.utils.showSuccessSnackBar
import kotlinx.coroutines.launch

class AuthSigninActivity : BaseActivity() {
    private lateinit var db: AppDatabase
    private lateinit var session: SessionManager
    private lateinit var binding: ActivityAuthSigninBinding
    private lateinit var inputPhoneNumber: TextInputEditText
    private lateinit var inputPassword: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthSigninBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        db = AppDatabase.getDatabase(this@AuthSigninActivity)
        session = SessionManager(this@AuthSigninActivity)

        inputPhoneNumber = binding.inputPhoneNumber
        inputPassword = binding.inputPassword

        binding.actionSignin.setOnClickListener {
            val phoneNumber = inputPhoneNumber.text.toString().trim()
            val password = inputPassword.text.toString().trim()

            if (phoneNumber.isEmpty() || password.isEmpty()) {
                binding.root.showErrorSnackBar("Nomor HP dan Password tidak boleh kosong", binding.actionSignin)

                return@setOnClickListener
            }

            serviceSignin(phoneNumber, password)
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

    private fun serviceSignin(phoneNumber: String, password: String) {
        lifecycleScope.launch {
            try {
                val result = db.userDao().getUserByPhoneNumber(phoneNumber)

                if (result == null) {
                    binding.root.showErrorSnackBar("Invalid Credentials!", binding.actionSignin)
                    return@launch
                }

                binding.root.showSuccessSnackBar("Login Berhasil", binding.actionSignin)
                session.createLoginSession(result.phoneNumber)
                kotlinx.coroutines.delay(1000)

                navigateTo(DashboardActivity::class.java, isFinal = true)
            } catch (e: android.database.sqlite.SQLiteConstraintException) {
                binding.root.showErrorSnackBar("Terjadi kesalahan tidak terduga, please try again later", binding.actionSignin)
            } catch (e: Exception) {
                binding.root.showErrorSnackBar("Terjadi kesalahan sistem, please try again later", binding.actionSignin)
            }
        }
    }
}