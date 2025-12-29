package com.myproject.warkopgundar

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.myproject.warkopgundar.databinding.ActivityDashboardBinding

class DashboardActivity : BaseActivity() {
    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        setupBottomNav()
        val targetMenu = intent.getIntExtra("TARGET_MENU_ID", -1)

        when {
            targetMenu != -1 -> {
                navigateToTarget(targetMenu)
            }

            else -> {
                replaceFragmentDashboard(R.id.fragmentContainer, DashboardHomeFragment())
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupBottomNav() {
        binding.actionHome.setOnClickListener {
            replaceFragmentDashboard(R.id.fragmentContainer, DashboardHomeFragment())
            updateUI(binding.actionHome)
        }

        binding.actionMenu.setOnClickListener {
            replaceFragmentDashboard(R.id.fragmentContainer, DashboardMenuFragment())
            updateUI(binding.actionMenu)
        }

        binding.actionCart.setOnClickListener {
            replaceFragmentDashboard(R.id.fragmentContainer, DashboardCartFragment())
            updateUI(binding.actionCart)
        }

        binding.actionSetting.setOnClickListener {
            replaceFragmentDashboard(R.id.fragmentContainer, DashboardSettingFragment())
            updateUI(binding.actionSetting)
        }
    }

    private fun navigateToTarget(menuId: Int) {
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