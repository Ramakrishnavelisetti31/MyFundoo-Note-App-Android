package com.example.fundooapp.model

import android.content.Context
import android.content.SharedPreferences

object SharedPreference {
    var sharedPreferences: SharedPreferences? = null

    fun initSharedPreferences(context: Context) {
        sharedPreferences = context.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE)
    }

    fun addString(key: String, value: String) {
        val editor = sharedPreferences?.edit()
        if (editor != null) {
            editor.putString(key, value)
            editor.apply()
        }
    }

    fun getString(key: String): String {
        return sharedPreferences?.getString(key, "").toString()
    }

    fun clear() {
        val editor = sharedPreferences?.edit()
        if (editor != null) {
            editor.clear()
            editor.apply()
        }
    }
}