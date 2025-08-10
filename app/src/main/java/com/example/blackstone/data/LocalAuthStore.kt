package com.example.blackstone.data

import android.content.Context

class LocalAuthStore(context: Context) {
    private val prefs = context.getSharedPreferences("auth_demo", Context.MODE_PRIVATE)

    fun saveUser(email: String, password: String, name: String, affiliation: String) {
        prefs.edit()
            .putString("email", email)
            .putString("password", password)
            .putString("name", name)
            .putString("affiliation", affiliation)
            .apply()
    }

    fun getEmail(): String? = prefs.getString("email", null)
    fun getPassword(): String? = prefs.getString("password", null)
}