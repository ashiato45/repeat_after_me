package com.ashiato45dev.ashia.repeatafterme
import android.os.Bundle
import android.preference.PreferenceActivity


class SettingsActivity : PreferenceActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preference)
    }
}