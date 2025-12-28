package com.myproject.warkopgundar

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import com.myproject.warkopgundar.databinding.ActivityAuthSignupBinding
import com.myproject.warkopgundar.utils.showErrorSnackBar
import com.myproject.warkopgundar.utils.showSuccessSnackBar
import kotlinx.coroutines.launch

class AuthSignupActivity : BaseActivity() {
    private lateinit var db: AppDatabase
    private lateinit var binding: ActivityAuthSignupBinding
    private lateinit var inputUsername: TextInputEditText
    private lateinit var inputPhoneNumber: TextInputEditText
    private lateinit var inputPassword: TextInputEditText
    private lateinit var inputConfirmPassword: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthSignupBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        db = AppDatabase.getDatabase(this@AuthSignupActivity)

        inputUsername = binding.inputUsername
        inputPhoneNumber = binding.inputPhoneNumber
        inputPassword = binding.inputPassword
        inputConfirmPassword = binding.inputConfirmPassword

        binding.actionSignup.setOnClickListener {
            val userName = inputUsername.text.toString().trim()
            val phoneNumber = inputPhoneNumber.text.toString().trim()
            val password = inputPassword.text.toString().trim()
            val confirmPassword = inputConfirmPassword.text.toString().trim()

            if (userName.isEmpty() || phoneNumber.isEmpty()) {
                binding.root.showErrorSnackBar("Nama dan Nomor Hp wajib diisi!", binding.actionSignup)
                return@setOnClickListener
            }

            if (password.length < 6) {
                binding.root.showErrorSnackBar("Password minimal 6 karakter", binding.actionSignup)
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                binding.root.showErrorSnackBar("Password dan ConfirmPassword tidak valid", binding.actionSignup)
                return@setOnClickListener
            }

            val dataUser = User(
                username = userName,
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
                kotlinx.coroutines.delay(1000)

                navigateTo(AuthSigninActivity::class.java, isFinal = true)
            } catch (e: android.database.sqlite.SQLiteConstraintException) {
                binding.root.showErrorSnackBar("Nomor Handphone telah terdaftar", binding.actionSignup)
            } catch (e: Exception) {
                binding.root.showErrorSnackBar("Terjadi kesalahan sistem, please try again later", binding.actionSignup)
            }
        }
    }
}