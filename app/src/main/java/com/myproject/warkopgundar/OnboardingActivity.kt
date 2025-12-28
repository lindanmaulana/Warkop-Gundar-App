package com.myproject.warkopgundar

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.myproject.warkopgundar.databinding.ActivityOnboardingBinding
import com.myproject.warkopgundar.databinding.ActivitySplashScreenBinding

class OnboardingActivity : BaseActivity() {
    private lateinit var binding: ActivityOnboardingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        val adapter = OnboardingAdapter(this)
        val viewPager = binding.viewPagerOnboarding

        val actionSkip = binding.actionSkip
        val actionNext = binding.actionNext
        val actionPrev = binding.actionPrev

        val tvProgress = binding.tvProgress

        binding.viewPagerOnboarding.adapter = adapter

        actionNext.setOnClickListener {
            val currentItem = viewPager.currentItem

            if (currentItem < adapter.itemCount - 1) {
                viewPager.setCurrentItem(currentItem + 1, true)
            } else {
                navigateTo(AuthSigninActivity::class.java, isFinal = true)
            }
        }

        actionPrev.setOnClickListener {
            val currentItem = viewPager.currentItem

            if (currentItem > 0) {
                viewPager.setCurrentItem(currentItem - 1, true)
            } else {
                onBackPressedDispatcher.onBackPressed()
            }
        }

        binding.viewPagerOnboarding.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tvProgress.text = (position + 1).toString()

                when(position) {
                    0 -> {
                        actionPrev.visibility = View.GONE
                    }

                    2 -> {
                        actionSkip.visibility = View.GONE
                        actionNext.text = "Get Started"
                    }

                    else -> {
                        actionPrev.visibility = View.VISIBLE
                        actionSkip.visibility = View.VISIBLE
                    }
                }
            }
        })

        TabLayoutMediator(binding.tablayout, binding.viewPagerOnboarding) { tab, position ->
            tab.text = ""
        }.attach()

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}