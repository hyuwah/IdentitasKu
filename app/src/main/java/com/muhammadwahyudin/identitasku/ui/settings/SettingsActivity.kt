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
import com.muhammadwahyudin.identitasku.data.Constants.PRIVACY_POLICY_URL
import com.muhammadwahyudin.identitasku.ui._helper.CustomTabHelper
import com.muhammadwahyudin.identitasku.utils.longToast

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Settings"
        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, SettingsFragment())
            .commit()
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.app_preferences, rootKey)

            findPreference<Preference>("setting_pref_privacy_policy")
                ?.setOnPreferenceClickListener {
                    val builder = CustomTabsIntent.Builder()
                    builder.setToolbarColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorPrimary
                        )
                    )
                    builder.setShowTitle(true)
                    val customTabsIntent = builder.build()

                    // check is chrome available
                    val customTabHelper = CustomTabHelper()
                    val packageName =
                        customTabHelper.getPackageNameToUse(requireContext(), PRIVACY_POLICY_URL)
                    if (packageName == null) {
                        // if chrome not available open in browser
                        val browserIntent =
                            Intent(Intent.ACTION_VIEW, Uri.parse(PRIVACY_POLICY_URL))
                        val browserChooserIntent =
                            Intent.createChooser(browserIntent, "Choose browser of your choice")
                        startActivity(browserChooserIntent)
                    } else {
                        customTabsIntent.intent.setPackage(packageName)
                        customTabsIntent.launchUrl(requireContext(), Uri.parse(PRIVACY_POLICY_URL))
                    }
                    true
                }

            findPreference<Preference>("setting_pref_about")?.setOnPreferenceClickListener {
                requireActivity()
                    .longToast(
                        """
                        Version:
                        ${BuildConfig.APPLICATION_ID}
                        ${BuildConfig.VERSION_NAME}
                    """.trimIndent()
                    )
                true
            }
            val build = if (BuildConfig.DEBUG) " (Debug)" else ""
            findPreference<Preference>("setting_pref_about")?.summary =
                "Build: ${BuildConfig.VERSION_NAME}$build"
        }
    }
}