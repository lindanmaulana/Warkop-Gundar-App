package com.myproject.warkopgundar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.myproject.warkopgundar.databinding.FragmentDashboardCartBinding
import com.myproject.warkopgundar.databinding.FragmentDashboardMenuBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class DashboardCartFragment : Fragment() {
    private var _binding: FragmentDashboardCartBinding? = null
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
    ): View {
        _binding = FragmentDashboardCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupActions()
    }

    private fun setupActions() {
        binding.actionCheckout.setOnClickListener {
            (requireActivity() as BaseActivity).navigateTo(OrderCheckoutActivity::class.java, typeTransition = AnimType.SLIDE)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DashboardCartFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}