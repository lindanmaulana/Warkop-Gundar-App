package com.myproject.warkopgundar

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.myproject.warkopgundar.databinding.ActivityDashboardBinding

class DashboardActivity : BaseActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private var currentMenuId: Int = R.id.actionHome

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        setupBottomNav()
        setupBackPressed()
        handleIntentNavigation(intent)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntentNavigation(intent)
    }

    private fun handleIntentNavigation(intent: Intent?) {
        val targetMenuId = intent?.getIntExtra("TARGET_MENU_ID", R.id.actionHome) ?: R.id.actionHome
        val categoryFilterId = intent?.getIntExtra(ExtraKey.CATEGORY.value, -1) ?: -1

        when(targetMenuId) {
            R.id.actionHome -> binding.actionHome.performClick()
            R.id.actionMenu -> binding.actionMenu.performClick()
            R.id.actionCart -> binding.actionCart.performClick()
            R.id.actionSetting -> binding.actionSetting.performClick()
        }

        if (targetMenuId == R.id.actionMenu && categoryFilterId != null) {
            val bundle = Bundle().apply {
                putInt("SELECTED_CATEGORY", categoryFilterId)
            }

            val menuFragment = DashboardMenuFragment().apply {
                arguments = bundle
            }

            supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, menuFragment).commit()
//            binding.root.postDelayed({
//                val menuFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
//
//                if (menuFragment is DashboardMenuFragment && menuFragment.isAdded) {
//                    menuFragment.applyFilter(categoryFilterId)
//                }
//            }, 400)
        }
    }

    private fun setupBackPressed() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (currentMenuId != R.id.actionHome) {
                    navigateToTarget(R.id.actionHome)
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }

    private fun setupBottomNav() {
        binding.actionHome.setOnClickListener { navigateToTarget(R.id.actionHome) }
        binding.actionMenu.setOnClickListener { navigateToTarget(R.id.actionMenu) }
        binding.actionCart.setOnClickListener { navigateToTarget(R.id.actionCart) }
        binding.actionSetting.setOnClickListener { navigateToTarget(R.id.actionSetting) }
    }

    private fun navigateToTarget(menuId: Int) {
        if (currentMenuId == menuId && supportFragmentManager.findFragmentById(R.id.fragmentContainer) != null) return

        currentMenuId = menuId
        when (menuId) {
            R.id.actionHome -> {
                replaceFragmentDashboard(R.id.fragmentContainer, DashboardHomeFragment())
                updateUI(binding.actionHome)
            }
            R.id.actionMenu -> {
                replaceFragmentDashboard(R.id.fragmentContainer, DashboardMenuFragment())
                updateUI(binding.actionMenu)
            }
            R.id.actionCart -> {
                replaceFragmentDashboard(R.id.fragmentContainer, DashboardCartFragment())
                updateUI(binding.actionCart)
            }
            R.id.actionSetting -> {
                replaceFragmentDashboard(R.id.fragmentContainer, DashboardSettingFragment())
                updateUI(binding.actionSetting)
            }
        }
    }

    private fun updateUI(activeIcon: ImageView) {
        val icons = listOf(binding.actionHome, binding.actionMenu, binding.actionCart, binding.actionSetting)
        icons.forEach { icon ->
            icon.alpha = if (icon == activeIcon) 1.0f else 0.5f
        }
    }
}