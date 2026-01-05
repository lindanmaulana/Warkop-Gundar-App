package com.myproject.warkopgundar

import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Color
import android.os.Parcelable
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.Serializable


enum class AnimType {
    SLIDE, FADE, NONE
}

enum class ExtraKey(val value: String) {
    MENU("EXTRA_MENU_DATA"),
    CATEGORY("EXTRA_CATEGORY_FILTER"),
    USER_ID("EXTRA_USER_ID"),
    ORDER_ID("EXTRA_ORDER_ID"),
    IS_EDIT("EXTRA_IS_EDIT_MODE"),
    MESSAGE("EXTRA_MESSAGE"),
    SESSION_LOGOUT("EXTRA_SESSION_LOGOUT"),
    SESSION_EXPIRED("EXTRA_SESSION_EXPIRED")
}

open class BaseActivity: AppCompatActivity() {
    fun navigateTo(
        destination: Class<*>,
        targetMenuId: Int? = null,
        typeTransition: AnimType = AnimType.FADE,
        isFinal: Boolean = false
    ) {
        val intent = Intent(this, destination)

        if (isFinal) intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        if (destination.name.contains("Dashboard")) {
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        targetMenuId?.let { intent.putExtra("TARGET_MENU_ID", it) }

        performNavigation(intent, typeTransition, isFinal)
    }

    fun navigateToWithData(
        destination: Class<*>,
        extra: Any,
        key: ExtraKey = ExtraKey.MENU,
        targetMenuId: Int? = null,
        typeTransition: AnimType = AnimType.FADE,
        isFinal: Boolean = false
    ) {
        val intent = Intent(this, destination)

        if (isFinal) intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        when (extra) {
            is String -> intent.putExtra(key.value, extra)
            is Int -> intent.putExtra(key.value, extra)
            is Parcelable -> intent.putExtra(key.value, extra)
            is Serializable -> intent.putExtra(key.value, extra)
        }

        targetMenuId?.let { intent.putExtra("TARGET_MENU_ID", it) }

        performNavigation(intent, typeTransition, isFinal)
    }

    private fun performNavigation(intent: Intent, type: AnimType, isFinal: Boolean) {
        val options = when(type) {
            AnimType.FADE -> ActivityOptions.makeCustomAnimation(this, android.R.anim.fade_in, android.R.anim.fade_out)
            AnimType.SLIDE -> ActivityOptions.makeCustomAnimation(this, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            AnimType.NONE -> null
        }

        startActivity(intent, options?.toBundle())
        if (isFinal) finish()
    }

    fun showDialogSuccess(title: String, message: String) {
        MaterialAlertDialogBuilder(this)
            .setTitle(title)
            .setMessage(message)
            .setIcon(R.drawable.icon_status_success)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    fun showDialogError(title: String, message: String) {
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle(title)
            .setMessage(message)
            .setIcon(R.drawable.icon_status_warning)
            .setPositiveButton("Tutup") { dialog, _ ->
                dialog.dismiss()
            }
            .show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED)
    }

    fun replaceFragmentDashboard(containerId: Int, fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.slide_in_left,
                android.R.anim.slide_out_right)
            .replace(containerId, fragment)
            .commit()
    }
}