package com.myproject.warkopgundar

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.myproject.warkopgundar.databinding.FragmentDashboardCartBinding
import com.myproject.warkopgundar.databinding.FragmentDashboardMenuBinding
import com.myproject.warkopgundar.utils.toParseCurrency

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class DashboardCartFragment : Fragment() {
    private var _binding: FragmentDashboardCartBinding? = null
    private val binding get() = _binding!!

    private lateinit var cartAdapter: CartAdapter
    private val cartViewModel: CartViewModel by activityViewModels()
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

        setupContent()
        setupActions()
        setupRecyclerView()
    }

    private fun setupContent() {
        cartViewModel.cartItems.observe(viewLifecycleOwner) { items ->
            Log.d("CEK_DATA", "Jumlah item di keranjang: ${items?.size ?: 0}")

            when {
                items.isEmpty() -> {
                    binding.rvCart.visibility = View.GONE
                    binding.layoutEmpty.visibility = View.VISIBLE
                    binding.layoutBottom.visibility = View.GONE
                }

                else -> {
                    binding.rvCart.visibility = View.VISIBLE
                    binding.layoutEmpty.visibility = View.GONE
                    binding.layoutBottom.visibility = View.VISIBLE
                    cartAdapter.submitList(items?.toList())
                    setUpTotalPrice(items)
                }
            }
        }
    }

    private fun setUpTotalPrice(items: List<CartWithMenu>) {
        val total = items.sumOf { item ->
            item.menu.price * item.cart.quantity
        }

        binding.tvTotalPrice.text = total.toParseCurrency()
    }

    private fun setupActions() {
        binding.actionCheckout.setOnClickListener {
            (requireActivity() as BaseActivity).navigateTo(OrderCheckoutActivity::class.java, typeTransition = AnimType.SLIDE)
        }
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(
            onPlusClick = { item ->
                cartViewModel.addToCart(item.menu, item.cart.userId)
            },

            onMinClick = { item ->
                cartViewModel.minusFromCart(item.menu, item.cart.userId)
            },

            onDeleteClick = { item ->
                cartViewModel.removeFromCart(item.menu, item.cart.userId)
            }
        )

        binding.rvCart.adapter = cartAdapter
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