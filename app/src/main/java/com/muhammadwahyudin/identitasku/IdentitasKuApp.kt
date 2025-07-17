package com.muhammadwahyudin.identitasku

import android.util.Log
import androidx.multidex.MultiDexApplication
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.muhammadwahyudin.identitasku.di.appModule
import com.muhammadwahyudin.identitasku.di.dataModule
import com.orhanobut.hawk.Hawk
import com.yariksoffice.lingver.Lingver
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
        System.loadLibrary("sqlcipher")
        Hawk.init(this).build()
        FirebaseAnalytics.getInstance(this)
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
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
        private val CRASHLYTICS_KEY_PRIORITY = "priority"
        private val CRASHLYTICS_KEY_TAG = "tag"
        private val CRASHLYTICS_KEY_MESSAGE = "message"

        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) return
            val throwable = t ?: Exception(message)
            FirebaseCrashlytics.getInstance().setCustomKey(CRASHLYTICS_KEY_PRIORITY, priority)
            FirebaseCrashlytics.getInstance().setCustomKey(CRASHLYTICS_KEY_TAG, tag.orEmpty())
            FirebaseCrashlytics.getInstance().setCustomKey(CRASHLYTICS_KEY_MESSAGE, message)
            FirebaseCrashlytics.getInstance().recordException(throwable)
        }
    }
}