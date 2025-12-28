package com.myproject.warkopgundar

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val PREF_NAME = "SESSION-WARKOPGUNDAR"
    private val IS_LOGIN = "is_login"
    private val KEY_PHONENUMBER = "user_phoneNumber"

    private val pref: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = pref.edit()

    fun createLoginSession(phoneNumber: String) {
        editor.putBoolean(IS_LOGIN, true)
        editor.putString(KEY_PHONENUMBER, phoneNumber)
        editor.commit()
    }

    fun getPhoneNumber(): String? {
        return pref.getString(KEY_PHONENUMBER, null)
    }

    fun isLoggedIn(): Boolean {
        return pref.getBoolean(IS_LOGIN, false)
    }

    fun logout() {
        editor.clear()
        editor.commit()
    }
}