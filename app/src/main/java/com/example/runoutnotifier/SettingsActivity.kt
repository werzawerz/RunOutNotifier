package com.example.runoutnotifier

import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity

class SettingsActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when(key) {

        }
    }
}