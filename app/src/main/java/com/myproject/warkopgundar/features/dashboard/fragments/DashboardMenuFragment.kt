package com.myproject.warkopgundar.features.dashboard.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import com.myproject.warkopgundar.db.AppDatabase
import com.myproject.warkopgundar.utils.BaseActivity
import com.myproject.warkopgundar.features.dashboard.fragments.carts.CartViewModel
import com.myproject.warkopgundar.utils.DialogSuccess
import com.myproject.warkopgundar.utils.ExtraKey
import com.myproject.warkopgundar.db.Menu
import com.myproject.warkopgundar.db.MenuCategory
import com.myproject.warkopgundar.R
import com.myproject.warkopgundar.utils.SessionManager
import com.myproject.warkopgundar.databinding.FragmentDashboardMenuBinding
import com.myproject.warkopgundar.features.dashboard.fragments.menus.MenuAdapter
import com.myproject.warkopgundar.features.dashboard.fragments.menus.MenuDetailActivity
import com.myproject.warkopgundar.utils.showErrorSnackBar
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class DashboardMenuFragment : Fragment() {
    private lateinit var menuAdapter: MenuAdapter
    private val cartViewModel: CartViewModel by activityViewModels()
    private lateinit var session: SessionManager
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
        menuAdapter = MenuAdapter(
            onItemClick = { menu -> navigateToDetail(menu) },
            onAddClick = { menu -> onAddMenuClicked(menu) }
        )
        db = AppDatabase.Companion.getDatabase(requireContext())
        session = SessionManager(requireContext())

        serviceGetAllMenu()
        setupActionCategory()

        val categoryId = arguments?.getInt("SELECTED_CATEGORY", -1) ?: -1

        if (categoryId != -1) {
            applyFilter(categoryId)
        }
    }

    private fun setupSections(listMenu: List<Menu>) {
        binding.sectionKopi.tvCategoryTitle.text = "Kopi"
        val adapterKopi = MenuAdapter(
            onItemClick = { menu -> navigateToDetail(menu) },
            onAddClick = { menu -> onAddMenuClicked(menu) }
        ).apply { isGridView = true }
        binding.sectionKopi.rvHorizontalMenu.adapter = adapterKopi
        adapterKopi.submitList(listMenu.filter { it.categoryId == MenuCategory.COFFE })

        binding.sectionMie.tvCategoryTitle.text = "Mie"
        val adapterMie = MenuAdapter(
            onItemClick = { menu -> navigateToDetail(menu) },
            onAddClick = { menu -> onAddMenuClicked(menu) }
        ).apply { isGridView = true }
        binding.sectionMie.rvHorizontalMenu.adapter = adapterMie
        adapterMie.submitList(listMenu.filter { it.categoryId == MenuCategory.MIE })

        binding.sectionNasi.tvCategoryTitle.text = "Nasi"
        val adapterNasi = MenuAdapter(
            onItemClick = { menu -> navigateToDetail(menu) },
            onAddClick = { menu -> onAddMenuClicked(menu) }
        ).apply { isGridView = true }
        binding.sectionNasi.rvHorizontalMenu.adapter = adapterNasi
        adapterNasi.submitList(listMenu.filter { it.categoryId == MenuCategory.RICE })
    }

    private fun navigateToDetail(menu: Menu) {
        (requireActivity() as? BaseActivity)?.navigateToWithData(
            destination = MenuDetailActivity::class.java,
            extra = menu,
            key = ExtraKey.MENU
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

        val filteredAdapter = MenuAdapter(
            onItemClick = { menu -> navigateToDetail(menu) },
            onAddClick = { menu -> onAddMenuClicked(menu) }
        )
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
        if (_binding == null) return

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

    private fun onAddMenuClicked(menu: Menu) {
        val userId = session.getUserId()
        if (userId != -1) {
            cartViewModel.addToCart(menu, userId)
            val successDialog = DialogSuccess("Berhasil Ditambahkan ke keranjang")
            successDialog.show(parentFragmentManager, "success_dialog")
        }
    }

    private fun serviceGetAllMenu() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                db.menuDao().getAllMenu().collect { listMenu ->
                    _binding?.let {
                        allMenuItems = listMenu
                        setupSections(listMenu)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                if (e is CancellationException) {
                    throw e
                }
                _binding?.let {
                    if (isAdded) {
                        val activityRoot = requireActivity().findViewById<View>(android.R.id.content)
                        activityRoot.showErrorSnackBar("Gagal memuat semua menu", activityRoot)
                    }
                }
            }
        }
    }

    private fun serviceGetByCategory(categoryId: Int, adapter: MenuAdapter) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                db.menuDao().getMenuByCategory(categoryId).collect { listMenu ->
                    _binding?.let {
                        adapter.submitList(listMenu)
                    }
                }
            } catch (e: Exception) {
                if (e is CancellationException) {
                    throw e
                }

                Log.e("DATABASE_ERROR", "Error getByCategory: ${e.message}")

                _binding?.let {
                    if (isAdded) {
                        val activityRoot = requireActivity().findViewById<View>(android.R.id.content)
                        activityRoot.showErrorSnackBar("Gagal memuat kategori", activityRoot)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}