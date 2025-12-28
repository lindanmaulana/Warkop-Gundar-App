package com.myproject.warkopgundar

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment


enum class AnimType {
    SLIDE, FADE, NONE
}
open class BaseActivity: AppCompatActivity() {
    fun navigateTo(destination: Class<*>, targetMenuId: Int? = null, typeTransition: AnimType = AnimType.FADE, isFinal: Boolean = false) {
        val intent = Intent(this, destination)

        val options = when(typeTransition) {
            AnimType.FADE -> ActivityOptions.makeCustomAnimation(
                this,
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )

            AnimType.SLIDE -> ActivityOptions.makeCustomAnimation(
                this,
                android.R.anim.slide_in_left,
                android.R.anim.slide_out_right
            )

            AnimType.NONE -> null
        }

        when {
            isFinal -> {
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

            destination.name.contains("Dashboard") -> {
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
        }

        targetMenuId?.let {
            intent.putExtra("TARGET_MENU_ID", it)
        }

        startActivity(intent, options?.toBundle())
        if (isFinal) finishAffinity()
    }

    fun replaceFragmentDashboard(containerId: Int, fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.slide_in_left,
                android.R.anim.slide_out_right)
            .replace(containerId, fragment)
            .commit()
    }
}

fun AppCompatActivity.navigateTo(destination: Class<*>, targetMenuId: Int? = null) {
    val intent = Intent(this, destination)

    val options = ActivityOptions.makeCustomAnimation(
        this,
        android.R.anim.fade_in,
        android.R.anim.fade_out
    )

    if (destination.name.contains("Dashboard")) {
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        targetMenuId?.let { intent.putExtra("TARGET_MENU_ID", it) }
    }

    startActivity(intent, options.toBundle())
    finish()
}