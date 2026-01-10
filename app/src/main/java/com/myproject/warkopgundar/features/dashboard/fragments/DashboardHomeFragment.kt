package com.myproject.warkopgundar.features.dashboard.fragments

import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.myproject.warkopgundar.db.AppDatabase
import com.myproject.warkopgundar.utils.BaseActivity
import com.myproject.warkopgundar.utils.ExtraKey
import com.myproject.warkopgundar.db.MenuCategory
import com.myproject.warkopgundar.R
import com.myproject.warkopgundar.utils.SessionManager
import com.myproject.warkopgundar.features.auth.AuthSigninActivity
import com.myproject.warkopgundar.databinding.FragmentDashboardHomeBinding
import com.myproject.warkopgundar.features.dashboard.DashboardActivity
import com.myproject.warkopgundar.utils.showErrorSnackBar
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class DashboardHomeFragment : Fragment() {
    private var _binding: FragmentDashboardHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: AppDatabase
    private lateinit var session: SessionManager
    private lateinit var username: TextView
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
        _binding = FragmentDashboardHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        username = binding.tvHeaderTitle
        db = AppDatabase.Companion.getDatabase(requireContext())
        session = SessionManager(requireContext())

        setupActions()
    }

    private fun setupActions() {
        binding.menuCoffe.setOnClickListener {
            (activity as? BaseActivity)?.navigateToWithData(
                destination = DashboardActivity::class.java,
                targetMenuId = R.id.actionMenu,
                extra = MenuCategory.COFFE,
                key = ExtraKey.CATEGORY,
            )
        }

        binding.menuMie.setOnClickListener {
            (activity as? BaseActivity)?.navigateToWithData(
                destination = DashboardActivity::class.java,
                targetMenuId = R.id.actionMenu,
                extra = MenuCategory.MIE,
                key = ExtraKey.CATEGORY,
            )
        }

        binding.menuRice.setOnClickListener {
            (activity as? BaseActivity)?.navigateToWithData(
                destination = DashboardActivity::class.java,
                targetMenuId = R.id.actionMenu,
                extra = MenuCategory.RICE,
                key = ExtraKey.CATEGORY,
            )
        }
    }

    override fun onResume() {
        super.onResume()

        val userEmail = session.getUserEmail()

        if (userEmail == null) {
            session.logout()
            (requireActivity() as BaseActivity).navigateTo(AuthSigninActivity::class.java, isFinal = true)
            return
        }

        lifecycleScope.launch {
            try {
                val user = db.userDao().getUserByEmail(userEmail)

                if (isAdded && user != null) username.text = "Hello,\n${user.username}"
            } catch (e: Exception) {
                e.printStackTrace()
                binding.root.showErrorSnackBar("Terjadi kesalahan sistem, please try again later", binding.tvHeaderTitle)
            }
        }
    }

    companion object {
        fun newInstance(param1: String, param2: String) =
            DashboardHomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}