package com.myproject.warkopgundar.features.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.myproject.warkopgundar.R
import com.myproject.warkopgundar.databinding.FragmentOnboardingBinding

class OnboardingFragment : Fragment() {
    private var _binding: FragmentOnboardingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val position = arguments?.getInt(ARG_POSITION) ?: 0

        when (position) {
            0 -> {
                binding.tvTitle.text = "Hallo, Sobat Gundar!"
                binding.tvDesc.text = "Tempat paling asik buat ngerjain tugas atau sekadar sambat soal dosen sambil ngopi."
                 binding.ivOnboarding.setImageResource(R.drawable.img_phone_1)
            }
            1 -> {
                binding.tvTitle.text = "Rasa Yang Selalu Ngangenin"
                binding.tvDesc.text = "Dari racikan kopi klasik sampai mi instan legendaris, semua dibuat spesial untuk menemani waktu santaimu."
                binding.ivOnboarding.setImageResource(R.drawable.img_phone_2)
            }
            2 -> {
                binding.tvTitle.text = "Jadi Bagian Dari Kami"
                binding.tvDesc.text = "Cari cabang Warkop Gundar terdekat dan temukan komunitas baru yang siap bikin harimu lebih berwarna."
                binding.ivOnboarding.setImageResource(R.drawable.img_phone_3)
            }
        }
    }

    companion object {
        private const val ARG_POSITION = "position"

        fun newInstance(position: Int) = OnboardingFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_POSITION, position)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}