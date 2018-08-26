package com.example.ashia.filereadtest
import android.os.Bundle
import android.preference.PreferenceActivity
import com.example.ashia.filereadtest.R


class SettingsActivity : PreferenceActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preference)
    }
}