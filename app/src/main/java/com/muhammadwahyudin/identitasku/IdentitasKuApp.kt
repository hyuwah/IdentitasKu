package com.muhammadwahyudin.identitasku

import android.util.Log
import androidx.multidex.MultiDexApplication
import com.crashlytics.android.Crashlytics
import com.facebook.stetho.Stetho
import com.google.firebase.analytics.FirebaseAnalytics
import com.muhammadwahyudin.identitasku.di.appModule
import com.muhammadwahyudin.identitasku.di.dataModule
import com.orhanobut.hawk.Hawk
import com.yariksoffice.lingver.Lingver
import io.fabric.sdk.android.Fabric
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber
import timber.log.Timber.DebugTree

/**
 * Main entry of project app
 */
class IdentitasKuApp : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        Hawk.init(this).build()
        Fabric.with(this, Crashlytics())
        FirebaseAnalytics.getInstance(this)
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
            Stetho.initializeWithDefaults(this)
        } else {
            Timber.plant(CrashReleaseTree())
        }
        Lingver.init(this, "id")

        startKoin {
            androidContext(this@IdentitasKuApp)
            modules(
                dataModule,
                appModule
            )
        }
    }

    inner class CrashReleaseTree : Timber.Tree() {
        val CRASHLYTICS_KEY_PRIORITY = "priority"
        val CRASHLYTICS_KEY_TAG = "tag"
        val CRASHLYTICS_KEY_MESSAGE = "message"

        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) return
            val throwable = t ?: Exception(message)
            Crashlytics.setInt(CRASHLYTICS_KEY_PRIORITY, priority)
            Crashlytics.setString(CRASHLYTICS_KEY_TAG, tag)
            Crashlytics.setString(CRASHLYTICS_KEY_MESSAGE, message)
            Crashlytics.logException(throwable)
        }
    }
}