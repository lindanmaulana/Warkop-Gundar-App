package com.myproject.warkopgundar

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
        val targetMenu = intent.getIntExtra("TARGET_MENU_ID", -1)

        when {
            targetMenu != -1 -> {
                navigateToTarget(targetMenu)
            }

            else -> {
                navigateToTarget(R.id.actionHome)
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
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