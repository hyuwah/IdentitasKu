package com.muhammadwahyudin.identitasku

import androidx.multidex.MultiDexApplication
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.commonsware.cwac.saferoom.SafeHelperFactory
import com.facebook.stetho.Stetho
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
                        DbUtils.populateData(appDB)
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
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
            Stetho.initializeWithDefaults(this)
        }
    }

}