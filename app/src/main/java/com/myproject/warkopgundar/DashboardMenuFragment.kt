package com.myproject.warkopgundar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import com.myproject.warkopgundar.databinding.FragmentDashboardMenuBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class DashboardMenuFragment : Fragment() {
    private lateinit var menuAdapter: MenuAdapter

    private var _binding: FragmentDashboardMenuBinding? = null
    private val binding get() = _binding!!
    private var param1: String? = null
    private var param2: String? = null

    private val allMenuItems = listOf(
        // --- KATEGORI KOPI ---
        MenuModel(1, "Cappuccino", "With Steamed Milk", "IDR 8.000", "4.5", "12K", R.drawable.img_menu_coffe, MenuCategory.COFFE),
        MenuModel(2, "Espresso", "Strong & Bold", "IDR 10.000", "4.7", "8K", R.drawable.img_menu_coffe, MenuCategory.COFFE),
        MenuModel(3, "Coffee Latte", "Creamy Texture", "IDR 12.000", "4.6", "15K", R.drawable.img_menu_coffe, MenuCategory.COFFE),
        MenuModel(4, "Americano", "Pure Black Coffee", "IDR 7.000", "4.4", "10K", R.drawable.img_menu_coffe, MenuCategory.COFFE),
        MenuModel(5, "Moccachino", "Coffee & Chocolate", "IDR 13.000", "4.8", "9K", R.drawable.img_menu_coffe, MenuCategory.COFFE),

        // --- KATEGORI MIE ---
        MenuModel(6, "Indomie Kuah", "Extra Telur", "IDR 12.000", "4.8", "167K", R.drawable.img_menu_mie, MenuCategory.MIE),
        MenuModel(7, "Indomie Goreng", "Double Porsi", "IDR 15.000", "4.9", "200K", R.drawable.img_menu_mie, MenuCategory.MIE),
        MenuModel(8, "Mie Nyemek", "Pedas Level 5", "IDR 14.000", "4.7", "50K", R.drawable.img_menu_mie, MenuCategory.MIE),
        MenuModel(9, "Mie Ayam Bakso", "Pangsit Goreng", "IDR 18.000", "4.6", "30K", R.drawable.img_menu_mie, MenuCategory.MIE),
        MenuModel(10, "Mie Goreng Aceh", "Rempah Spesial", "IDR 17.000", "4.5", "25K", R.drawable.img_menu_mie, MenuCategory.MIE),

        // --- KATEGORI NASI ---
        MenuModel(11, "Nasi Goreng", "Ayam Suwir", "IDR 15.000", "4.9", "80K", R.drawable.img_menu_rice, MenuCategory.RICE),
        MenuModel(12, "Nasi Gila", "Sosis & Bakso", "IDR 18.000", "4.8", "40K", R.drawable.img_menu_rice, MenuCategory.RICE),
        MenuModel(13, "Nasi Ayam Geprek", "Sambal Bawang", "IDR 20.000", "4.7", "100K", R.drawable.img_menu_rice, MenuCategory.RICE),
        MenuModel(14, "Nasi Kuning", "Lauk Komplit", "IDR 12.000", "4.6", "20K", R.drawable.img_menu_rice, MenuCategory.RICE),
        MenuModel(15, "Nasi Uduk", "Semur Jengkol", "IDR 13.000", "4.5", "15K", R.drawable.img_menu_rice, MenuCategory.RICE)
    )

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
        _binding = FragmentDashboardMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        menuAdapter = MenuAdapter()

        setupSections()
        setupActionCategory()
    }

    private fun setupSections() {
        binding.sectionKopi.tvCategoryTitle.text = "Kopi"
        val adapterKopi = MenuAdapter().apply { isGridView = true }
        binding.sectionKopi.rvHorizontalMenu.adapter = adapterKopi
        adapterKopi.submitList(allMenuItems.filter { it.category == MenuCategory.COFFE })

        binding.sectionMie.tvCategoryTitle.text = "Mie"
        val adapterMie = MenuAdapter().apply { isGridView = true }
        binding.sectionMie.rvHorizontalMenu.adapter = adapterMie
        adapterMie.submitList(allMenuItems.filter { it.category == MenuCategory.MIE })

        binding.sectionNasi.tvCategoryTitle.text = "Nasi"
        val adapterNasi = MenuAdapter().apply { isGridView = true }
        binding.sectionNasi.rvHorizontalMenu.adapter = adapterNasi
        adapterNasi.submitList(allMenuItems.filter { it.category == MenuCategory.RICE })
    }

    private fun setupActionCategory(){
        updateUi(binding.actionCategoryAll)

        binding.actionCategoryAll.setOnClickListener {
            binding.containerMenu.visibility =  View.VISIBLE
            binding.containerMenuFiltered.visibility = View.GONE
            updateUi(binding.actionCategoryAll)
        }

        binding.actionCategoryCoffe.setOnClickListener {
            showFilteredMenu(MenuCategory.COFFE)
            updateUi(binding.actionCategoryCoffe)
        }

        binding.actionCategoryMie.setOnClickListener {
            showFilteredMenu(MenuCategory.MIE)
            updateUi(binding.actionCategoryMie)
        }

        binding.actionCategoryRice.setOnClickListener {
            showFilteredMenu(MenuCategory.RICE)
            updateUi(binding.actionCategoryRice)
        }
    }

    private fun showFilteredMenu(category: String) {
        binding.containerMenu.visibility = View.GONE
        binding.containerMenuFiltered.visibility = View.VISIBLE

        val filteredList = allMenuItems.filter { it.category == category }

        menuAdapter.isGridView = false
        binding.containerMenuFiltered.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = menuAdapter
        }

        menuAdapter.submitList(filteredList)
    }

    private fun updateUi(actionActive: MaterialButton) {
        val actions = listOf(binding.actionCategoryAll, binding.actionCategoryCoffe, binding.actionCategoryMie, binding.actionCategoryRice)

        actions.forEach { action ->
            if (action == actionActive) {
                action.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.primary))
                action.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                action.alpha = 1.0f
            } else {
                action.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray))
                action.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                action.alpha = 0.5f
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}