package com.myproject.warkopgundar.features.dashboard.fragments

import android.database.sqlite.SQLiteConstraintException
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.myproject.warkopgundar.utils.AnimType
import com.myproject.warkopgundar.db.AppDatabase
import com.myproject.warkopgundar.utils.BaseActivity
import com.myproject.warkopgundar.utils.DialogSuccess
import com.myproject.warkopgundar.utils.ExtraKey
import com.myproject.warkopgundar.utils.SessionManager
import com.myproject.warkopgundar.features.dashboard.fragments.settings.SettingContactActivity
import com.myproject.warkopgundar.features.dashboard.fragments.settings.SettingProfileActivity
import com.myproject.warkopgundar.features.auth.AuthSigninActivity
import com.myproject.warkopgundar.databinding.FragmentDashboardSettingBinding
import com.myproject.warkopgundar.utils.maskEmail
import com.myproject.warkopgundar.utils.showErrorSnackBar
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class DashboardSettingFragment : Fragment() {
    private var _binding: FragmentDashboardSettingBinding? = null
    private val binding get() = _binding!!

    private lateinit var db: AppDatabase
    private lateinit var session: SessionManager
    private lateinit var username: TextView
    private lateinit var email: TextView

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDashboardSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        session = SessionManager(requireContext())
        db = AppDatabase.Companion.getDatabase(requireContext())
        username = binding.tvHeaderUsername
        email = binding.tvHeaderEmail

        val message = requireActivity().intent.getStringExtra(ExtraKey.MESSAGE.value)
        if (message != null) {
            val successDialog = DialogSuccess(message)
            successDialog.show(requireActivity().supportFragmentManager, "success_dialog")

            requireActivity().intent.removeExtra(ExtraKey.MESSAGE.value)
        }

        setupActions()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupActions() {
        binding.actionProfile.setOnClickListener {
            (requireActivity() as BaseActivity).navigateTo(SettingProfileActivity::class.java, typeTransition = AnimType.SLIDE)
        }

        binding.actionOrderHistory.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext()).setTitle("Riwayat Pesanan")
                .setMessage("Fitur ini akan segera hadir.")
                .setNegativeButton("OK") {dialog, which ->
                    dialog.dismiss()
                }.show()
        }

        binding.actionVoucherPromo.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext()).setTitle("Voucher & Promo")
                .setMessage("Fitur ini akan segera hadir.")
                .setNegativeButton("OK") {dialog, which ->
                    dialog.dismiss()
                }.show()
        }

        binding.actionMyAddress.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext()).setTitle("Alamat Saya")
                .setMessage("Fitur ini akan segera hadir.")
                .setNegativeButton("OK") {dialog, which ->
                    dialog.dismiss()
                }.show()
        }

        binding.actionNotification.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext()).setTitle("Notifikasi")
                .setMessage("Fitur ini akan segera hadir.")
                .setNegativeButton("OK") {dialog, which ->
                    dialog.dismiss()
                }.show()
        }

        binding.actionApplication.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext()).setTitle("Tampilan Aplikasi")
                .setMessage("Fitur ini akan segera hadir.")
                .setNegativeButton("OK") {dialog, which ->
                    dialog.dismiss()
                }.show()
        }

        binding.actionSettingLanguage.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext()).setTitle("Bahasa")
                .setMessage("Fitur ini akan segera hadir.")
                .setNegativeButton("OK") {dialog, which ->
                    dialog.dismiss()
                }.show()
        }

        binding.actionAccountSecurity.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext()).setTitle("Keamanan Akun")
                .setMessage("Fitur ini akan segera hadir.")
                .setNegativeButton("OK") {dialog, which ->
                    dialog.dismiss()
                }.show()
        }

        binding.actionHelpSupport.setOnClickListener {
            (requireActivity() as BaseActivity).navigateTo(SettingContactActivity::class.java, typeTransition = AnimType.SLIDE)
        }

        binding.actionLogout.setOnClickListener {
            val dialog = MaterialAlertDialogBuilder(requireContext()).setTitle("Konfirmasi Logout")
                .setMessage("Apakah Anda yakin ingin keluar? Anda perlu masuk lagi untuk menggunakan aplikasi ini.")
                .setNegativeButton("BATAL") {dialog, which ->
                    dialog.dismiss()
                }
                .setPositiveButton("KELUAR") {dialog, which ->
                    session.logout()
                    (requireActivity() as BaseActivity).navigateToWithData(
                        destination = AuthSigninActivity::class.java,
                        extra = "Kamu telah berhasil keluar. Sampai jumpa lagi di Warkop Gundar!",
                        key = ExtraKey.SESSION_LOGOUT,
                        isFinal = true)
                }.show()

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED)
        }
    }

    override fun onResume() {
        super.onResume()

        val userEmail = session.getUserEmail()

        if (userEmail == null) {
            session.logout()
            (requireActivity() as BaseActivity).navigateTo(AuthSigninActivity::class.java, isFinal = true)
            return
        }

        lifecycleScope.launch {
            try {
                val user = db.userDao().getUserByEmail(userEmail)

                when {
                    isAdded && user != null -> {
                        username.text = user.username
                        email.text = maskEmail(user.email)
                    }

                    else -> {
                        session.logout()
                        (requireActivity() as BaseActivity).navigateTo(AuthSigninActivity::class.java, isFinal = true)
                    }
                }
            }  catch (e: Exception) {
                Log.e("ProfileFragment", "Error loading user: ${e.message}")
                binding.root.showErrorSnackBar("Terjadi kesalahan sistem, please try again later", binding.tvHeaderUsername)
            }
        }
    }
}