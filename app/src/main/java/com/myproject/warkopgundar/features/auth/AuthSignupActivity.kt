package com.myproject.warkopgundar.features.auth

import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import com.myproject.warkopgundar.utils.AnimType
import com.myproject.warkopgundar.db.AppDatabase
import com.myproject.warkopgundar.utils.BaseActivity
import com.myproject.warkopgundar.db.User
import com.myproject.warkopgundar.databinding.ActivityAuthSignupBinding
import com.myproject.warkopgundar.utils.showErrorSnackBar
import com.myproject.warkopgundar.utils.showSuccessSnackBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AuthSignupActivity : BaseActivity() {
    private lateinit var db: AppDatabase
    private lateinit var binding: ActivityAuthSignupBinding
    private lateinit var inputUsername: TextInputEditText
    private lateinit var inputEmail: TextInputEditText
    private lateinit var inputPhoneNumber: TextInputEditText
    private lateinit var inputPassword: TextInputEditText
    private lateinit var inputConfirmPassword: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthSignupBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        db = AppDatabase.Companion.getDatabase(this@AuthSignupActivity)

        inputUsername = binding.inputUsername
        inputEmail = binding.inputEmail
        inputPhoneNumber = binding.inputPhoneNumber
        inputPassword = binding.inputPassword
        inputConfirmPassword = binding.inputConfirmPassword

        binding.actionSignup.setOnClickListener {
            val userName = inputUsername.text.toString().trim()
            val email = inputEmail.text.toString().trim()
            val phoneNumber = inputPhoneNumber.text.toString().trim()
            val password = inputPassword.text.toString().trim()
            val confirmPassword = inputConfirmPassword.text.toString().trim()

            if (userName.isEmpty() || phoneNumber.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                binding.root.showErrorSnackBar("Semua kolom wajib diisi!", binding.actionSignup)
                return@setOnClickListener
            }

            when {
                !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    binding.root.showErrorSnackBar("Format email salah (Contoh: user@email.com)", binding.actionSignup)
                    return@setOnClickListener
                }

                !email.endsWith("@gmail.com") -> {
                    binding.root.showErrorSnackBar("Hanya menerima email @gmail.com", binding.actionSignup)
                    return@setOnClickListener
                }
            }

            when {
                !phoneNumber.startsWith("08") -> {
                    binding.root.showErrorSnackBar("Gunakan format 08 (Contoh: 087723...)", binding.actionSignup)
                    return@setOnClickListener
                }

                !phoneNumber.all { it.isDigit() } -> {
                    binding.root.showErrorSnackBar("Nomor telepon hanya boleh berisi angka!", binding.actionSignup)
                    return@setOnClickListener
                }

                phoneNumber.length !in 10..13 -> {
                    binding.root.showErrorSnackBar("Nomor handphone harus 10-13 digit!", binding.actionSignup)
                    return@setOnClickListener
                }
            }

            when {
                password.length < 6 -> {
                    binding.root.showErrorSnackBar("Password minimal 6 karakter", binding.actionSignup)
                    return@setOnClickListener
                }

                password != confirmPassword -> {
                    binding.root.showErrorSnackBar("Password dan ConfirmPassword tidak valid", binding.actionSignup)
                    return@setOnClickListener
                }
            }

            val dataUser = User(
                username = userName,
                email = email,
                phoneNumber = phoneNumber,
                password = password
            )

            serviceSignup(dataUser)
        }

        binding.actionToSignin.setOnClickListener {
            navigateTo(AuthSigninActivity::class.java, typeTransition = AnimType.SLIDE)
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun serviceSignup(data: User) {
        lifecycleScope.launch {
            try {
                db.userDao().insertUser(data)
                binding.root.showSuccessSnackBar("Registrasi Berhasil", binding.actionSignup)
                delay(1000)

                navigateTo(AuthSigninActivity::class.java, isFinal = true)
            } catch (e: Exception) {
                e.printStackTrace()
                binding.root.showErrorSnackBar("Terjadi kesalahan sistem, please try again later", binding.actionSignup)
            }
        }
    }
}