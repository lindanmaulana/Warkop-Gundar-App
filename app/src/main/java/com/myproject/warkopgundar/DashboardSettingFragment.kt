package com.myproject.warkopgundar

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.dialog.MaterialDialogs
import com.myproject.warkopgundar.databinding.FragmentDashboardSettingBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class DashboardSettingFragment : Fragment() {
    private lateinit var session: SessionManager
    private var _binding: FragmentDashboardSettingBinding? = null
    private val binding get() = _binding!!
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
        // Inflate the layout for this fragment
        _binding = FragmentDashboardSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        session = SessionManager(requireContext())

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
            MaterialAlertDialogBuilder(requireContext()).setTitle("Bantuan & Hubungi Kami")
                .setMessage("Fitur ini akan segera hadir.")
                .setNegativeButton("OK") {dialog, which ->
                    dialog.dismiss()
                }.show()
        }

        binding.actionLogout.setOnClickListener {
            val dialog = MaterialAlertDialogBuilder(requireContext()).setTitle("Konfirmasi Logout")
                .setMessage("Apakah Anda yakin ingin keluar? Anda perlu masuk lagi untuk menggunakan aplikasi ini.")
                .setNegativeButton("BATAL") {dialog, which ->
                    dialog.dismiss()
                }
                .setPositiveButton("KELUAR") {dialog, which ->
                    session.logout()
                    (requireActivity() as BaseActivity).navigateTo(AuthSigninActivity::class.java, isFinal = true)
                }.show()

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}