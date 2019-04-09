package com.muhammadwahyudin.identitasku

import android.util.Log
import androidx.multidex.MultiDexApplication
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.commonsware.cwac.saferoom.SafeHelperFactory
import com.crashlytics.android.Crashlytics
import com.facebook.stetho.Stetho
import com.google.firebase.analytics.FirebaseAnalytics
import com.muhammadwahyudin.identitasku.data.Constants
import com.muhammadwahyudin.identitasku.data.db.AppDatabase
import com.muhammadwahyudin.identitasku.data.db.DataDao
import com.muhammadwahyudin.identitasku.data.db.DataTypeDao
import com.muhammadwahyudin.identitasku.data.repository.AppRepository
import com.muhammadwahyudin.identitasku.data.repository.IAppRepository
import com.muhammadwahyudin.identitasku.ui.home.HomeViewModelFactory
import com.muhammadwahyudin.identitasku.utils.Commons
import com.muhammadwahyudin.identitasku.utils.DbUtils
import com.orhanobut.hawk.Hawk
import io.fabric.sdk.android.Fabric
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.*
import timber.log.Timber
import timber.log.Timber.DebugTree

/**
 * Main entry of project app
 */
class IdentitasKuApp : MultiDexApplication(), KodeinAware {
    override val kodein: Kodein = Kodein.lazy {
        bind<AppDatabase>() with eagerSingleton {
            Room.databaseBuilder(applicationContext, AppDatabase::class.java, Constants.DB_NAME)
                .fallbackToDestructiveMigration()
                .openHelperFactory(SafeHelperFactory(Commons.getDeviceId(applicationContext).toCharArray()))
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        val appDB = instance<AppDatabase>()
                        DbUtils.populateDataType(appDB)
                        if (BuildConfig.DEBUG) DbUtils.populateData(appDB)
                    }
                })
                .build()
        }
        bind<DataTypeDao>() with singleton { instance<AppDatabase>().dataTypeDao() }
        bind<DataDao>() with singleton { instance<AppDatabase>().dataDao() }
        bind<IAppRepository>() with singleton { AppRepository(instance(), instance()) }
        bind() from provider { HomeViewModelFactory(instance()) }
    }

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