package com.myproject.warkopgundar.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val PREF_NAME = "SESSION-WARKOPGUNDAR"
    private val IS_LOGIN = "is_login"
    private val KEY_USEREMAIL = "user_email"
    private val KEY_USERID = "user_id"

    private val pref: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = pref.edit()

    fun createLoginSession(userId: Int, email: String) {
        editor.putBoolean(IS_LOGIN, true)
        editor.putInt(KEY_USERID, userId)
        editor.putString(KEY_USEREMAIL, email)
        editor.commit()
    }

    fun getUserEmail(): String? {
        return pref.getString(KEY_USEREMAIL, null)
    }

    fun getUserId(): Int {
        return pref.getInt(KEY_USERID, -1)
    }

    fun isLoggedIn(): Boolean {
        return pref.getBoolean(IS_LOGIN, false)
    }

    fun logout() {
        editor.clear()
        editor.commit()
    }
}