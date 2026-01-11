package com.myproject.warkopgundar.features.dashboard.fragments.menus

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.content.IntentCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.myproject.warkopgundar.utils.AnimType
import com.myproject.warkopgundar.utils.BaseActivity
import com.myproject.warkopgundar.utils.ExtraKey
import com.myproject.warkopgundar.R
import com.myproject.warkopgundar.utils.SessionManager
import com.myproject.warkopgundar.features.auth.AuthSigninActivity
import com.myproject.warkopgundar.databinding.ActivityMenuDetailBinding
import com.myproject.warkopgundar.db.Menu
import com.myproject.warkopgundar.features.dashboard.fragments.carts.CartViewModel
import com.myproject.warkopgundar.features.dashboard.DashboardActivity
import com.myproject.warkopgundar.features.order.OrderCheckoutActivity
import com.myproject.warkopgundar.utils.DialogSuccess
import com.myproject.warkopgundar.utils.toLikeCountFormat
import com.myproject.warkopgundar.utils.toParseCurrency

class MenuDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityMenuDetailBinding
    private lateinit var session: SessionManager
    private val cartViewModel: CartViewModel by viewModels()
    private var menu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMenuDetailBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        session = SessionManager(this@MenuDetailActivity)
        val dataMenu = IntentCompat.getParcelableExtra(intent, ExtraKey.MENU.value, Menu::class.java)

        if (dataMenu == null) {
            navigateTo(DashboardActivity::class.java, R.id.actionMenu, isFinal = true)
            return
        }

        this.menu = dataMenu
        setupDetailmenu(dataMenu)

        if (!session.isLoggedIn()) {
            session.logout()
            navigateToWithData(
                destination = AuthSigninActivity::class.java,
                extra = "Sesi kamu berakhir. Harap untuk masuk kembali!",
                key = ExtraKey.SESSION_EXPIRED,
                isFinal = true)
        }

        setupActions()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupActions() {
        binding.actionBack.setOnClickListener {
            navigateTo(DashboardActivity::class.java, R.id.actionMenu, typeTransition = AnimType.SLIDE)
        }

        binding.actionCheckout.setOnClickListener {
            navigateTo(OrderCheckoutActivity::class.java, typeTransition = AnimType.SLIDE)
        }

        binding.actionAddToCart.setOnClickListener {
            menu?.let { data ->
                val userId = session.getUserId()

                if (userId != -1) cartViewModel.addToCart(data, session.getUserId())
            }

            val successDialog =
                DialogSuccess("Berhasil Ditambahkan ke keranjang")
            successDialog.show(supportFragmentManager, "success_dialog")
            binding.etNote.text?.clear()
        }
    }

    private fun setupDetailmenu(data: Menu) {
        binding.tvMenuName.text = data.name
        binding.tvMenuPrice.text = data.price.toParseCurrency()
        binding.tvMenuDescription.text = data.description
        binding.tvRatingValue.text = data.rating.toString()
        binding.tvRatingCount.text = data.likes.toLikeCountFormat()
        Glide.with(this)
            .load(data.imageRes)
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_placeholder)
            .centerCrop()
            .into(binding.imgMenu)
    }
}