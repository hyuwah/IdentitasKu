package com.muhammadwahyudin.identitasku.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.muhammadwahyudin.identitasku.BuildConfig
import com.muhammadwahyudin.identitasku.R
import com.muhammadwahyudin.identitasku.ui._helper.CustomTabHelper
import org.jetbrains.anko.support.v4.act
import org.jetbrains.anko.support.v4.longToast


class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Settings"
        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, SettingsFragment())
            .commit()
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        private val PRIVACY_POLICY_URL = "https://sites.google.com/view/identitasku/beranda"
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.app_preferences, rootKey)

            findPreference<Preference>("setting_pref_privacy_policy")?.setOnPreferenceClickListener {
                val builder = CustomTabsIntent.Builder()
                builder.setToolbarColor(ContextCompat.getColor(act, R.color.colorPrimary))
                builder.setShowTitle(true)
                val customTabsIntent = builder.build()

                // check is chrome available
                val customTabHelper = CustomTabHelper()
                val packageName = customTabHelper.getPackageNameToUse(act, PRIVACY_POLICY_URL)
                if (packageName == null) {
                    // if chrome not available open in browser
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(PRIVACY_POLICY_URL))
                    val browserChooserIntent = Intent.createChooser(browserIntent, "Choose browser of your choice")
                    startActivity(browserChooserIntent)
                } else {
                    customTabsIntent.intent.setPackage(packageName)
                    customTabsIntent.launchUrl(act, Uri.parse(PRIVACY_POLICY_URL))
                }
                true
            }

            findPreference<Preference>("setting_pref_about")?.setOnPreferenceClickListener {
                longToast("Version: ${BuildConfig.APPLICATION_ID} ${BuildConfig.VERSION_NAME}")
                true
            }
        }
    }
}