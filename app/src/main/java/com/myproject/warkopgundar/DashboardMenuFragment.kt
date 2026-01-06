package com.myproject.warkopgundar

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import com.myproject.warkopgundar.databinding.FragmentDashboardMenuBinding
import com.myproject.warkopgundar.utils.showErrorSnackBar
import com.myproject.warkopgundar.utils.showSuccessSnackBar
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class DashboardMenuFragment : Fragment() {
    private lateinit var menuAdapter: MenuAdapter
    private lateinit var db: AppDatabase

    private var _binding: FragmentDashboardMenuBinding? = null
    private val binding get() = _binding!!
    private var param1: String? = null
    private var param2: String? = null

    private var allMenuItems: List<Menu> = listOf()

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
        menuAdapter = MenuAdapter { menu -> navigateToDetail(menu) }
        db = AppDatabase.getDatabase(requireContext())

        serviceGetAllMenu()
        setupActionCategory()
    }

    private fun setupSections(listMenu: List<Menu>) {
        binding.sectionKopi.tvCategoryTitle.text = "Kopi"
        val adapterKopi = MenuAdapter{menu -> navigateToDetail(menu)}.apply { isGridView = true }
        binding.sectionKopi.rvHorizontalMenu.adapter = adapterKopi
        adapterKopi.submitList(listMenu.filter { it.categoryId == MenuCategory.COFFE })

        binding.sectionMie.tvCategoryTitle.text = "Mie"
        val adapterMie = MenuAdapter{menu -> navigateToDetail(menu)}.apply { isGridView = true }
        binding.sectionMie.rvHorizontalMenu.adapter = adapterMie
        adapterMie.submitList(listMenu.filter { it.categoryId == MenuCategory.MIE })

        binding.sectionNasi.tvCategoryTitle.text = "Nasi"
        val adapterNasi = MenuAdapter{menu -> navigateToDetail(menu)}.apply { isGridView = true }
        binding.sectionNasi.rvHorizontalMenu.adapter = adapterNasi
        adapterNasi.submitList(listMenu.filter { it.categoryId == MenuCategory.RICE })
    }

    private fun navigateToDetail(menu: Menu) {
        (requireActivity() as? BaseActivity)?.navigateToWithData(
            destination = MenuDetailActivity::class.java,
            extra = menu,
        )
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

    private fun showFilteredMenu(categoryId: Int) {
        binding.containerMenu.visibility = View.GONE
        binding.containerMenuFiltered.visibility = View.VISIBLE

        val filteredAdapter = MenuAdapter {menu -> navigateToDetail(menu)}
        filteredAdapter.isGridView = false

        binding.containerMenuFiltered.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = filteredAdapter
        }

        serviceGetByCategory(categoryId, filteredAdapter)
    }

    private fun updateUi(actionActive: MaterialButton) {
        val actions = listOf(binding.actionCategoryAll, binding.actionCategoryCoffe, binding.actionCategoryMie, binding.actionCategoryRice)

        actions.forEach { action ->
            if (action == actionActive) {
                action.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.primary))
                action.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                action.alpha = 0.5f
            } else {
                action.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.primary))
                action.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                action.alpha = 1.0f
            }
        }
    }

    fun applyFilter(categoryId: Int) {
        when (categoryId) {
            MenuCategory.COFFE -> {
                showFilteredMenu(MenuCategory.COFFE)
                updateUi(binding.actionCategoryCoffe)
            }
            MenuCategory.MIE -> {
                showFilteredMenu(MenuCategory.MIE)
                updateUi(binding.actionCategoryMie)
            }
            MenuCategory.RICE -> {
                showFilteredMenu(MenuCategory.RICE)
                updateUi(binding.actionCategoryRice)
            }
            else -> {
                binding.containerMenu.visibility =  View.VISIBLE
                binding.containerMenuFiltered.visibility = View.GONE
                updateUi(binding.actionCategoryAll)
            }
        }
    }

    private fun serviceGetAllMenu() {
        lifecycleScope.launch {
            try {
                db.menuDao().getAllMenu().collect { listMenu ->
                    allMenuItems = listMenu
                    setupSections(listMenu)
                }
            } catch (e: android.database.sqlite.SQLiteConstraintException) {
                binding.root.showErrorSnackBar("Terjadi kesalahan tidak terduga, please try again later", binding.containerMenu)
            } catch (e: Exception) {
                binding.root.showErrorSnackBar("Terjadi kesalahan sistem, please try again later", binding.containerMenu)
            }
        }
    }

    private fun serviceGetByCategory(categoryId: Int, adapter: MenuAdapter) {
        lifecycleScope.launch {
            try {
                db.menuDao().getMenuByCategory(categoryId).collect { listMenu ->
                    adapter.submitList(listMenu)
                }
            } catch (e: android.database.sqlite.SQLiteConstraintException) {
                binding.root.showErrorSnackBar("Terjadi kesalahan tidak terduga, please try again later", binding.containerMenu)
            } catch (e: Exception) {
                binding.root.showErrorSnackBar("Terjadi kesalahan sistem, please try again later", binding.containerMenu)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}